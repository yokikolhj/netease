package com.shirly.neteasemaster.function.eureka.controller;

import com.shirly.neteasemaster.function.eureka.model.ServiceInstance;
import com.shirly.neteasemaster.function.eureka.service.EurekaServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author shirly
 * @Date 2020/2/7 19:48
 * @Description
 */
@RestController
public class EurekaController {

    @Autowired
    EurekaServiceInstance eurekaServiceInstance;

    // 服务注册
    @PostMapping("/heartBeat")
    public boolean heartBeat(@RequestBody ServiceInstance instance) {
        return eurekaServiceInstance.heartBeat(instance);
    }

    // 服务发现
    @GetMapping("/getServer")
    public List<ServiceInstance> getServer(String serverId) {
        return eurekaServiceInstance.getServer(serverId);
    }

    // 开放接口，获取所有的服务列表
    @GetMapping("/getAllServer")
    public Map<String, List<ServiceInstance>> getServerMap() {
        return eurekaServiceInstance.getAllServer();
    }
}
