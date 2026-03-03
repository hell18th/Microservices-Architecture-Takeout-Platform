package com.sky.address.controller.user;

import com.sky.address.domain.entity.Address;
import com.sky.address.service.AddressService;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端地址管理
 * 提供地址的增删改查以及默认地址设置等功能
 */
@Slf4j
@RestController
@RequestMapping("/user/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 新增地址
     *
     * @param address 地址信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Void> add(@RequestBody Address address) {
        log.info("新增地址：{}", address);
        addressService.add(address);
        return Result.success();
    }

    /**
     * 查询地址列表
     *
     * @return 地址列表
     */
    @GetMapping("/list")
    public Result<List<Address>> list() {
        log.info("查询地址列表");
        List<Address> list = addressService.list();
        return Result.success(list);
    }

    /**
     * 查询默认地址
     *
     * @return 默认地址信息
     */
    @GetMapping("/default")
    public Result<Address> getDefault() {
        log.info("查询默认地址");
        Address address = addressService.getDefault();
        return Result.success(address);
    }

    /**
     * 更新地址信息
     *
     * @param address 更新后的地址信息
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody Address address) {
        log.info("修改地址：{}", address);
        addressService.update(address);
        return Result.success();
    }

    /**
     * 根据ID删除地址
     *
     * @param id 地址ID
     * @return 操作结果
     */
    @DeleteMapping
    public Result<Void> deleteById(Long id) {
        log.info("删除地址：{}", id);
        addressService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据ID查询地址详情
     *
     * @param id 地址ID
     * @return 地址信息
     */
    @GetMapping("/{id}")
    public Result<Address> getById(@PathVariable Long id) {
        log.info("查询地址：{}", id);
        Address address = addressService.getById(id);
        return Result.success(address);
    }

    /**
     * 设置默认地址
     *
     * @param id 要设置为默认的地址ID
     * @return 操作结果
     */
    @PutMapping("/default")
    public Result<Void> setDefault(Long id) {
        log.info("设置默认地址：{}", id);
        addressService.setDefault(id);
        return Result.success();
    }
}