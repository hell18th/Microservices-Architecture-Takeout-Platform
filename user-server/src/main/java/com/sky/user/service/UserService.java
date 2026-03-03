package com.sky.user.service;

import com.sky.user.domain.dto.UserLoginDTO;
import com.sky.user.domain.vo.UserLoginVO;

import java.time.LocalDateTime;

public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);

    Integer userStatistics(LocalDateTime begin, LocalDateTime end);
}