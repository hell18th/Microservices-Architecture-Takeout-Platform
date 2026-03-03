package com.sky.order.controller.admin;

import com.sky.api.dto.SalesDTO;
import com.sky.order.domain.dto.OrderCancelDTO;
import com.sky.order.domain.dto.OrderConfirmDTO;
import com.sky.order.domain.dto.OrderPageQueryDTO;
import com.sky.order.domain.dto.OrderRejectDTO;
import com.sky.order.domain.entity.Orders;
import com.sky.order.domain.vo.OrderStatusNumberVO;
import com.sky.order.domain.vo.OrderVO;
import com.sky.order.service.OrderService;
import com.sky.result.PageResult;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单管理
 * 处理订单相关的请求
 */
@Slf4j
@RestController
@RequestMapping("admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     *
     * @param orderPageQueryDTO 订单搜索条件
     * @return 订单列表
     */
    @GetMapping("/page")
    public Result<PageResult<OrderVO>> page(@RequestBody OrderPageQueryDTO orderPageQueryDTO) {
        log.info("订单搜索：{}", orderPageQueryDTO);
        PageResult<OrderVO> pageResult = orderService.search(orderPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 统计订单状态数量接口
     * 获取各订单状态的数量统计信息
     *
     * @return Result<OrderStatusNumberVO> 包含订单状态数量统计结果的响应对象
     */
    @GetMapping("/statusNumber")
    public Result<OrderStatusNumberVO> statusNumber() {
        log.info("统计订单状态数据");
        OrderStatusNumberVO orderStatusNumberVO = orderService.getStatusNumber();
        return Result.success(orderStatusNumberVO);
    }

    /**
     * 查询订单详情接口
     * 根据订单ID获取订单详细信息
     *
     * @param id 订单ID
     * @return Result<OrderVO> 包含订单详细信息的响应对象
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 订单确认
     * 商家确认接单
     *
     * @param orderConfirmDTO 订单确认数据传输对象
     * @return Result<Void> 操作结果响应对象
     */
    @PutMapping("/confirm")
    public Result<Void> confirm(@RequestBody OrderConfirmDTO orderConfirmDTO) {
        log.info("订单确认：{}", orderConfirmDTO);
        orderService.confirm(orderConfirmDTO);
        return Result.success();
    }

    /**
     * 订单拒绝
     * 商家拒绝接单
     *
     * @param orderRejectDTO 订单拒绝数据传输对象
     * @return Result<Void> 操作结果响应对象
     */
    @PutMapping("/reject")
    public Result<Void> reject(@RequestBody OrderRejectDTO orderRejectDTO) {
        log.info("订单拒绝：{}", orderRejectDTO);
        orderService.reject(orderRejectDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * 取消订单并记录取消原因
     *
     * @param orderCancelDTO 订单取消数据传输对象
     * @return Result<Void> 操作结果响应对象
     */
    @PutMapping("/cancel")
    public Result<Void> cancel(@RequestBody OrderCancelDTO orderCancelDTO) {
        log.info("取消订单：{}", orderCancelDTO);
        orderService.cancelWithReason(orderCancelDTO);
        return Result.success();
    }

    /**
     * 订单配送
     * 将订单状态改为配送中
     *
     * @param id 订单ID
     * @return Result<Void> 操作结果响应对象
     */
    @PutMapping("/delivery/{id}")
    public Result<Void> delivery(@PathVariable Long id) {
        log.info("订单配送：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 完成订单
     * 将订单状态改为已完成
     *
     * @param id 订单ID
     * @return Result<Void> 操作结果响应对象
     */
    @PutMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id) {
        log.info("订单完成：{}", id);
        orderService.complete(id);
        return Result.success();
    }

    @GetMapping("/turnover")
    public Result<Double> turnover(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        log.info("查询营业额");
        Double turnover = orderService.turnover(dayBegin, dayEnd);
        return Result.success(turnover);
    }

    @GetMapping("/countAll")
    public Result<Integer> countAll(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        Integer count = orderService.countByDateAndStatus(dayBegin, dayEnd, null);
        return Result.success(count);
    }

    @GetMapping("/countCOMPLETED")
    public Result<Integer> countCOMPLETED(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        Integer count = orderService.countByDateAndStatus(dayBegin, dayEnd, Orders.COMPLETED);
        return Result.success(count);
    }

    @GetMapping("/getSales")
    public Result<List<SalesDTO>> getSales(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        List<SalesDTO> salesDTOList = orderService.getSales(dayBegin, dayEnd);
        return Result.success(salesDTOList);
    }

    @GetMapping("/countCONFIRMED")
    public Result<Integer> countCONFIRMED(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        Integer count = orderService.countByDateAndStatus(dayBegin, dayEnd, Orders.CONFIRMED);
        return Result.success(count);
    }

    @GetMapping("/countDELIVERY")
    public Result<Integer> countDELIVERY(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        Integer count = orderService.countByDateAndStatus(dayBegin, dayEnd, Orders.DELIVERY_IN_PROGRESS);
        return Result.success(count);
    }

    @GetMapping("/countCANCELLED")
    public Result<Integer> countCANCELLED(LocalDateTime dayBegin, LocalDateTime dayEnd) {
        Integer count = orderService.countByDateAndStatus(dayBegin, dayEnd, Orders.CANCELLED);
        return Result.success(count);
    }
}