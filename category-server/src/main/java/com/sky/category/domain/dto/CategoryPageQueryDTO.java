package com.sky.category.domain.dto;

import lombok.Data;

@Data
public class CategoryPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
    private Integer type;
}