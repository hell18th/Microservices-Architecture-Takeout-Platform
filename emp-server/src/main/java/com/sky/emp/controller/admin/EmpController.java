package com.sky.emp.controller.admin;

import com.sky.emp.domain.dto.EmpDTO;
import com.sky.emp.domain.dto.EmpLoginDTO;
import com.sky.emp.domain.dto.EmpPageQueryDTO;
import com.sky.emp.domain.entity.Emp;
import com.sky.emp.domain.vo.EmpLoginVO;
import com.sky.emp.service.EmpService;
import com.sky.result.PageResult;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 * 提供员工登录、员工信息管理、分页查询、状态管理等功能
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmpController {
    @Autowired
    private EmpService empService;

    /**
     * 员工登录接口
     * 处理员工登录请求，验证登录信息并返回登录结果
     *
     * @param empLoginDTO 员工登录数据传输对象，包含登录凭证信息
     * @return Result<EmpLoginVO> 包含员工登录视图对象的响应结果
     */
    @PostMapping("/login")
    public Result<EmpLoginVO> login(@RequestBody EmpLoginDTO empLoginDTO) {
        log.info("员工登录：{}", empLoginDTO);
        EmpLoginVO empLoginVO = empService.login(empLoginDTO);
        return Result.success(empLoginVO);
    }


    /**
     * 新增员工
     *
     * @param empDTO 员工信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Void> save(@RequestBody EmpDTO empDTO) {
        log.info("员工保存：{}", empDTO);
        empService.save(empDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     *
     * @param empPageQueryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<PageResult<Emp>> page(EmpPageQueryDTO empPageQueryDTO) {
        log.info("员工分页查询：{}", empPageQueryDTO);
        PageResult<Emp> pageResult = empService.page(empPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用或禁用员工账号
     *
     * @param status 状态(1启用，0禁用)
     * @param id     员工ID（路径变量）
     * @return 操作结果
     */
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable Integer status, Long id) {
        log.info("员工状态：{}，员工id：{}", status, id);
        empService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     * @return 员工信息
     */
    @GetMapping("/{id}")
    public Result<Emp> getById(@PathVariable Long id) {
        log.info("员工id：{}", id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    /**
     * 修改员工信息
     *
     * @param empDTO 员工信息
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> update(@RequestBody EmpDTO empDTO) {
        log.info("员工修改：{}", empDTO);
        empService.update(empDTO);
        return Result.success();
    }
}