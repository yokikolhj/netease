package com.shirly.neteasemaster.function.aop.myaop;

import com.shirly.neteasemaster.function.aop.service.KTVPrincess;
import com.shirly.neteasemaster.function.aop.service.KTVService;
import com.shirly.neteasemaster.function.aop.service.SPAPrincess;
import com.shirly.neteasemaster.function.aop.service.SPAService;

/**
 * @Author shirly
 * @Date 2020/1/28 16:38
 * @Description
 */
public class AopMain {
    public static void main(String[] args) throws Exception {
        // 使用者提供的增强逻辑，切入点都有了
        Advice advice = new TimeCsAdvice();
        Pointcut pointcut = new Pointcut("com\\.shirly\\.neteasemaster\\.function\\.aop\\.service\\..*", ".*Massage");

        Aspect aspect = new Aspect(advice, pointcut);

        // 完成框架的功能
//        SPAService spa = new SPAPrincess();
//        spa.aromaOilMassage("shirly");
//        spa.fullBodyMassage("li");

        // 如果要玩AOP，需要一个工厂来负责提供对象，需要一个IOC
        // 写个简化的IOC容器
        IocContainer ioc = new IocContainer();
        ioc.addBeanDefinition("spa", SPAPrincess.class);
        ioc.addBeanDefinition("ktv", KTVPrincess.class);

        ioc.setAspect(aspect);

        SPAService spa = (SPAService) ioc.getBean("spa");
        spa.aromaOilMassage("shirly");
        spa.fullBodyMassage("li");
        KTVService ktv = (KTVService) ioc.getBean("ktv");
        ktv.sing("ju");
    }
}
