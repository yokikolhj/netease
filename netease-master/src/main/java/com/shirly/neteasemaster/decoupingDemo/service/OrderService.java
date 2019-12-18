package com.shirly.neteasemaster.decoupingDemo.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shirly.neteasemaster.decoupingDemo.annotation.NeedSetFieldValue;
import com.shirly.neteasemaster.decoupingDemo.dao.OrderDao;
import com.shirly.neteasemaster.decoupingDemo.dao.UserDao;
import com.shirly.neteasemaster.decoupingDemo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 10:25
 * @description 描述
 */
@Service("decoupingOrderService")
public class OrderService {

    // required=true 表示注入的时候，该bean必须存在，否则就会注入失败
    @Autowired(required = false) // 忽略当前要注入的bean，如果有直接注入，没有跳过，不会报错。
    private OrderDao orderDao;

    /*@Autowired
    UserDao userDao;*/

    // 只要有某个注解修饰的方法，都需要进行切面点操作
    @NeedSetFieldValue
    public Page<Order> pageQuery(String customerId, int pageNum, int pageSize) {
        Page<Order> page = PageHelper.startPage(pageNum, pageSize);
        orderDao.query(customerId);

        // 需要获得订单客户姓名
        /*// 违反开闭原则
        for (Order order : page) {
            User user = userDao.find(order.getCustomerId());
            if (user != null) {
                order.setCustomerName(user.getUserName());
            }
        }*/

        // AOP本质就是在这个操作之前或之后去做其他事

        return page;
    }
}
