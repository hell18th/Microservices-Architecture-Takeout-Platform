package com.sky.category.service;

import com.sky.category.domain.dto.CategoryDTO;
import com.sky.category.domain.dto.CategoryPageQueryDTO;
import com.sky.category.domain.vo.CategoryVO;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    void update(CategoryDTO categoryDTO);

    PageResult<CategoryVO> page(CategoryPageQueryDTO categoryQueryDTO);

    void startOrStop(Integer status, Long id);

    void save(CategoryDTO categoryDTO);

    void delete(Long id);

    List<CategoryVO> list(Integer type);
}