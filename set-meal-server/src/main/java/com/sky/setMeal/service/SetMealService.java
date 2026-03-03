package com.sky.setMeal.service;

import com.sky.api.dto.ApiSetMeal;
import com.sky.api.dto.SetMealCountDTO;
import com.sky.result.PageResult;
import com.sky.setMeal.domain.dto.SetMealDTO;
import com.sky.setMeal.domain.dto.SetMealPageQueryDTO;
import com.sky.setMeal.domain.entity.SetMeal;
import com.sky.setMeal.domain.vo.SetMealVO;

import java.util.List;

public interface SetMealService {
    void saveWithDish(SetMealDTO setMealDTO);

    PageResult<SetMealVO> page(SetMealPageQueryDTO setMealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetMealVO getByIdWithDish(Long id);

    void update(SetMealDTO setMealDTO);

    void startOrStop(Integer status, Long id);

    List<SetMeal> list(Long categoryId);

    SetMealCountDTO countByCategoryId(Long categoryId);

    ApiSetMeal getApiSetMealById(Long setMealId);

    Integer countByStatus(Integer status);
}