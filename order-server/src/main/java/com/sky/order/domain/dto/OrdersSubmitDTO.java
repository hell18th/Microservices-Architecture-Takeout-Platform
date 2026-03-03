package com.sky.order.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO {
    private Long addressId;//地址id
    private Integer payMethod;//付款方式
    private BigDecimal amount;//总金
    private String remark;//备注
    private LocalDateTime estimatedDeliveryTime;//预计送达时间
    private Integer deliveryStatus;//配送状态 1立即送出 0选择具体时间
    private Integer tablewareNumber;//餐具数量
    private Integer tablewareStatus;//餐具数量状态 1按套餐提供 0选择具体数量
    private Integer packAmount;//打包费
}