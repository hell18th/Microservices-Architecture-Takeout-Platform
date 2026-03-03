package com.sky.exception;

/**
 * 账号不存在自定义异常
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}