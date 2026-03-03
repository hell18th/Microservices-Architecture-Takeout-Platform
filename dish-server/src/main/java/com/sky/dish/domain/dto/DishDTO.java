package com.sky.dish.domain.dto;

import com.sky.dish.domain.entity.DishFlavor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String image;
    private String description;
    private Integer status;
    private List<DishFlavor> flavors = new ArrayList<>();
}