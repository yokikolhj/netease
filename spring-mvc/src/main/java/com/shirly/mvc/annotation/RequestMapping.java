package com.shirly.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/5 14:52
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
