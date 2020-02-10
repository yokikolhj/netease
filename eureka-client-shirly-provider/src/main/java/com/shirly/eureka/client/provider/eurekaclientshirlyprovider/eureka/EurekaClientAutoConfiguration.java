package com.shirly.eureka.client.provider.eurekaclientshirlyprovider.eureka;

import com.alibaba.fastjson.JSON;
import com.shirly.eureka.client.EurekaClientProperties;
import com.shirly.eureka.model.EurekaClient;
import com.shirly.eureka.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author shirly
 * @Date 2020/2/7 21:23
 * @Description
 */
@Configuration // 配置类，在启动时初始化自身并注册到eureka上去
//@Component
@EnableConfigurationProperties(EurekaClientProperties.class)
public class EurekaClientAutoConfiguration {

    ScheduledExecutorService heartBeat = Executors.newScheduledThreadPool(1);

    @Autowired
    EurekaClient eurekaClient;

    /**
     * 1.启动时，加载配置，并做成一个ServiceInstance的bean
     * @param properties
     * @return
     */
    @Bean
    public EurekaClient client(EurekaClientProperties properties) {
        System.out.println("开始初始化EurekaClient-bean");
        EurekaClient eurekaClient = new EurekaClient();
        eurekaClient.setHost(properties.getHost());
        eurekaClient.setPort(properties.getPort());
        eurekaClient.setServerName(properties.getServerName());
        return eurekaClient;
    }

    /**
     * 2.将配置文件中bean进行处理，并调用心跳接口，注册到注册中心去
     */
    @PostConstruct
    public void register() {
        heartBeat.scheduleAtFixedRate(() -> {
            // 发起http请求调用心跳接口
            System.out.println("开始发送心跳到注册中心");
            try {
                HttpUtils.sendHttpPost("http://localhost:8081/netease/heartBeat", JSON.toJSONString(eurekaClient));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

}
