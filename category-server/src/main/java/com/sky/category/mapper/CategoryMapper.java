package com.sky.category.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.category.domain.dto.CategoryPageQueryDTO;
import com.sky.category.domain.entity.Category;
import com.sky.category.domain.vo.CategoryVO;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    Page<CategoryVO> page(CategoryPageQueryDTO categoryQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    void deleteById(Long id);

    List<Category> selectByType(Integer type);
}