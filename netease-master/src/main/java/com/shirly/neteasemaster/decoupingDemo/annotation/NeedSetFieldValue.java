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
@Target(ElementType.METHOD) // 在类的方法上生效
@Retention(RetentionPolicy.RUNTIME) // 能够存在于JVM运行期
public @interface NeedSetFieldValue {
}
