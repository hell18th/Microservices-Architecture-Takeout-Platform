package com.sky.shoppingCar.mapper;

import com.sky.shoppingCar.domain.entity.ShoppingCar;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCarMapper {
    List<ShoppingCar> list(ShoppingCar shoppingCar);

    @Update("update shopping_car set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCar car);

    @Insert("insert into shopping_car(name, image, user_id, dish_id, set_meal_id, dish_flavor, number, amount, create_time) values(#{name}, #{image}, #{userId}, #{dishId}, #{setMealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCar shoppingCar);

    @Delete("delete from shopping_car where user_id = #{userId}")
    void deleteByUserId(Long userId);

    void insertBatch(List<ShoppingCar> shoppingCarlist);
}