package com.sky.api.client;

import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@FeignClient("user-server")
public interface UserClient {
    @GetMapping("/user/user/userStatistics")
    Result<Integer> selectByDate(@RequestParam("begin") LocalDateTime begin, @RequestParam("end") LocalDateTime end);
}