package com.sky.category.controller.admin;

import com.sky.category.domain.dto.CategoryDTO;
import com.sky.category.domain.dto.CategoryPageQueryDTO;
import com.sky.category.domain.vo.CategoryVO;
import com.sky.category.service.CategoryService;
import com.sky.result.PageResult;
import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 * 提供分类的增删改查、启用/禁用等操作接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 修改分类信息
     *
     * @param categoryDTO 分类数据传输对象
     * @return Result<Void> 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询分类信息
     *
     * @param categoryQueryDTO 分类分页查询条件
     * @return Result<PageResult<Category>> 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<CategoryVO>> page(CategoryPageQueryDTO categoryQueryDTO) {
        log.info("分页查询：{}", categoryQueryDTO);
        PageResult<CategoryVO> pageResult = categoryService.page(categoryQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或禁用分类
     *
     * @param status 状态（1启用，0禁用）
     * @param id     分类ID
     * @return Result<Void> 操作结果
     */
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 新增分类
     *
     * @param categoryDTO 分类数据传输对象
     * @return Result<Void> 操作结果
     */
    @PostMapping
    public Result<Void> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 根据ID删除分类
     *
     * @param id 分类ID
     * @return Result<Void> 操作结果
     */
    @DeleteMapping
    public Result<Void> delete(Long id) {
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 查询分类列表
     *
     * @param type 分类类型
     * @return Result<List<Category>> 分类列表
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> list(Integer type) {
        log.info("查询分类：{}", type);
        List<CategoryVO> list = categoryService.list(type);
        return Result.success(list);
    }
}