package com.sky.dish.domain.entity;

import lombok.Data;

@Data
public class DishFlavor {
    private Long id;
    private Long dishId;
    private String name;
    private String value;
}