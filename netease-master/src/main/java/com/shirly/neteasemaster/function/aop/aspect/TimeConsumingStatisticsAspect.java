package com.shirly.neteasemaster.function.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author shirly
 * @Date 2020/1/21 14:19
 * @Description
 */
@Aspect
@Component
public class TimeConsumingStatisticsAspect {

    @Around("execution(* com.shirly.neteasemaster.function.aop.service.*.*(..))")
    public Object methodTimeConsumingStatistics(ProceedingJoinPoint pjp) throws Throwable {
        // 执行业务逻辑之前代码
        long startTime = System.currentTimeMillis();
        Object ret = pjp.proceed(); // 执行业务逻辑
        // 执行业务逻辑之后代码
        long useTime = System.currentTimeMillis() - startTime;
        System.out.println("记录：" + pjp.getTarget() + "." + pjp.getSignature() + " 耗时：" + useTime + "ms");
        return ret;
    }
}
