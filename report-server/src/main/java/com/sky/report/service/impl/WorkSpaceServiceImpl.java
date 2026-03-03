package com.sky.report.service.impl;

import com.sky.api.client.DishClient;
import com.sky.api.client.OrderClient;
import com.sky.api.client.SetMealClient;
import com.sky.api.client.UserClient;
import com.sky.constant.StatusConstant;
import com.sky.report.domain.vo.OverviewDishesVO;
import com.sky.report.domain.vo.OverviewOrdersVO;
import com.sky.report.domain.vo.OverviewSetMealVO;
import com.sky.report.domain.vo.TodayDataVO;
import com.sky.report.service.WorkSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private DishClient dishClient;
    @Autowired
    private SetMealClient setMealClient;

    @Override
    public TodayDataVO getTodayData(LocalDate dayBegin, LocalDate dyaEnd) {
        LocalDateTime begin;
        LocalDateTime end;
        if (dayBegin == null || dyaEnd == null) {
            begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        } else {
            begin = LocalDateTime.of(dayBegin, LocalTime.MIN);
            end = LocalDateTime.of(dyaEnd, LocalTime.MAX);
        }
        Integer newUsers = userClient.selectByDate(begin, end).getData();
        Integer validOrderCount = orderClient.countCOMPLETED(begin, end).getData();
        Integer totalOrderCount = orderClient.countAll(begin, end).getData();
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount * 100;
        Double turnover = orderClient.getTurnover(begin, end).getData();
        if (turnover == null) turnover = 0.0;
        Double unitPrice = validOrderCount == 0 ? 0.0 : turnover / validOrderCount;
        return new TodayDataVO(newUsers, validOrderCount, orderCompletionRate, turnover, unitPrice);
    }

    @Override
    public OverviewOrdersVO overviewOrders() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        Integer waitingOrders = orderClient.countCONFIRMED(begin, end).getData();
        Integer deliveredOrders = orderClient.countDELIVERY(begin, end).getData();
        Integer completedOrders = orderClient.countCOMPLETED(begin, end).getData();
        Integer canceledOrders = orderClient.countCANCELLED(begin, end).getData();
        Integer allOrders = orderClient.countAll(begin, end).getData();
        return new OverviewOrdersVO(waitingOrders, deliveredOrders, completedOrders, canceledOrders, allOrders);
    }

    @Override
    public OverviewDishesVO overviewDishes() {
        Integer discontinued = dishClient.countByStatus(StatusConstant.DISABLE);
        Integer sold = dishClient.countByStatus(StatusConstant.ENABLE);
        return new OverviewDishesVO(discontinued, sold);
    }

    @Override
    public OverviewSetMealVO overviewSetMeal() {
        Integer discontinued = setMealClient.countByStatus(StatusConstant.DISABLE);
        Integer sold = setMealClient.countByStatus(StatusConstant.ENABLE);
        return new OverviewSetMealVO(discontinued, sold);
    }
}