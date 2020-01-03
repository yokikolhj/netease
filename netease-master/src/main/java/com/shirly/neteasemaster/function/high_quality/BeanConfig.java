package com.shirly.neteasemaster.function.high_quality;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/3 10:21
 * @description 配置ApplicationEventMulticaster
 */
//@Configuration
public class BeanConfig {

    // 配置线程池
    @Bean("coreThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor tp = new ThreadPoolTaskExecutor();
        tp.setMaxPoolSize(30);
        tp.setCorePoolSize(10);
        tp.setQueueCapacity(20);
        tp.setThreadNamePrefix("coreTaskExecutorThread-");
        return tp;
    }

    // 注意： 这个beanName必须是applicationEventMulticaster
    // 配置了线程池才走异步方式
    @Bean("applicationEventMulticaster")
    public SimpleApplicationEventMulticaster getSimpleApplicationEventMulticaster(
            @Qualifier("coreThreadPoolTaskExecutor") ThreadPoolTaskExecutor tp) {
        SimpleApplicationEventMulticaster sp = new SimpleApplicationEventMulticaster();
        // 设置异步执行线程池
        sp.setTaskExecutor(tp);
        return sp;
    }
}
