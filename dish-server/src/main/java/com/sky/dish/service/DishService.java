package com.sky.dish.service;

import com.sky.api.dto.ApiDish;
import com.sky.api.dto.DishCountDTO;
import com.sky.dish.domain.dto.DishDTO;
import com.sky.dish.domain.dto.DishPageQueryDTO;
import com.sky.dish.domain.vo.DishVO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void update(DishDTO dishDTO);

    List<DishVO> list(Long categoryId);

    void startOrStop(Integer status, Long id);

    DishCountDTO countByCategoryId(Long categoryId);

    ApiDish getApiDishById(Long dishId);

    Integer countByStatus(Integer status);
}