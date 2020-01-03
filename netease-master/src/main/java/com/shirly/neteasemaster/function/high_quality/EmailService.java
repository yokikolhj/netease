package com.shirly.neteasemaster.function.high_quality;

import com.shirly.neteasemaster.decoupingDemo.model.Order;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 16:29
 * @description 描述
 */
@Service
public class EmailService {

    public void sendEmail(Email email) {
        // 以写文件代替
        try {
            FileWriter writer = new FileWriter("C:/tmp/" + email.getTitle() + ".txt");
            writer.write(email.getContent());
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @EventListener
    @Async // 这个方法异步执行
    public void handleOrderEvent(Order order) {
        // 发邮件
        Email email = new Email("恭喜航班预订成功", "您的航班信息...", order.getId());
        sendEmail(email);
    }
}
