package com.shirly.neteasemaster.function.design.order;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 14:09
 */
@Component
public class WeChatListener implements ApplicationListener<OrderEvent> {
    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {
        System.out.println("3、微信通知成功");
    }
}
