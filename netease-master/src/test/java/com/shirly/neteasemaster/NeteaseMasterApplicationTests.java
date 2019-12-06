package com.shirly.neteasemaster;

import com.shirly.neteasemaster.function.design.order.OrderService;
import com.shirly.neteasemaster.function.design.sale.SaleService;
import com.shirly.neteasemaster.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class NeteaseMasterApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    SaleService saleService;

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        System.out.println("开始测试");
    }

    @Test
    void testFindUser() {
        System.out.println("连接数据库查找");
        Map<String, Object> result = userService.find(1);
        System.out.println(result);
    }

    @Test
    void testMyCat() {
        System.out.println("连接mycat查找");
        List<Map<String, Object>> result = userService.connectMyCat();
        System.out.println(result);
    }

    @Test
    void testOrderService() {
        orderService.saveOrder();
    }

    @Test
    void testSaleService() {
        applicationContext.getBean("saleService");

        double vip = saleService.sale("vip", 100);
        double normal = saleService.sale("normal", 100);
        double svip = saleService.sale("svip", 100);
        System.out.println("vip:" + vip + " normal:" + normal + " svip:" + svip);
    }
}
