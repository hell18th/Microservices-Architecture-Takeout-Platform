package com.sky.dish.controller.admin;

import com.sky.api.dto.DishCountDTO;
import com.sky.dish.domain.dto.DishDTO;
import com.sky.dish.domain.dto.DishPageQueryDTO;
import com.sky.dish.domain.vo.DishVO;
import com.sky.dish.service.DishService;
import com.sky.result.PageResult;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 新增菜品接口
     * 接收菜品DTO对象，保存菜品信息及其口味信息
     *
     * @param dishDTO 菜品数据传输对象，包含菜品基本信息和口味信息
     * @return Result<Void> 统一返回结果，操作成功返回成功状态
     */
    @PostMapping
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        String keyPattern = "category::" + dishDTO.getCategoryId();
        cleanCache(keyPattern);
        return Result.success();
    }

    /**
     * 分页查询菜品信息
     *
     * @param dishPageQueryDTO 菜品分页查询参数对象，包含分页条件和查询条件
     * @return Result<PageResult<DishVO>> 包含分页结果的响应对象，成功时返回菜品分页数据
     */
    @GetMapping("/page")
    public Result<PageResult<DishVO>> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询：{}", dishPageQueryDTO);
        PageResult<DishVO> pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品接口
     *
     * @param ids 要删除的菜品ID列表
     * @return Result<Void> 操作结果，删除成功返回成功状态
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.deleteBatch(ids);
        cleanCache("category::*");
        return Result.success();
    }

    /**
     * 根据ID查询菜品信息
     *
     * @param id 菜品ID
     * @return Result<DishVO> 包含菜品信息的响应结果
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据ID查询菜品信息：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     *
     * @param dishDTO 菜品数据传输对象，包含要修改的菜品信息
     * @return Result<Void> 操作结果，成功返回成功状态，失败返回错误信息
     */
    @PutMapping
    public Result<Void> update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息：{}", dishDTO);
        dishService.update(dishDTO);
        cleanCache("category::*");
        return Result.success();
    }

    /**
     * 启用或禁用菜品接口
     * 根据传入的状态值来启用或禁用指定的菜品
     *
     * @param status 菜品状态，用于控制菜品的启用或禁用
     * @param id     菜品ID，标识需要操作的具体菜品
     * @return Result<Void> 操作结果，成功时返回成功状态
     */
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用或禁用菜品：{}", id);
        dishService.startOrStop(status, id);
        cleanCache("category::*");
        return Result.success();
    }

    @GetMapping
    public DishCountDTO countByCategoryId(Long categoryId) {
        return dishService.countByCategoryId(categoryId);
    }

    @GetMapping("/countByStatus")
    public Integer countByStatus(Integer status) {
        return dishService.countByStatus(status);
    }

    private void cleanCache(String keyPattern) {
        Set<String> keys = redisTemplate.keys(keyPattern);
        redisTemplate.delete(keys);
    }
}