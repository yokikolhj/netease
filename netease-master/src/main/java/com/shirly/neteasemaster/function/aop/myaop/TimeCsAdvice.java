package com.shirly.neteasemaster.function.aop.myaop;

import java.lang.reflect.Method;

/**
 * @Author shirly
 * @Date 2020/1/28 16:20
 * @Description
 */
public class TimeCsAdvice implements Advice {

    @Override
    public Object invoke(Object target, Method method, Object[] args) throws Exception {
        // 执行业务逻辑之前代码
        long startTime = System.currentTimeMillis();
        // 执行业务逻辑
        Object ret = method.invoke(target, args);
        // 执行业务逻辑之后代码
        long useTime = System.currentTimeMillis() - startTime;
        System.out.println("记录：" + target.getClass().getName() + "." + method.getName() + " 耗时：" + useTime + "ms");
        return ret;
    }
}
