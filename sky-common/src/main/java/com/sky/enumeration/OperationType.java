package com.sky.enumeration;

/**
 * 操作类型枚举
 * 定义了数据操作的类型，用于标识当前执行的是更新操作还是插入操作
 */
public enum OperationType {
    /**
     * 更新操作类型
     */
    UPDATE,

    /**
     * 插入操作类型
     */
    INSERT
}