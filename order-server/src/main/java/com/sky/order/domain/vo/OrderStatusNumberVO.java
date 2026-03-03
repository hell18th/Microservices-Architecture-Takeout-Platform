package com.sky.order.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusNumberVO {
    private Integer toBeConfirmed;//待接单
    private Integer confirmed;//待派送
    private Integer deliveryInProgress;//派送中
}