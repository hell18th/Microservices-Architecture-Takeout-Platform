package com.sky.setMeal.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMealDish {
    private Long id;
    private Long setMealId;
    private Long dishId;
    private String name;
    private BigDecimal price;
    private Integer copies;
}