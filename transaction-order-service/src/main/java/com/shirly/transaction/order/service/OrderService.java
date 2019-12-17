package com.shirly.transaction.order.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 15:04
 * @description 订单系统服务
 */
@Service
public class OrderService {

    @Autowired
    OrderDatabaseService orderDatabaseService;

    @Autowired
    MQService mqService;

    /*@Transactional*/
    public void createOrder(JSONObject orderInfo) throws Exception {
        // 1.订单信息保存到订单数据库
        orderDatabaseService.saveOrder(orderInfo); // 同一个系统数据库：订单+消息发送记录

        /*// 2.通过http接口发送订单信息到运单系统 --接口靠不住
        String result = callDispatchHttpApi(orderInfo);
        if (!"OK".equals(result)) {
            throw new Exception("订单创建失败，原因[运单接口调用失败]");
        }*/

        // 问题1：发送消息时失败（系统挂掉） --- 添加本地消息表
        // 问题2：发送过程中失败（网络、MQ等问题） --- MQ发布确认机制
                 // 确认发送成功，系统挂掉，再重发就行了，多次消费保持幂等性
        // 问题3：消费过程中失败

        // 不用接口传递数据，用MQ
        mqService.sendMsg(orderInfo);
    }

    /**
     * 通过http接口发送到运单系统，将订单号传过去
     * @param orderInfo
     * @return
     */
    private String callDispatchHttpApi(JSONObject orderInfo) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(3000); //单位毫秒
        requestFactory.setReadTimeout(2000); //单位毫秒

        RestTemplate restTemplate = new RestTemplate(requestFactory); // spring提供的一个http客服端工具
        String httpUrl = "http://127.0.0.1:8087/dispatch-api/dispatch?orderId=" + orderInfo.getString("orderId");
        String result = restTemplate.getForObject(httpUrl, String.class);

        return result;
    }
}
