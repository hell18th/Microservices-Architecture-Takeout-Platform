package com.sky.order.controller.user;

import com.sky.order.domain.dto.OrderPageQueryDTO;
import com.sky.order.domain.dto.OrdersSubmitDTO;
import com.sky.order.domain.vo.OrderSubmitVO;
import com.sky.order.domain.vo.OrderVO;
import com.sky.order.service.OrderService;
import com.sky.result.PageResult;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端订单管理
 * 处理用户端订单相关的请求
 */
@Slf4j
@RestController
@RequestMapping("user/order")
public class UserOrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单提交数据传输对象，包含订单详细信息
     * @return Result<OrderSubmitVO> 响应结果，包含订单提交成功后的视图对象
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 查询历史订单
     * 该方法用于查询用户的历史订单记录，支持分页查询
     *
     * @param orderPageQueryDTO 订单分页查询参数对象，包含查询条件和分页信息
     * @return Result<PageResult<OrderVO>> 包含订单分页结果的响应对象
     */
    @GetMapping("/history")
    public Result<PageResult<OrderVO>> history(@RequestBody OrderPageQueryDTO orderPageQueryDTO) {
        log.info("查询历史订单");
        PageResult<OrderVO> history = orderService.history(orderPageQueryDTO);
        return Result.success(history);
    }

    /**
     * 查询订单详情
     * 根据订单ID获取订单详细信息
     *
     * @param id 订单ID，路径参数
     * @return Result<OrderVO> 包含订单详情的响应结果
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.getOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 取消订单
     * 该方法用于处理用户取消订单的请求，通过订单ID标识需要取消的订单
     *
     * @param id 订单ID，用于标识需要取消的订单
     * @return Result<Void> 操作结果，成功时返回成功状态，失败时返回错误信息
     */
    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 再来一单
     * 根据订单ID重新创建一个新的订单
     *
     * @param id 订单ID，用于标识需要重新下单的原始订单
     * @return Result<Void> 操作结果，成功时返回成功状态
     */
    @PostMapping("/again/{id}")
    public Result<Void> again(@PathVariable Long id) {
        log.info("再来一单：{}", id);
        orderService.again(id);
        return Result.success();
    }

    /**
     * 客户催单
     * 用于处理客户的订单催促请求，通过订单ID触发催单逻辑
     *
     * @param id 订单ID，用于标识需要催单的具体订单
     * @return Result<Void> 操作结果，成功时返回成功状态，失败时返回错误信息
     */
    @GetMapping("/reminder/{id}")
    public Result<Void> reminder(@PathVariable Long id) {
        log.info("客户催单：{}", id);
        orderService.reminder(id);
        return Result.success();
    }
}