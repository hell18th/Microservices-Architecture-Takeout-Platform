package com.sky.shoppingCar.service;

import com.sky.shoppingCar.domain.dto.ShoppingCarDTO;
import com.sky.shoppingCar.domain.entity.ShoppingCar;

import java.util.List;

public interface ShoppingCarService {
    void add(ShoppingCarDTO shoppingCarDTO);

    List<ShoppingCar> list();

    void delete();

    void insertBatch(List<ShoppingCar> shoppingCarList);
}