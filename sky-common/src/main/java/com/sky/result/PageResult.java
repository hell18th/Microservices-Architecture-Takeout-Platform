package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果类，用于封装分页查询的返回结果
 * 包含总记录数和当前页的数据列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页的数据记录列表
     */
    private List<T> records;
}