package com.sky.setMeal.domain.dto;

import lombok.Data;

@Data
public class SetMealPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer categoryId;
    private Integer status;
}