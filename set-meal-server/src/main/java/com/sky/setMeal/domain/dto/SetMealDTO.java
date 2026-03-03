package com.sky.setMeal.domain.dto;

import com.sky.setMeal.domain.entity.SetMealDish;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetMealDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String image;
    private String description;
    private Integer status;
    private List<SetMealDish> setMealDishes = new ArrayList<>();
}