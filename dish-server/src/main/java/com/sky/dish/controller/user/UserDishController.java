package com.sky.dish.controller.user;

import com.sky.api.dto.ApiDish;
import com.sky.dish.domain.vo.DishVO;
import com.sky.dish.service.DishService;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜品浏览
 */
@Slf4j
@RestController
@RequestMapping("/user/dish")
public class UserDishController {
    @Autowired
    private DishService dishService;

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据分类id查询菜品列表
     *
     * @param categoryId 菜品分类id
     * @return 菜品列表数据
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        String key = "category::" + categoryId;
//        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        Object objectData = redisTemplate.opsForValue().get(key);
        List<DishVO> list = new ArrayList<>();
        if (objectData instanceof List<?> listData) {
            for (Object dish : listData) {
                if (dish instanceof DishVO dishVO) {
                    list.add(dishVO);
                }
            }
        }
        if (!list.isEmpty()) {
            return Result.success(list);
        }
        list = dishService.list(categoryId);
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }

    @GetMapping
    ApiDish getApiDishById(@RequestParam("dishId") Long dishId) {
        return dishService.getApiDishById(dishId);
    }
}