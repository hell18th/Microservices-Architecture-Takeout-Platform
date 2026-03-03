package com.sky.category.domain.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Integer sort;
    private Integer type;
}