package com.shirly.neteasemaster.function.aop.myaop;

import java.lang.reflect.Method;

/**
 * @Author shirly
 * @Date 2020/1/28 16:13
 * @Description 提供统一advice的API
 */
public interface Advice {
    // 定义一个行为
    // 用户在此提供增强逻辑
    Object invoke(Object target, Method method, Object[] args) throws Exception;
}
