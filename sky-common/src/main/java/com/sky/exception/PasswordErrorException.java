package com.sky.exception;

/**
 * 密码错误自定义异常
 */
public class PasswordErrorException extends RuntimeException {
    public PasswordErrorException(String message) {
        super(message);
    }
}