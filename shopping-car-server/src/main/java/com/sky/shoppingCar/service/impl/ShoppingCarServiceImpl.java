package com.sky.shoppingCar.service.impl;

import com.sky.api.client.DishClient;
import com.sky.api.client.SetMealClient;
import com.sky.api.dto.ApiDish;
import com.sky.api.dto.ApiSetMeal;
import com.sky.context.BaseContext;
import com.sky.shoppingCar.domain.dto.ShoppingCarDTO;
import com.sky.shoppingCar.domain.entity.ShoppingCar;
import com.sky.shoppingCar.mapper.ShoppingCarMapper;
import com.sky.shoppingCar.service.ShoppingCarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCarServiceImpl implements ShoppingCarService {
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private DishClient dishClient;

    @Autowired
    private SetMealClient setMealClient;

    @Override
    public void add(ShoppingCarDTO shoppingCarDTO) {
        ShoppingCar shoppingCar = new ShoppingCar();
        BeanUtils.copyProperties(shoppingCarDTO, shoppingCar);
        Long userId = BaseContext.getCurrentId();
        shoppingCar.setUserId(userId);
        List<ShoppingCar> list = shoppingCarMapper.list(shoppingCar);
        if (list != null && !list.isEmpty()) {
            ShoppingCar car = list.get(0);
            car.setNumber(car.getNumber() + 1);
            shoppingCarMapper.updateNumberById(car);
        } else {
            Long dishId = shoppingCarDTO.getDishId();
            if (dishId != null) {
                ApiDish dish = dishClient.getById(dishId);
                shoppingCar.setName(dish.getName());
                shoppingCar.setImage(dish.getImage());
                shoppingCar.setAmount(dish.getPrice());
            } else {
                Long setMealId = shoppingCar.getSetMealId();
                ApiSetMeal setMeal = setMealClient.getById(setMealId);
                shoppingCar.setName(setMeal.getName());
                shoppingCar.setImage(setMeal.getImage());
                shoppingCar.setAmount(setMeal.getPrice());
            }
            shoppingCar.setNumber(1);
            shoppingCar.setCreateTime(LocalDateTime.now());
            shoppingCarMapper.insert(shoppingCar);
        }
    }

    @Override
    public List<ShoppingCar> list() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCar shoppingCar = ShoppingCar.builder().userId(userId).build();
        return shoppingCarMapper.list(shoppingCar);
    }

    @Override
    public void delete() {
        Long userId = BaseContext.getCurrentId();
        shoppingCarMapper.deleteByUserId(userId);
    }

    @Override
    public void insertBatch(List<ShoppingCar> shoppingCarList) {
        shoppingCarMapper.insertBatch(shoppingCarList);
    }
}