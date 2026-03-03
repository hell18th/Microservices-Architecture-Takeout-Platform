package com.sky.user.mapper;

import com.sky.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {
    User selectByOpenid(String openid);

    void insertUser(User user);

    Integer selectByDate(LocalDateTime begin, LocalDateTime end);
}