package com.shirly.neteasemaster.function.high_quality;

import com.shirly.neteasemaster.decoupingDemo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 16:22
 * @description 描述
 */
@Service("flightOrderService")
public class OrderService {

    @Autowired
    ApplicationContext context;

//    @Autowired
//    private EmailService es;
//
//    @Autowired
//    private MsgService ms;

    @Transactional
    public void addFlightOrder(Order order) {
        // TODO

        // 以下违背了很多设计原则
//        // 发短信
//        Msg msg = new Msg(order.getId(), "17721013884", "恭喜航班预订成功！");
//        ms.sendMsg(msg);
//        // 发邮件
//        Email email = new Email("恭喜航班预订成功", "您的航班信息...", order.getId());
//        es.sendEmail(email);

        // 以下优化
        // 发布者 order对象就是事件 时间通道为spring的ApplicationContext
        context.publishEvent(order);
    }
}
