package com.shirly.neteasemaster.function.design.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 13:45
 * @description 电商场景1
 */
@Service
public class OrderService {

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 伪代码
     * 电商 - 新订单 - 不断维护（外包除外）
     */
    public void saveOrder() {
        // TODO 主要职责：创建订单
        System.out.println("1、订单创建成功");

        // 目标：后续新增功能，不需要再次修改这个类
        // 利用Spring事件驱动-发布事件
        applicationContext.publishEvent(new OrderEvent(applicationContext, "shirly"));

        /*// TODO 因为有了订单，产生后续一系列的代码逻辑
        System.out.println("2、短信发送成功");

        System.out.println("3、微信通知成功");

        // ...*/
    }

}
