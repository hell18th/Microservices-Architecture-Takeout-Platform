package com.sky.exception;

/**
 * 登录失败自定义异常
 */
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }
}