package com.shirly.neteasemaster.function.design.order;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 11:59
 * @description 监听者，触发-因为发布了事件
 *              标识直接需要监听的事件类型
 */
@Component
public class SmsExtListener implements SmartApplicationListener {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return false; // 可判断事件是否是你需要处理的
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

    }
}
