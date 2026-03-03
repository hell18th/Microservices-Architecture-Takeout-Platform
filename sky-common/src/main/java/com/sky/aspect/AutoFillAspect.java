package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现自动填充功能
 * 用于在数据插入和更新时自动填充创建时间、更新时间、创建人、更新人等字段
 * 根据@AutoFill注解中指定的操作类型来决定填充哪些字段
 */
@Slf4j
@Aspect
@Component
public class AutoFillAspect {

    /**
     * 切点：匹配所有在com.sky.mapper包下且带有@AutoFill注解的方法
     */
    @Pointcut("execution(* com.sky.*.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {
    }

    /**
     * 前置通知：在目标方法执行前进行自动填充操作
     * 根据@AutoFill注解的操作类型（INSERT/UPDATE）自动设置对应的时间和用户信息字段
     * INSERT操作：填充创建时间、更新时间、创建人、更新人
     * UPDATE操作：仅填充更新时间、更新人
     *
     * @param joinPoint 连接点，提供目标方法执行时的相关信息
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行自动填充");

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 获取操作类型（INSERT或UPDATE）
        OperationType operationType = signature.getMethod().getAnnotation(AutoFill.class).value();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        // 获取要操作的实体对象
        Object entity = args[0];

        // 获取当前时间戳和当前用户ID
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 根据操作类型进行不同的自动填充
        if (operationType == OperationType.INSERT) {
            // 插入操作时填充创建时间和更新时间、创建人和更新人
            try {
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用setter方法设置值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("INSERT自动填充失败：{}", e.getMessage());
            }
        } else if (operationType == OperationType.UPDATE) {
            // 更新操作时仅填充更新时间和更新人
            try {
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用setter方法设置值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("UPDATE自动填充失败：{}", e.getMessage());
            }
        }

        log.info("结束进行自动填充");
    }
}