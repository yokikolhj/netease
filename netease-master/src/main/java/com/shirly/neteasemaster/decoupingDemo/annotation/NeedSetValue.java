package com.shirly.neteasemaster.decoupingDemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 11:50
 * @description 注解没有任何逻辑，就只是java注释
 *     需要知道这些信息，用注解来定义
 */
@Target(ElementType.FIELD) // 在类的属性上生效
@Retention(RetentionPolicy.RUNTIME) // 能够存在于JVM运行期
public @interface NeedSetValue {
    // 贴在这个类头上的标签属性

    // 要调用的是哪个bean
    Class<?> beanClass();

    // 需要传入的参数
    String param();
//    String[] param();

    // 调用的方法
    String method();

    // 获取结果集的哪个值取设置到当前属性中
    String targetField();
}
