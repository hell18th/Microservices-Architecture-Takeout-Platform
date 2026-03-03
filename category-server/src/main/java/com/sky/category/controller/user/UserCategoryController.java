package com.sky.category.controller.user;

import com.sky.category.domain.vo.CategoryVO;
import com.sky.category.service.CategoryService;
import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类浏览
 */
@Slf4j
@RestController
@RequestMapping("/user/category")
public class UserCategoryController {
    @Autowired
    private CategoryService categoryService;

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