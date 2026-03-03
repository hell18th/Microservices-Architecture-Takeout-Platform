package com.sky.report.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportVO {
    private String dateList;
    private String orderCountList;
    private String validOrderCountList;
    private Double orderCompleteRate;
    private Integer totalOrderCount;
    private Integer validOrderCount;
}