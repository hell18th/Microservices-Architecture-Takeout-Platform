package com.sky.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private Long id;
    private String name;
    private String image;
    private Long orderId;
    private Long dishId;
    private Long setMealId;
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
}