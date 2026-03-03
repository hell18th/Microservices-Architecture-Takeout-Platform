package com.sky.category.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.api.client.DishClient;
import com.sky.api.client.SetMealClient;
import com.sky.api.dto.DishCountDTO;
import com.sky.api.dto.SetMealCountDTO;
import com.sky.category.domain.dto.CategoryDTO;
import com.sky.category.domain.dto.CategoryPageQueryDTO;
import com.sky.category.domain.entity.Category;
import com.sky.category.domain.vo.CategoryVO;
import com.sky.category.mapper.CategoryMapper;
import com.sky.category.service.CategoryService;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.exception.DeleteNotAllowedException;
import com.sky.result.PageResult;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishClient dishClient;
    @Autowired
    private SetMealClient setMealClient;

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    @Override
    public PageResult<CategoryVO> page(CategoryPageQueryDTO categoryQueryDTO) {
        PageHelper.startPage(categoryQueryDTO.getPage(), categoryQueryDTO.getPageSize());
        Page<CategoryVO> page = categoryMapper.page(categoryQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder().status(status).id(id).build();
        categoryMapper.update(category);
    }

    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.insert(category);
    }

    @Override
    public void delete(Long id) {
        // 判断当前分类是否关联了菜品
        DishCountDTO dishCountDTO = dishClient.countByCategoryId(id);
        if (dishCountDTO.getCountDish() > 0) {
            throw new DeleteNotAllowedException(MessageConstant.CATEGORY_HAS_BEEN_RELATED_BY_DISH);
        }
        // 判断当前分类是否关联了套餐
        SetMealCountDTO setMealCountDTO = setMealClient.countByCategoryId(id);
        if (setMealCountDTO.getCountSetMeal() > 0) {
            throw new DeleteNotAllowedException(MessageConstant.CATEGORY_HAS_BEEN_RELATED_BY_SET_MEAL);
        }
        categoryMapper.deleteById(id);
    }

    @Override
    public List<CategoryVO> list(Integer type) {
        List<Category> categories = categoryMapper.selectByType(type);
        return categories.stream().map(category -> {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            return categoryVO;
        }).toList();
    }
}