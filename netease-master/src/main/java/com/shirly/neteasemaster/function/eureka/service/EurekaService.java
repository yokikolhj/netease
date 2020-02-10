package com.shirly.neteasemaster.function.eureka.service;

import com.shirly.neteasemaster.function.eureka.model.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author shirly
 * @Date 2020/2/7 19:36
 * @Description Eureka注册中心服务
 */
public class EurekaService {

    // 存放服务
    Map<String, List<ServiceInstance>> serverMap = new ConcurrentHashMap<>();

    // 心跳机制用于检查服务是否在线
    public void heartBeat() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // 定时任务
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ServiceInstance serviceInstance = new ServiceInstance();
            if (System.currentTimeMillis() - serviceInstance.getIsDirtyWithTime() > 90) {
                serviceInstance.setStatus("Down");
            }
        },0,30, TimeUnit.SECONDS);
    }

    // 服务发现-提供查询在线服务功能

    // 传入服务名，获取相关的服务集合

    // 提供所有在线的服务

}
