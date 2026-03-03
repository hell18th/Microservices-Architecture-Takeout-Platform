package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理系统中抛出的异常，返回统一格式的错误响应
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理通用异常
     * 捕获系统中未被其他异常处理器处理的异常
     *
     * @param ex 异常对象
     * @return Result 包含错误信息的统一响应结果
     */
    @ExceptionHandler
    public Result<Void> exceptionHandler(Exception ex) {
        log.error("异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理数据库唯一键重复异常
     * 当数据库中存在重复的唯一键约束时抛出此异常
     *
     * @param e 数据库唯一键重复异常对象
     * @return Result 包含重复数据错误信息的统一响应结果
     */
    @ExceptionHandler
    public Result<Void> handle(DuplicateKeyException e) {
        String message = e.getMessage();
        log.error("异常：{}", message);
        if (message.contains("Duplicate entry")) {
            message = message.substring(message.indexOf("Duplicate entry"));
            String[] split = message.split(" ");
            String msg = split[2] + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }
}