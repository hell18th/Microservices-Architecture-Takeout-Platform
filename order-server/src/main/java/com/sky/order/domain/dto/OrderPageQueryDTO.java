package com.sky.order.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private Integer status;
    private Long userId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String number;
    private String phone;
}