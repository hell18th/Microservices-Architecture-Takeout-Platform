package com.sky.setMeal.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.enumeration.OperationType;
import com.sky.setMeal.domain.dto.SetMealPageQueryDTO;
import com.sky.setMeal.domain.entity.SetMeal;
import com.sky.setMeal.domain.vo.SetMealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {
    @Select("select count(*) from set_meal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(SetMeal setMeal);

    Page<SetMealVO> page(SetMealPageQueryDTO setMealPageQueryDTO);

    @Select("select * from set_meal where id = #{id}")
    SetMeal getById(Long id);

    void deleteBatch(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(SetMeal setMeal);

    @Select("select * from set_meal where category_id = #{categoryId} and status = #{status}")
    List<SetMeal> list(Long categoryId, Integer status);

    @Select("select count(*) from set_meal where status = #{status}")
    Integer countByStatus(Integer status);
}