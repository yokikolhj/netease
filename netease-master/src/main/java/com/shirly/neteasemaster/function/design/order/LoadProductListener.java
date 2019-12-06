package com.shirly.neteasemaster.function.design.order;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 11:59
 * @description Spring启动之后，执行一些特定的业务代码
 */
@Component
public class LoadProductListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 防止启动两次
        if (contextRefreshedEvent.getApplicationContext().getParent() != null) {
            System.out.println("Spring启动之后，执行一些初始化的操作（加载数据库的数据，初始数据化...）");
            // 添加需求
        }
    }
}
