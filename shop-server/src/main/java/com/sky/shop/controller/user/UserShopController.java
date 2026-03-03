package com.sky.shop.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端店铺管理
 * 提供用户查询店铺营业状态的接口
 */
@Slf4j
@RestController
@RequestMapping("/user/shop")
public class UserShopController {
    /**
     * Redis中存储店铺状态的键名
     */
    private static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取店铺营业状态接口
     *
     * @return Result 包含店铺状态信息的成功响应
     * 状态值：1表示营业中，0表示打烊中
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        log.info("查询店铺营业状态");
        // 从Redis中获取店铺状态信息
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("店铺营业状态：{}", status != null && status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}