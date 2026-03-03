package com.sky.report.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayDataVO {
    private Integer newUsers;
    private Integer validOrderCount;
    private Double orderCompletionRate;
    private Double turnover;
    private Double unitPrice;
}