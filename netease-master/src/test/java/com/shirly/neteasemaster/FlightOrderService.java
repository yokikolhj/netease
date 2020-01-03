package com.shirly.neteasemaster;

import com.shirly.neteasemaster.decoupingDemo.model.Order;
import com.shirly.neteasemaster.function.high_quality.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 17:11
 * @description 描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlightOrderService {

    @Autowired
    OrderService orderService;

    @Test
    public void test() {
        Order order = new Order();
        order.setId("userId");
        orderService.addFlightOrder(order);
    }
}
