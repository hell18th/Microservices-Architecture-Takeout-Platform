package com.sky.user.controller.user;

import com.sky.result.Result;
import com.sky.user.domain.dto.UserLoginDTO;
import com.sky.user.domain.vo.UserLoginVO;
import com.sky.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户端用户管理
 * 提供用户相关的接口
 */
@Slf4j
@RestController
@RequestMapping("/user/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * 处理用户登录请求，验证用户信息后返回登录结果
     *
     * @param userLoginDTO 用户登录数据传输对象，包含登录所需信息
     * @return Result<UserLoginVO> 响应结果，包含登录成功后的用户信息和token
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    @GetMapping("/userStatistics")
    public Result<Integer> userStatistics(LocalDateTime begin, LocalDateTime end) {
        Integer count = userService.userStatistics(begin, end);
        return Result.success(count);
    }
}