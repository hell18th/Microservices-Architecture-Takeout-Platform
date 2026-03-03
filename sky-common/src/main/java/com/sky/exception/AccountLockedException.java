package com.sky.exception;

/**
 * 账号被锁定自定义异常
 */
public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String message) {
        super(message);
    }
}