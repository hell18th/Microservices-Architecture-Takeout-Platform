package com.sky.setMeal.controller.user;

import com.sky.api.dto.ApiSetMeal;
import com.sky.result.Result;
import com.sky.setMeal.domain.entity.SetMeal;
import com.sky.setMeal.domain.entity.SetMealDish;
import com.sky.setMeal.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐浏览
 */
@Slf4j
@RestController
@RequestMapping("/user/setMeal")
public class UserSetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 查询套餐列表
     * 根据分类ID查询对应的套餐列表信息
     *
     * @param categoryId 套餐分类ID，用于筛选指定分类下的套餐
     * @return Result<List<SetMeal>> 返回查询结果，包含套餐列表数据的响应对象
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "setMealCache", key = "#categoryId")
    public Result<List<SetMeal>> list(Long categoryId) {
        List<SetMeal> list = setMealService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 根据套餐ID获取套餐中的菜品列表
     *
     * @param id 套餐ID
     * @return 包含套餐菜品列表的成功响应结果
     */
    @GetMapping("/dish/{id}")
    public Result<List<SetMealDish>> getDishesBySetMealId(@PathVariable Long id) {
        List<SetMealDish> list = setMealService.getByIdWithDish(id).getSetMealDishes();
        return Result.success(list);
    }

    @GetMapping
    ApiSetMeal getApiSetMealById(@RequestParam("setMealId") Long setMealId) {
        return setMealService.getApiSetMealById(setMealId);
    }
}