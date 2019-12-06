package com.shirly.neteasemaster.function.design.order;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 13:55
 * @description 定义事件
 */
public class OrderEvent extends ApplicationContextEvent {
    public OrderEvent(ApplicationContext source, String params) {
        super(source);
        System.out.println("Hello " + params);
    }
}
