package com.shirly.neteasemaster.function.transaction_annotation.jdbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/10 15:45
 */
@Component
@Aspect // AOP编程
public class MyTransactionAspect {

    @Autowired
    MyTransactionManager myTransactionManager;

    @Around("@annotation(MyTransactional)") // 凡是加了@MyTransactional注解的，方法执行前后增强
    public Object doTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("方法执行前增强了功能");
        Connection connection = myTransactionManager.getConnection();
        connection.setAutoCommit(false); // 关闭事务提交
        try {
            // AOP程序中控制 目标方法执行
            Object result = proceedingJoinPoint.proceed();

            System.out.println("方法执行后增强了功能");

            connection.commit();
            System.out.println("事务提交");

            return result;
        } catch (Exception e) {
            connection.rollback();
            System.out.println("事务回滚");
        }
        return null;
    }

}
