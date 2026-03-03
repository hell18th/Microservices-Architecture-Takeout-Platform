package com.sky.report.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverviewOrdersVO {
    private Integer waitingOrders;
    private Integer deliveredOrders;
    private Integer completedOrders;
    private Integer canceledOrders;
    private Integer allOrders;
}