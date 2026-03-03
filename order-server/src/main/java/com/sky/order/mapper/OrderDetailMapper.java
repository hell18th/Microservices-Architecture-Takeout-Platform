package com.sky.order.mapper;

import com.sky.api.dto.SalesDTO;
import com.sky.order.domain.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void insertBatch(List<OrderDetail> orderDetails);

    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long id);

    List<SalesDTO> getSales(LocalDateTime dayBegin, LocalDateTime dayEnd, Integer status);
}