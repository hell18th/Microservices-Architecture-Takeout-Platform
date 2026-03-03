package com.sky.address.mapper;

import com.sky.address.domain.entity.Address;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressMapper {
    void insert(Address address);

    List<Address> list(Address address);

    void update(Address address);

    @Delete("delete from address where id = #{id}")
    void deleteById(Long id);

    @Select("select * from address where id = #{id}")
    Address getById(Long id);

    @Update("update address set is_default = 0 where user_id = #{userId}")
    void updateNotDefaultByUserId(Long userId);
}