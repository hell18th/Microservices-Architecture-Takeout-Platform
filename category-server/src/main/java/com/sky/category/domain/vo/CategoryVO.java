package com.sky.category.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
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