package com.sky.emp.service;

import com.sky.emp.domain.dto.EmpDTO;
import com.sky.emp.domain.dto.EmpLoginDTO;
import com.sky.emp.domain.dto.EmpPageQueryDTO;
import com.sky.emp.domain.entity.Emp;
import com.sky.emp.domain.vo.EmpLoginVO;
import com.sky.result.PageResult;

public interface EmpService {
    EmpLoginVO login(EmpLoginDTO empLoginDTO);

    void save(EmpDTO empDTO);

    PageResult<Emp> page(EmpPageQueryDTO empPageQueryDTO);

    void startOrStop(Integer status, Long id);

    Emp getById(Long id);

    void update(EmpDTO empDTO);
}