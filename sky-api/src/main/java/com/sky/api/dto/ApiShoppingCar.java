package com.sky.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiShoppingCar {
    private Long id;
    private String name;
    private Long userId;
    private Long dishId;
    private Long setMealId;
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
    private String image;
    private LocalDateTime createTime;
}