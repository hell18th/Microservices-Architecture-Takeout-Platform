package com.sky.emp.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.emp.domain.dto.EmpPageQueryDTO;
import com.sky.emp.domain.entity.Emp;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpMapper {
    Emp selectByUsername(String username);

    @AutoFill(value = OperationType.INSERT)
    void insertEmp(Emp emp);

    Page<Emp> page(EmpPageQueryDTO empPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Emp emp);

    Emp selectById(Long id);
}