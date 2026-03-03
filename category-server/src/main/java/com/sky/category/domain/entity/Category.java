package com.sky.category.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    Long id;
    String name;
    Integer sort;
    Integer type;
    Integer status;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    Long createUser;
    Long updateUser;
}