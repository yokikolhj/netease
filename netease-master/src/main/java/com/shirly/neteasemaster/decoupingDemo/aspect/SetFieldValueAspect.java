package com.shirly.neteasemaster.decoupingDemo.aspect;

import com.shirly.neteasemaster.decoupingDemo.util.BeanUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 18:28
 * @description 描述
 */
@Component
@Aspect
public class SetFieldValueAspect {

    @Autowired
    BeanUtil beanUtil;

    @Around("@annotation(com.shirly.neteasemaster.decoupingDemo.annotation.NeedSetFieldValue)")
    public Object doSetFieldValue(ProceedingJoinPoint joinPoint) throws Throwable {
        //不做前置处理
        Object ret = joinPoint.proceed(); //拿到的是结果集
        // 结果集如下：
        /**
         * {id=1,customerId=1,customerName=null},
         * {id=1,customerId=1,customerName=null},
         * ...
         * customerName上面有@NeedSetValue
         * */
        if (ret instanceof Collection) {
            beanUtil.setFieldValueForCollection((Collection) ret);
        }

        return ret;
    }
}
