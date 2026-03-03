package com.sky.shoppingCar.controller.user;

import com.sky.result.Result;
import com.sky.shoppingCar.domain.dto.ShoppingCarDTO;
import com.sky.shoppingCar.domain.entity.ShoppingCar;
import com.sky.shoppingCar.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端购物车
 */
@Slf4j
@RestController
@RequestMapping("/user/shoppingCar")
public class ShoppingCarController {
    @Autowired
    private ShoppingCarService shoppingCarService;

    /**
     * 添加商品到购物车
     *
     * @param shoppingCarDTO 购物车数据传输对象，包含要添加的商品信息
     * @return Result<Void> 操作结果，成功时返回成功状态
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody ShoppingCarDTO shoppingCarDTO) {
        log.info("添加购物车：{}", shoppingCarDTO);
        shoppingCarService.add(shoppingCarDTO);
        return Result.success();
    }

    /**
     * 查询购物车列表
     *
     * @return Result<List<ShoppingCar>> 返回购物车列表的响应结果
     * 包含购物车对象列表，每个购物车对象包含商品信息、数量等数据
     */
    @GetMapping("list")
    public Result<List<ShoppingCar>> list() {
        log.info("查询购物车");
        List<ShoppingCar> list = shoppingCarService.list();
        return Result.success(list);
    }

    /**
     * 清空购物车接口
     * 处理DELETE请求，用于清空当前用户的购物车
     *
     * @return Result<Void> 操作结果，成功时返回成功状态
     */
    @DeleteMapping("clean")
    public Result<Void> delete() {
        log.info("清空购物车");
        shoppingCarService.delete();
        return Result.success();
    }

    @PostMapping("/insertBatch")
    public Result<Void> insertBatch(@RequestBody List<ShoppingCar> shoppingCarList) {
        log.info("批量插入购物车");
        shoppingCarService.insertBatch(shoppingCarList);
        return Result.success();
    }
}