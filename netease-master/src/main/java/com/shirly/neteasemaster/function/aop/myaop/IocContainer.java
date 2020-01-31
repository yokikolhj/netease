package com.shirly.neteasemaster.function.aop.myaop;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author shirly
 * @Date 2020/1/28 17:33
 * @Description
 */
public class IocContainer {

    // 简化版的bean定义map
    private Map<String, Class<?>> beanDefinitionMap = new HashMap<>();

    // 用户切面可能有多个，这里只是一个
    private Aspect aspect;

    public void addBeanDefinition(String beanName, Class<?> beanClass) {
        beanDefinitionMap.put(beanName, beanClass);
    }

    // 行为
    public Object getBean(String beanName) throws Exception {
        // 需要能得到用户给定的bean的定义
        Object bean = createInstance(beanName);
        bean = proxyEnhance(bean);
        return bean;
    }

    // 进行代理增强
    private Object proxyEnhance(Object bean) {
        if (bean.getClass().getName().matches(aspect.getPointcut().getClassPattern())) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(),
                    new AopInvocationHandler(bean, aspect));
        }
        return bean;
    }

    private Object createInstance(String beanName) throws Exception {
        return beanDefinitionMap.get(beanName).newInstance();
    }

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
    }
}
