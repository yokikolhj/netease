package com.shirly.neteasemaster.function.high_quality;

import com.shirly.neteasemaster.decoupingDemo.model.Order;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 16:29
 * @description 描述
 */
@Service
public class MsgService {

    public void sendMsg(Msg msg) {
        // 保存发送的消息
        System.out.println("消息：" + msg);
    }

    @EventListener //告诉spring这是一个事件监听方法，监听Order事件
    @Async // 这个方法异步执行
    public void handleOrderEvent(Order order) {
        // 发短信
        Msg msg = new Msg(order.getId(), "17721013884", "恭喜航班预订成功！");
        sendMsg(msg);
    }
}
