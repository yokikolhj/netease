package com.shirly.neteasemaster.function.design.sale;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 15:31
 * @description 打折接口
 *     有很多实现，每一种实现是一种策略，封装不同的方法
 */
public interface Calculate {
    // 针对用户群体
    String userType();
    // 打折计算方法 - 把不同的打折策略单独封装起来（OOP 封装继承多态）
    double discount(double fee);
}
