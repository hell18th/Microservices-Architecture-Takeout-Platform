package com.sky.order.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.sky.api.client.AddressClient;
import com.sky.api.client.ShoppingCarClient;
import com.sky.api.dto.ApiAddress;
import com.sky.api.dto.ApiShoppingCar;
import com.sky.api.dto.SalesDTO;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.exception.OrderException;
import com.sky.order.domain.dto.*;
import com.sky.order.domain.entity.OrderDetail;
import com.sky.order.domain.entity.Orders;
import com.sky.order.domain.vo.OrderStatusNumberVO;
import com.sky.order.domain.vo.OrderSubmitVO;
import com.sky.order.domain.vo.OrderVO;
import com.sky.order.mapper.OrderDetailMapper;
import com.sky.order.mapper.OrderMapper;
import com.sky.order.service.OrderService;
import com.sky.order.websocket.WebSocketServer;
import com.sky.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressClient addressClient;
    @Autowired
    private ShoppingCarClient shoppingCarClient;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 1. 根据地址id查询地址信息，验证地址是否存在
        ApiAddress address = addressClient.getById(ordersSubmitDTO.getAddressId()).getData();
        if (address == null) {
            throw new OrderException(MessageConstant.ADDRESS_NOT_FOUND);
        }

        // 2. 获取当前登录用户id，查询购物车数据
        Long userId = BaseContext.getCurrentId();
//        ApiShoppingCar shoppingCar = ApiShoppingCar.builder().userId(userId).build();
        List<ApiShoppingCar> list = shoppingCarClient.list().getData();
        if (list == null || list.isEmpty()) {
            throw new OrderException(MessageConstant.SHOPPING_CAR_EMPTY);
        }

        // 3. 构造订单对象，设置订单基本信息
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(address.getPhone());
        orders.setAddress(address.getDetail());
        orders.setConsignee(address.getConsignee());

        // 4. 保存订单信息到数据库
        orderMapper.insert(orders);

        // - 保存订单详情
        List<OrderDetail> orderDetails = list.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).toList();
        orderDetailMapper.insertBatch(orderDetails);
        // - 清空购物车
        shoppingCarClient.clean();
        // - 返回正确的OrderSubmitVO
        return OrderSubmitVO.builder().id(orders.getId()).orderNumber(orders.getNumber()).orderAmount(orders.getAmount()).orderTime(orders.getOrderTime()).build();
    }

    @Override
    @Transactional
    public PageResult<OrderVO> history(OrderPageQueryDTO orderPageQueryDTO) {
        orderPageQueryDTO.setUserId(BaseContext.getCurrentId());
        PageHelper.startPage(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());
        Page<OrderVO> page = orderMapper.page(orderPageQueryDTO);
        List<OrderVO> orderVOList = page.getResult();
        if (orderVOList != null && !orderVOList.isEmpty()) {
            for (OrderVO orderVO : orderVOList) {
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderVO.getId());
                orderVO.setOrderDetailList(orderDetailList);
            }
        }
        return new PageResult<>(page.getTotal(), orderVOList);
    }

    @Override
    @Transactional
    public OrderVO getOrderDetail(Long id) {
        Orders orders = orderMapper.getById(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null || orders.getStatus() > Orders.TO_BE_CONFIRMED) {
            throw new OrderException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //微信支付退款接口
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(MessageConstant.CANCELLED_BY_USER);
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    @Transactional
    public void again(Long id) {
        Long userId = BaseContext.getCurrentId();
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        List<ApiShoppingCar> list = orderDetailList.stream().map(item -> {
            ApiShoppingCar shoppingCar = new ApiShoppingCar();
            BeanUtils.copyProperties(item, shoppingCar);
            shoppingCar.setUserId(userId);
            shoppingCar.setCreateTime(LocalDateTime.now());
            return shoppingCar;
        }).toList();
        shoppingCarClient.insertBatch(list);
    }

    @Override
    public PageResult<OrderVO> search(OrderPageQueryDTO orderPageQueryDTO) {
        PageHelper.startPage(orderPageQueryDTO.getPage(), orderPageQueryDTO.getPageSize());
        Page<OrderVO> page = orderMapper.page(orderPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public OrderStatusNumberVO getStatusNumber() {
        Integer toBeConfirmed = orderMapper.selectNumberByStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.selectNumberByStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.selectNumberByStatus(Orders.DELIVERY_IN_PROGRESS);
        return OrderStatusNumberVO.builder().toBeConfirmed(toBeConfirmed).confirmed(confirmed).deliveryInProgress(deliveryInProgress).build();
    }

    @Override
    public void confirm(OrderConfirmDTO orderConfirmDTO) {
        orderMapper.update(Orders.builder().id(orderConfirmDTO.getId()).status(Orders.CONFIRMED).build());
    }

    @Override
    public void reject(OrderRejectDTO orderRejectDTO) {
        Orders orders = orderMapper.getById(orderRejectDTO.getId());
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if (orders.getPayStatus().equals(Orders.PAID)) {
            //微信退款接口
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(orderRejectDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void cancelWithReason(OrderCancelDTO orderCancelDTO) {
        Orders orders = orderMapper.getById(orderCancelDTO.getId());
        if (orders == null) {
            throw new OrderException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(orderCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void delivery(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null || !orders.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orderMapper.update(Orders.builder().id(id).status(Orders.DELIVERY_IN_PROGRESS).build());
    }

    @Override
    public void complete(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null || !orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orderMapper.update(Orders.builder().id(id).status(Orders.COMPLETED).deliveryTime(LocalDateTime.now()).build());
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);//1表示来单提醒，2表示客户催单
        map.put("orderId", id);
        map.put("content", "订单号：" + orders.getNumber());
        Gson gson = new Gson();
        String jsonString = gson.toJson(map);
        webSocketServer.broadcast(jsonString);
    }

    @Override
    public Double turnover(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        return orderMapper.selectTurnoverByDate(dayBegin, dayEnd, Orders.COMPLETED);
    }

    @Override
    public Integer countByDateAndStatus(LocalDateTime dayBegin, LocalDateTime dayEnd, Integer status) {
        return orderMapper.countByDateAndStatus(dayBegin, dayEnd, status);
    }

    @Override
    public List<SalesDTO> getSales(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        return orderDetailMapper.getSales(dayBegin, dayEnd, Orders.COMPLETED);
    }
}