package com.shirly.transaction.order.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 15:09
 * @description MQ服务
 */
@Service
public class MQService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct // 在服务器加载Servlet的时候运行，并且只会被服务器执行一次
    public void setup() {
        rabbitTemplate.setConfirmCallback((correlationData, ask, cause) -> {
            // ask为true，代表目前已经准确收到消息
            System.out.println("收到回执：" + correlationData);
            if (!ask) {
                return;
            }
            try {
                // 修改本地消息表状态为已发送
                String sql = "update distributed_message set msg_status=1 where unique_id=?";
                int count = jdbcTemplate.update(sql, correlationData.getId());
                if (count != 1) {
                    System.out.println("警告：修改本地消息表状态修改不成功");
                }
            } catch (Exception e) {
                System.out.println("警告：修改本地消息表状态时出现异常");
            }
        });
    }

    /**
     * 发送MQ消息，修改本地消息表状态
     * @param orderInfo
     * @throws Exception
     */
    public void sendMsg(JSONObject orderInfo) throws Exception {
        // 1.发送消息到MQ
        rabbitTemplate.convertAndSend("", "orderQueue", orderInfo.toJSONString(),
                new CorrelationData(orderInfo.getString("orderId")));
    }
}
