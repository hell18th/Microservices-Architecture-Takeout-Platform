package com.sky.api.client;

import com.sky.api.dto.ApiDish;
import com.sky.api.dto.DishCountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("dish-server")
public interface DishClient {
    @GetMapping("admin/dish")
    DishCountDTO countByCategoryId(@RequestParam("categoryId") Long categoryId);

    @GetMapping("user/dish")
    ApiDish getById(@RequestParam("dishId") Long dishId);

    @GetMapping("admin/dish/countByStatus")
    Integer countByStatus(@RequestParam("status") Integer status);
}