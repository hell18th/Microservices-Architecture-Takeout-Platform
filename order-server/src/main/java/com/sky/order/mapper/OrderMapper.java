package com.sky.order.mapper;

import com.github.pagehelper.Page;
import com.sky.order.domain.dto.OrderPageQueryDTO;
import com.sky.order.domain.entity.Orders;
import com.sky.order.domain.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);

    Page<OrderVO> page(OrderPageQueryDTO orderPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    void update(Orders orders);

    @Select("select count(*) from orders where status = #{status}")
    Integer selectNumberByStatus(Integer toBeConfirmed);

    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> selectByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    Double selectTurnoverByDate(LocalDateTime dayBegin, LocalDateTime dayEnd, Integer status);

    Integer countByDateAndStatus(LocalDateTime dayBegin, LocalDateTime dayEnd, Integer status);
}