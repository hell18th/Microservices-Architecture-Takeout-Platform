package com.sky.dish.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dish.domain.dto.DishPageQueryDTO;
import com.sky.dish.domain.entity.Dish;
import com.sky.dish.domain.vo.DishVO;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    @Select("select count(*) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    void deleteBatch(List<Long> ids);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from dish where category_id = #{categoryId} and status = #{status}")
    List<Dish> list(Long categoryId, Integer status);

    @Select("select count(*) from dish where status = #{status}")
    Integer countByStatus(Integer status);
}