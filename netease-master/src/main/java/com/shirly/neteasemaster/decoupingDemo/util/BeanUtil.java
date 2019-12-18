package com.shirly.neteasemaster.decoupingDemo.util;

import com.github.pagehelper.util.StringUtil;
import com.shirly.neteasemaster.decoupingDemo.annotation.NeedSetValue;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 15:14
 * @description 获取bean
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    private  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.applicationContext == null) {
            this.applicationContext = applicationContext;
        }
    }

    /**
     * 对结果集设置值
     * @param collection
     */
    public void setFieldValueForCollection(Collection collection) throws Exception {
        // 通过反射拿到对象
        Class<?> clazz = collection.iterator().next().getClass(); // Order对象
        Field[] fields = clazz.getDeclaredFields(); // id、customerId、customerName

        Map<String, Object> cache = new HashMap<>(); // 工作中一般用redis

        // 遍历是否有@NeedSetValue
        for (Field field : fields) {
            NeedSetValue nsv = field.getAnnotation(NeedSetValue.class);
            if (nsv == null) {
                continue;
            }
            // 拿到之后设置可见
            field.setAccessible(true);
            // 1.调用的bean
            Object beanClass = this.applicationContext.getBean(nsv.beanClass());
            // 2.获取到调用的方法
            Method method =nsv.beanClass().getMethod(nsv.method(), clazz.getDeclaredField(nsv.param()).getType());
            // 3.入参对象
            Field paramField = clazz.getDeclaredField(nsv.param());
            paramField.setAccessible(true);
            // 4.值的来源
            Field targetField = null;

            String keyPrefix = nsv.beanClass() + "-" + nsv.method() + "-" + nsv.targetField() + "-";
            for (Object object : collection) {
                Object paramValue = paramField.get(object);
                if (paramValue == null) {
                    continue;
                }
                Object value = null;
                // 先那缓存
                String key = keyPrefix + paramValue;
                if (cache.containsKey(key)) {
                    value = cache.get(key);
                } else {
                    value = method.invoke(beanClass, paramValue); // User对象

                    if (!StringUtil.isEmpty(nsv.targetField())) {
                        if (value != null) {
                            if (targetField == null) {
                                targetField = value.getClass().getDeclaredField(nsv.targetField());
                                targetField.setAccessible(true);
                            }
                            value = targetField.get(value);
                        }
                    }
                    cache.put(key, value);
                }
                field.set(object, value);
            }
        }
    }

}
