package com.sky.api.client;

import com.sky.api.dto.ApiSetMeal;
import com.sky.api.dto.SetMealCountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("set-meal-server")
public interface SetMealClient {
    @GetMapping("admin/SetMeal")
    SetMealCountDTO countByCategoryId(@RequestParam("categoryId") Long categoryId);

    @GetMapping("user/setMeal")
    ApiSetMeal getById(@RequestParam("setMealId") Long setMealId);

    @GetMapping("admin/SetMeal/countByStatus")
    Integer countByStatus(@RequestParam("status") Integer status);
}