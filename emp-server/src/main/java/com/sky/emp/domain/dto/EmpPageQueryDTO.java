package com.sky.emp.domain.dto;

import lombok.Data;

@Data
public class EmpPageQueryDTO {
    private Integer page;
    private Integer pageSize;
    private String name;
}