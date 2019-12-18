package com.shirly.neteasemaster.decoupingDemo.model;

import com.shirly.neteasemaster.decoupingDemo.annotation.NeedSetValue;
import com.shirly.neteasemaster.decoupingDemo.dao.UserDao;

import java.io.Serializable;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 10:37
 * @description 描述
 */
public class Order implements Serializable {

    private String id;
    private String customerId;

    @NeedSetValue(beanClass= UserDao.class, param="customerId", method="find", targetField = "name") //注解
    private String customerName; // 没有值

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
