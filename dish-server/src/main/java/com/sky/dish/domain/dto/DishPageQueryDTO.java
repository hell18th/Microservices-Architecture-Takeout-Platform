package com.sky.dish.domain.dto;

import lombok.Data;

@Data
public class DishPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer categoryId;
    private Integer status;
}