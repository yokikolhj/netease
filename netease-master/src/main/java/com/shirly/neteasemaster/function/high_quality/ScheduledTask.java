package com.shirly.neteasemaster.function.high_quality;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/3 11:59
 * @description 定时任务
 */
@Component
public class ScheduledTask {
    private int count =0;

    // 容器启动后开始每隔两秒钟执行一次
//    @Scheduled(fixedRate = 2000L)
    public void work() {
        System.out.println("第" + (++count) + "次执行at-" + new Date());
    }
}
