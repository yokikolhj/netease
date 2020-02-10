package com.shirly.neteasemaster.function.eureka.service;

import com.shirly.neteasemaster.function.eureka.model.ServiceInstance;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author shirly
 * @Date 2020/2/7 19:58
 * @Description
 */
@Service
public class EurekaServiceInstanceImpl implements EurekaServiceInstance {

    // 注册中心的存放
    private Map<String, List<ServiceInstance>> serverMap = new ConcurrentHashMap<>();

    private ScheduledExecutorService removeScheduled = Executors.newScheduledThreadPool(1);

    /**
     * 服务同步
     * 每隔30秒把已经下线的服务剔除掉
     */
    @PostConstruct
    private void renewService() {
        removeScheduled.scheduleAtFixedRate(() -> {
            System.out.println("执行剔除服务功能");
            // 循环遍历 - 准备两个list处理也可以，一个处理新来的心跳，一个把预剔除的服务加入进去
            for (String key : serverMap.keySet()) {
                List<ServiceInstance> instances = serverMap.get(key);
                for (ServiceInstance instance : instances) {
                    if (System.currentTimeMillis() - instance.getIsDirtyWithTime() > 90000) {
                        instance.setStatus("DOWN"); // 如果有30s没有数据就判断下线
                    } else {
                        instance.setStatus("UP"); // 如果有新的心跳，就判断服务已经恢复
                    }
                }
            }
        }, 60, 30, TimeUnit.SECONDS);
    }

    /**
     * 查看服务状态，修改最后一次心跳时间
     * @param instance 服务对象
     * @return true
     */
    @Override
    public boolean heartBeat(ServiceInstance instance) {
        // list允许有多个集群的地址，
        List<ServiceInstance> instances = serverMap.get(instance.getServerName());
        if (instances == null) {
            // 认为新的服务上线
            addInstance(instance, new ArrayList<>());
            return true;
        }
        // 完全没有的，就是注册
        // 原来有这个服务，但是现在有个新地址加进来
        // 心跳，就是修改一下时间
        switch (renew(instance)) {
            case "404": // 注册
                instances = new ArrayList<>();
            case "303": // 添加新的服务进去
            case "200": // 修改时间
                addInstance(instance, instances);
                break;
        }
        return true;
    }

    /**
     * 添加服务
     * @param instance 服务对象
     * @param instances 同名服务对象集合服务对象集合
     */
    private void addInstance(ServiceInstance instance, List<ServiceInstance> instances) {
        instance.setStatus("UP");
        instance.setIsDirtyWithTime(System.currentTimeMillis());
        instances.add(instance);
        serverMap.put(instance.getServerName(), instances);
    }

    /**
     * 更新，服务续约，服务注册
     * @param instance 服务对象
     * @return 查找结果编码
     */
    public String renew(ServiceInstance instance) {
        int count = 0;
        String code = "404";
        if (serverMap.containsKey(instance.getServerName())) {
            List<ServiceInstance> instances = serverMap.get(instance.getServerName());
            if (instances != null) {
                for (ServiceInstance oldInstance : instances) {
                    if (oldInstance.getHost().equals(instance.getHost()) && oldInstance.getPort() == instance.getPort()) {
                        instances.remove(oldInstance); // 移除这个服务，之后再重新添加，为修改时间
                        count ++;
                        break;
                    }
                }
            }
            // 200表示有这个key，且有相同ip和端口的服务
            // 300表示有这个key,但是ip和端口没有
            code = count > 0 ? "200" : "303";
        }
        return code;
    }

    @Override
    public List<ServiceInstance> getServer(String serverId) {
        List<ServiceInstance> aliveInstances = new ArrayList<>();
        // 去掉状态为DOWN的服务
        List<ServiceInstance> instances = serverMap.get(serverId);
        if (instances != null) {
            for (ServiceInstance instance : instances) {
                if (!instance.getStatus().equals("DOWN")) {
                    aliveInstances.add(instance);
                }
            }
        }
        return aliveInstances;
    }

    @Override
    public Map<String, List<ServiceInstance>> getAllServer() {
        return serverMap;
    }
}
