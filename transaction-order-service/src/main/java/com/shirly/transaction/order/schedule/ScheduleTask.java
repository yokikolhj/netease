package com.shirly.transaction.order.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 17:53
 * @description 定时任务
 */
@Component
public class ScheduleTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 3000) // 针对长时间没有发送成功的消息重新发送
    public void reportCurrentTime() {
        System.out.println("当前时间：" + dateFormat.format(new Date()));
        // TODO 检查消息记录表是否都成功发送消息
        // 如果持续异常：不是系统自动能够解决，需要人工干预
    }

}
