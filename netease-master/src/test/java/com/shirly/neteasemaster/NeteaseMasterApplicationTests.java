package com.shirly.neteasemaster;

import com.shirly.neteasemaster.function.design.order.OrderService;
import com.shirly.neteasemaster.function.design.sale.SaleService;
import com.shirly.neteasemaster.function.jdbc_starter.UserService;
import com.shirly.neteasemaster.function.transaction_annotation.pojo.User;
import com.shirly.neteasemaster.function.transaction_annotation.service.UserService_Transaction;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
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

    @Autowired
    UserService_Transaction userService_transaction;

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

    @Test
    void testInsertUser() {
        User user = new User();
        user.setEmail("xxx@xxx");
        user.setLastName("shirly");
        userService_transaction.insertUser(user);
    }

    @Test
    void testDeleteUser() throws SQLException {
        userService_transaction.deleteUser(3);
    }
}
