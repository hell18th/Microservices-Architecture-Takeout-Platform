package com.sky.emp.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.emp.domain.dto.EmpDTO;
import com.sky.emp.domain.dto.EmpLoginDTO;
import com.sky.emp.domain.dto.EmpPageQueryDTO;
import com.sky.emp.domain.entity.Emp;
import com.sky.emp.domain.vo.EmpLoginVO;
import com.sky.emp.mapper.EmpMapper;
import com.sky.emp.service.EmpService;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.result.PageResult;
import com.sky.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public EmpLoginVO login(EmpLoginDTO empLoginDTO) {
        String username = empLoginDTO.getUsername();
        String password = empLoginDTO.getPassword();
        Emp emp = empMapper.selectByUsername(username);
        if (emp == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!emp.getPassword().equals(md5)) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        if (emp.getStatus().equals(StatusConstant.DISABLE)) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, emp.getId());
        claims.put(JwtClaimsConstant.EMP_USERNAME, emp.getUsername());
        String token = JwtUtils.generateJwt(claims);
        return new EmpLoginVO(emp.getId(), emp.getUsername(), token);
    }

    @Override
    public void save(EmpDTO empDTO) {
        Emp emp = new Emp();
        BeanUtils.copyProperties(empDTO, emp);
        emp.setStatus(StatusConstant.ENABLE);
        String md5 = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        emp.setPassword(md5);
        empMapper.insertEmp(emp);
    }

    @Override
    public PageResult<Emp> page(EmpPageQueryDTO empPageQueryDTO) {
        PageHelper.startPage(empPageQueryDTO.getPage(), empPageQueryDTO.getPageSize());
        Page<Emp> page = empMapper.page(empPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Emp emp = Emp.builder().status(status).id(id).build();
        empMapper.update(emp);
    }

    @Override
    public Emp getById(Long id) {
        Emp emp = empMapper.selectById(id);
        // 隐藏密码信息
        emp.setPassword("****");
        return emp;
    }

    @Override
    public void update(EmpDTO empDTO) {
        Emp emp = new Emp();
        BeanUtils.copyProperties(empDTO, emp);
        empMapper.update(emp);
    }
}