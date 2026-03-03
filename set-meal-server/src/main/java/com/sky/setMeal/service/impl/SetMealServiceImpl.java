package com.sky.setMeal.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.api.dto.ApiSetMeal;
import com.sky.api.dto.SetMealCountDTO;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.exception.DeleteNotAllowedException;
import com.sky.result.PageResult;
import com.sky.setMeal.domain.dto.SetMealDTO;
import com.sky.setMeal.domain.dto.SetMealPageQueryDTO;
import com.sky.setMeal.domain.entity.SetMeal;
import com.sky.setMeal.domain.entity.SetMealDish;
import com.sky.setMeal.domain.vo.SetMealVO;
import com.sky.setMeal.mapper.SetMealDishMapper;
import com.sky.setMeal.mapper.SetMealMapper;
import com.sky.setMeal.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    @Transactional
    public void saveWithDish(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        setMeal.setStatus(StatusConstant.DISABLE);
        //向套餐表插入数据
        setMealMapper.insert(setMeal);
        //获取生成的套餐id
        Long setMealId = setMeal.getId();
        List<SetMealDish> setMealDishes = setMealDTO.getSetMealDishes();
        setMealDishes.forEach(setMealDish -> setMealDish.setSetMealId(setMealId));
        //保存套餐和菜品的关联关系
        setMealDishMapper.insertBatch(setMealDishes);
    }

    @Override
    public PageResult<SetMealVO> page(SetMealPageQueryDTO setMealPageQueryDTO) {
        PageHelper.startPage(setMealPageQueryDTO.getPage(), setMealPageQueryDTO.getPageSize());
        Page<SetMealVO> page = setMealMapper.page(setMealPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            if (setMealMapper.getById(id).getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeleteNotAllowedException(MessageConstant.SET_MEAL_ON_SALE);
            }
        }
        setMealMapper.deleteBatch(ids);
        setMealDishMapper.deleteBySetMealIds(ids);
    }

    @Override
    public SetMealVO getByIdWithDish(Long id) {
        SetMeal setMeal = setMealMapper.getById(id);
        List<SetMealDish> setMealDishes = setMealDishMapper.getBySetMealId(id);
        SetMealVO setMealVO = new SetMealVO();
        BeanUtils.copyProperties(setMeal, setMealVO);
        setMealVO.setSetMealDishes(setMealDishes);
        return setMealVO;
    }

    @Override
    @Transactional
    public void update(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        setMealMapper.update(setMeal);
        setMealDishMapper.deleteBySetMealIds(List.of(setMeal.getId()));
        List<SetMealDish> setMealDishes = setMealDTO.getSetMealDishes();
        if (setMealDishes != null && !setMealDishes.isEmpty()) {
            setMealDishes.forEach(setMealDish -> setMealDish.setSetMealId(setMeal.getId()));
            setMealDishMapper.insertBatch(setMealDishes);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        SetMeal setMeal = SetMeal.builder().status(status).id(id).build();
        setMealMapper.update(setMeal);
    }

    @Override
    public List<SetMeal> list(Long categoryId) {
        return setMealMapper.list(categoryId, StatusConstant.ENABLE);
    }

    @Override
    public SetMealCountDTO countByCategoryId(Long categoryId) {
        SetMealCountDTO setMealCountDTO = new SetMealCountDTO();
        setMealCountDTO.setCountSetMeal(setMealMapper.countByCategoryId(categoryId));
        return setMealCountDTO;
    }

    @Override
    public ApiSetMeal getApiSetMealById(Long setMealId) {
        SetMeal setMeal = setMealMapper.getById(setMealId);
        ApiSetMeal apiSetMeal = new ApiSetMeal();
        BeanUtils.copyProperties(setMeal, apiSetMeal);
        return apiSetMeal;
    }

    @Override
    public Integer countByStatus(Integer status) {
        return setMealMapper.countByStatus(status);
    }
}