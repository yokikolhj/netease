package com.shirly.neteasemaster.function.eureka.service;

import com.shirly.neteasemaster.function.eureka.model.ServiceInstance;

import java.util.List;
import java.util.Map;

/**
 * @Author shirly
 * @Date 2020/2/7 19:54
 * @Description
 */
public interface EurekaServiceInstance {

    // 心跳
    boolean heartBeat(ServiceInstance instance);

    // 更新，服务续约，服务注册
    String renew(ServiceInstance instance);

    // 获取在线服务
    List<ServiceInstance> getServer(String serverId);

    Map<String, List<ServiceInstance>> getAllServer();
}
