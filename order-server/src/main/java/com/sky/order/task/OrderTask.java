package com.sky.order.task;

import com.sky.constant.MessageConstant;
import com.sky.order.domain.entity.Orders;
import com.sky.order.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void processTimeoutOrder() {
        log.info("处理超时订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-1);
        List<Orders> orders = orderMapper.selectByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                orderMapper.update(Orders.builder().id(order.getId()).status(Orders.CANCELLED).cancelReason(MessageConstant.ORDER_TIMEOUT).cancelTime(LocalDateTime.now()).build());
            }
        }
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void processDeliveryOrder() {
        log.info("处理待派送订单：{}", LocalDateTime.now());
        List<Orders> orders = orderMapper.selectByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now());
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                orderMapper.update(Orders.builder().id(order.getId()).status(Orders.COMPLETED).build());
            }
        }
    }
}