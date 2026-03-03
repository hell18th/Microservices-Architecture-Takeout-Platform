package com.sky.order.service;

import com.sky.api.dto.SalesDTO;
import com.sky.order.domain.dto.*;
import com.sky.order.domain.vo.OrderStatusNumberVO;
import com.sky.order.domain.vo.OrderSubmitVO;
import com.sky.order.domain.vo.OrderVO;
import com.sky.result.PageResult;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    PageResult<OrderVO> history(OrderPageQueryDTO orderPageQueryDTO);

    OrderVO getOrderDetail(Long id);

    void cancel(Long id);

    void again(Long id);

    PageResult<OrderVO> search(OrderPageQueryDTO orderPageQueryDTO);

    OrderStatusNumberVO getStatusNumber();

    void confirm(OrderConfirmDTO orderConfirmDTO);

    void reject(OrderRejectDTO orderRejectDTO);

    void cancelWithReason(OrderCancelDTO orderCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    void reminder(Long id);

    Double turnover(LocalDateTime dayBegin, LocalDateTime dayEnd);

    Integer countByDateAndStatus(LocalDateTime dayBegin, LocalDateTime dayEnd, Integer status);

    List<SalesDTO> getSales(LocalDateTime dayBegin, LocalDateTime dayEnd);
}