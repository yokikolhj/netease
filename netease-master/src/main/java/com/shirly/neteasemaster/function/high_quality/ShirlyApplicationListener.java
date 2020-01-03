package com.shirly.neteasemaster.function.high_quality;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/3 9:37
 * @description 描述
 */
@Component
public class ShirlyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("监听到ContextRefreshedEvent");
        // 初始化代码
    }
}
