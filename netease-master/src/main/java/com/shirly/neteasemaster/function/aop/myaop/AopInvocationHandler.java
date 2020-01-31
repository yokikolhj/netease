package com.shirly.neteasemaster.function.aop.myaop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author shirly
 * @Date 2020/1/28 21:23
 * @Description
 */
public class AopInvocationHandler implements InvocationHandler {

    private Object target; // 持有被代理对象

    private Aspect aspect; // 切面增强的逻辑

    public AopInvocationHandler(Object target, Aspect aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 判断当前方法是否需要增强
        if (method.getName().matches(aspect.getPointcut().getMethodPattern())) {
            return aspect.getAdvice().invoke(target, method, args);
        }
        // 不需要增强，直接调用业务对象的方法
        return method.invoke(target, args);
    }
}
