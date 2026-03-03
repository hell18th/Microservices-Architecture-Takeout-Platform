package com.sky.order.domain.dto;

import lombok.Data;

@Data
public class OrderRejectDTO {
    private Long id;
    private String rejectionReason;
}