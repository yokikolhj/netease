package com.shirly.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/5 14:44
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
public @interface Controller {
    // 给控制器取名
    String value() default "";
}
