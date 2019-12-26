package com.shirly.transaction.order.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 15:07
 * @description 订单数据库相关服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderDatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate; // spring提供

    @Autowired
    RabbitTemplate rabbitTemplate; // rabbit提供

    public void saveOrder(JSONObject orderInfo) throws Exception {
        String sql = "insert into order(order_id,user_id,order_content) values(?,?,?)";
        // 1.添加订单记录
        jdbcTemplate.update(sql, orderInfo.get("orderId"), orderInfo.get("userId"), orderInfo.get("orderContent"));

        // 2.生成并记录发送到mq消息记录
        saveLocalMessage(orderInfo);
    }

    /**
     * 生成并记录发送到mq消息记录
     * @param orderInfo
     * @throws Exception
     */
    private void saveLocalMessage(JSONObject orderInfo) throws Exception {
        rabbitTemplate.convertAndSend("orderQueue", orderInfo);
    }
}
