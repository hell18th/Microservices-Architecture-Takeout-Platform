package com.sky.shop.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理
 * 通过Redis存储和获取店铺的营业状态
 */
@Slf4j
@RestController
@RequestMapping("/admin/shop")
public class ShopController {
    /**
     * Redis中存储店铺状态的键名
     */
    private static final String KEY = "SHOP_STATUS";

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置店铺营业状态
     *
     * @param status 状态值，1表示营业中，0表示打烊中
     * @return 操作结果
     */
    @PostMapping("/{status}")
    public Result<Void> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * 查询店铺营业状态
     *
     * @return 店铺当前营业状态
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        log.info("查询店铺营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("店铺营业状态：{}", status != null && status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}