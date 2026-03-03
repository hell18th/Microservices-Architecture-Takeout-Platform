package com.sky.dish.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.api.dto.ApiDish;
import com.sky.api.dto.DishCountDTO;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dish.domain.dto.DishDTO;
import com.sky.dish.domain.dto.DishPageQueryDTO;
import com.sky.dish.domain.entity.Dish;
import com.sky.dish.domain.entity.DishFlavor;
import com.sky.dish.domain.vo.DishVO;
import com.sky.dish.mapper.DishFlavorMapper;
import com.sky.dish.mapper.DishMapper;
import com.sky.dish.service.DishService;
import com.sky.exception.DeleteNotAllowedException;
import com.sky.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE);
        dishMapper.insert(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult<DishVO> page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            if (dishMapper.getById(id).getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeleteNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        dishMapper.deleteBatch(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        dishFlavorMapper.deleteByDishIds(List.of(dish.getId()));
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public List<DishVO> list(Long categoryId) {
        List<Dish> dishes = dishMapper.list(categoryId, StatusConstant.ENABLE);
        List<DishVO> list = new ArrayList<>();
        for (Dish dish : dishes) {
            list.add(getByIdWithFlavor(dish.getId()));
        }
        return list;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder().id(id).status(status).build();
        dishMapper.update(dish);
    }

    @Override
    public DishCountDTO countByCategoryId(Long categoryId) {
        Integer countDish = dishMapper.countByCategoryId(categoryId);
        DishCountDTO dishCountDTO = new DishCountDTO();
        dishCountDTO.setCountDish(countDish);
        return dishCountDTO;
    }

    @Override
    public ApiDish getApiDishById(Long dishId) {
        Dish dish = dishMapper.getById(dishId);
        ApiDish apiDish = new ApiDish();
        BeanUtils.copyProperties(dish, apiDish);
        return apiDish;
    }

    @Override
    public Integer countByStatus(Integer status) {
        return dishMapper.countByStatus(status);
    }
}