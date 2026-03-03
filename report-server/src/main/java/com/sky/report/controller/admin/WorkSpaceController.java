package com.sky.report.controller.admin;

import com.sky.report.domain.vo.OverviewDishesVO;
import com.sky.report.domain.vo.OverviewOrdersVO;
import com.sky.report.domain.vo.OverviewSetMealVO;
import com.sky.report.domain.vo.TodayDataVO;
import com.sky.report.service.WorkSpaceService;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作台
 * 包括今日数据概览、订单统计、菜品统计、套餐统计
 */
@Slf4j
@RestController
@RequestMapping("/admin/workspace")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;

    /**
     * 获取今日数据统计信息
     *
     * @return 今日数据统计结果
     */
    @GetMapping("/TodayData")
    public Result<TodayDataVO> getTodayData() {
        log.info("获取今日数据");
        TodayDataVO todayDataVO = workSpaceService.getTodayData(null, null);
        return Result.success(todayDataVO);
    }

    /**
     * 获取订单统计数据
     *
     * @return 订单统计结果
     */
    @GetMapping("/overviewOrders")
    public Result<OverviewOrdersVO> overviewOrders() {
        log.info("获取订单统计数据");
        OverviewOrdersVO overviewOrders = workSpaceService.overviewOrders();
        return Result.success(overviewOrders);
    }

    /**
     * 获取菜品统计数据
     *
     * @return 菜品统计结果
     */
    @GetMapping("/overviewDishes")
    public Result<OverviewDishesVO> overviewDishes() {
        log.info("获取菜品统计数据");
        OverviewDishesVO overviewDishes = workSpaceService.overviewDishes();
        return Result.success(overviewDishes);
    }

    /**
     * 获取套餐统计数据
     *
     * @return 套餐统计结果
     */
    @GetMapping("/overviewSetMeal")
    public Result<OverviewSetMealVO> overviewSetMeal() {
        log.info("获取套餐统计数据");
        OverviewSetMealVO overviewSetMealVO = workSpaceService.overviewSetMeal();
        return Result.success(overviewSetMealVO);
    }
}