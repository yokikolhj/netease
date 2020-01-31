package com.shirly.neteasemaster.function.aop.myaop;

import com.shirly.neteasemaster.function.aop.service.KTVService;

/**
 * @Author shirly
 * @Date 2020/1/28 20:34
 * @Description 静态代理类
 */
public class StaticProxy implements KTVService { // 集成同样的接口

    private KTVService target; // 持有被代理对象

    public StaticProxy(KTVService target) {
        this.target = target;
    }

    // 静态代理
    @Override
    public void sing(String customer) {
        // 执行业务逻辑之前代码
        long startTime = System.currentTimeMillis();
        // 执行业务逻辑
        target.sing(customer);
        // 执行业务逻辑之后代码
        long useTime = System.currentTimeMillis() - startTime;
        System.out.println("记录：" + target.getClass().getName() + ".sing 耗时：" + useTime + "ms");
    }
}
