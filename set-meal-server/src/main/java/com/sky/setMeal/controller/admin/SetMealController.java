package com.sky.setMeal.controller.admin;

import com.sky.api.dto.SetMealCountDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.setMeal.domain.dto.SetMealDTO;
import com.sky.setMeal.domain.dto.SetMealPageQueryDTO;
import com.sky.setMeal.domain.vo.SetMealVO;
import com.sky.setMeal.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/SetMeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 保存套餐信息
     * 该方法接收套餐DTO对象，调用服务层保存套餐及其关联的菜品信息
     *
     * @param setMealDTO 套餐数据传输对象，包含套餐基本信息和关联的菜品信息
     * @return Result<Void> 操作结果，成功时返回成功状态，失败时返回错误信息
     */
    @PostMapping
    @CacheEvict(value = "setMealCache", key = "#setMealDTO.categoryId")
    public Result<Void> save(@RequestBody SetMealDTO setMealDTO) {
        setMealService.saveWithDish(setMealDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐信息
     * 该方法处理套餐分页查询请求，接收分页查询条件参数，调用服务层进行分页查询，
     * 并将查询结果封装到Result响应对象中返回
     *
     * @param setMealPageQueryDTO 套餐分页查询条件参数对象，包含分页信息和查询条件
     * @return Result<PageResult<SetMealVO>> 包含分页结果的响应对象，其中PageResult封装了分页数据和分页信息
     */
    @GetMapping("/page")
    public Result<PageResult<SetMealVO>> page(SetMealPageQueryDTO setMealPageQueryDTO) {
        PageResult<SetMealVO> pageResult = setMealService.page(setMealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐信息
     *
     * @param ids 要删除的套餐ID列表
     * @return Result<Void> 操作结果，删除成功返回成功状态
     */
    @DeleteMapping
    @CacheEvict(value = "setMealCache", allEntries = true)
    public Result<Void> delete(@RequestParam List<Long> ids) {
        setMealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据ID查询套餐信息
     *
     * @param id 套餐ID
     * @return Result<SetMealVO> 包含套餐信息的响应结果
     */
    @GetMapping("/{id}")
    public Result<SetMealVO> getById(@PathVariable Long id) {
        SetMealVO setMealVO = setMealService.getByIdWithDish(id);
        return Result.success(setMealVO);
    }

    /**
     * 更新套餐信息
     *
     * @param setMealDTO 套餐数据传输对象，包含要更新的套餐信息
     * @return Result<Void> 操作结果，成功时返回成功状态
     */
    @PutMapping
    @CacheEvict(value = "setMealCache", allEntries = true)
    public Result<Void> update(@RequestBody SetMealDTO setMealDTO) {
        setMealService.update(setMealDTO);
        return Result.success();
    }

    /**
     * 启动或停止套餐服务
     * 该方法通过状态参数控制指定ID的套餐服务的启动或停止操作
     *
     * @param status 状态参数，用于指定启动或停止操作（1-启动，0-停止）
     * @param id     套餐服务的唯一标识ID
     * @return Result<Void> 操作结果，成功时返回成功状态，失败时返回错误信息
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setMealCache", allEntries = true)
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        setMealService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping
    public SetMealCountDTO countByCategoryId(Long categoryId) {
        return setMealService.countByCategoryId(categoryId);
    }

    @GetMapping("/countByStatus")
    public Integer countByStatus(Integer status) {
        return setMealService.countByStatus(status);
    }
}