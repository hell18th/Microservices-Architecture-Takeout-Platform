package com.sky.context;

/**
 * 基础上下文工具类，用于在当前线程中存储和获取用户ID
 * 使用ThreadLocal实现线程隔离，确保在多线程环境下数据安全
 */
public class BaseContext {
    /**
     * ThreadLocal变量，用于存储当前线程的用户ID
     */
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户ID
     *
     * @param id 用户ID
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前线程的用户ID
     *
     * @return 当前线程的用户ID，如果未设置则返回null
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 移除当前线程的用户ID，防止内存泄漏
     * 在请求处理完成后需要调用此方法清理ThreadLocal
     */
    public static void remove() {
        threadLocal.remove();
    }
}