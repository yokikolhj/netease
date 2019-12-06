package com.shirly.neteasemaster.function.design.order;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 11:59
 * @description 监听者，触发-因为发布了事件
 *              标识直接需要监听的事件类型
 */
@Component
public class SmsListener implements ApplicationListener<OrderEvent> {
    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {
        System.out.println("2、短信发送成功");
    }
}
