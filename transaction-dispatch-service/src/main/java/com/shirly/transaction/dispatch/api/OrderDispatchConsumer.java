package com.shirly.transaction.dispatch.api;

import com.rabbitmq.client.Channel;
import com.shirly.transaction.dispatch.service.DispatchService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 18:34
 * @description 从生产队列中取数据
 */
@Component
public class OrderDispatchConsumer {
    private final Logger logger = LoggerFactory.getLogger(OrderDispatchConsumer.class);

    @Autowired
    DispatchService dispatchService;

    // spring集成，方法触发spring
    @RabbitListener(queues = "orderQueue") // 远程rabbitmq服务器，rabbitmq客服端封装
    public void messageConsumer(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
            throws Exception {
        try {
            JSONObject orderInfo = JSONObject.parseObject(message);
            logger.warn("收到mq里面的消息：" + orderInfo.toJSONString());
            Thread.sleep(5000L);

            String orderId = orderInfo.getString("orderId");
            dispatchService.dispatch(orderId); // 去重,chuanjin
            // 告诉mq，已经拿到数据
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 监控、短信、微信、日志监控进行预警，根据异常去选择
            channel.basicNack(tag, false, true); // 让mq重发
            channel.basicNack(tag, false, false); // 让MQ不要重发，丢掉（到死信队列）---人工干预 系统预警 监控
            logger.error(e.getMessage());
        }
        // 如果不给回复，就等这个consumer断开之链接后，mq-server会继续推送

    }
}
