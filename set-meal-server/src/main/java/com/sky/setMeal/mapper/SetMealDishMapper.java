package com.sky.setMeal.mapper;

import com.sky.setMeal.domain.entity.SetMealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    void insertBatch(List<SetMealDish> setMealDishes);

    void deleteBySetMealIds(List<Long> setMealIds);

    @Select("select * from set_meal_dish where set_meal_id = #{id}")
    List<SetMealDish> getBySetMealId(Long id);
}