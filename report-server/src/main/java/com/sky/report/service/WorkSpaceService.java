package com.sky.report.service;

import com.sky.report.domain.vo.OverviewDishesVO;
import com.sky.report.domain.vo.OverviewOrdersVO;
import com.sky.report.domain.vo.OverviewSetMealVO;
import com.sky.report.domain.vo.TodayDataVO;

import java.time.LocalDate;

public interface WorkSpaceService {
    TodayDataVO getTodayData(LocalDate begin, LocalDate end);

    OverviewOrdersVO overviewOrders();

    OverviewDishesVO overviewDishes();

    OverviewSetMealVO overviewSetMeal();
}