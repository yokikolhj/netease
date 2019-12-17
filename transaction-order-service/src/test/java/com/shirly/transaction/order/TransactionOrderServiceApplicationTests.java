package com.shirly.transaction.order;

import com.shirly.transaction.order.service.OrderService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

@SpringBootTest
class TransactionOrderServiceApplicationTests {

    @Autowired
    OrderService orderService;

    @BeforeAll
    public void start() {
        System.out.println("测试开始########################");
    }

    @AfterAll
    public void finish() {
        System.out.println("测试结束########################");
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void orderCreate() throws Exception {
        // 订单号生成
        String orderId = UUID.randomUUID().toString();
        JSONObject orderInfo = new JSONObject();
        orderInfo.put("orderId", orderId);
        orderInfo.put("userId", "shirly");
        orderInfo.put("orderContent", "爆炒基围虾");
        orderService.createOrder(orderInfo);
        System.out.println("订单创建成功");
    }

    // 单机/本地事务
    public void transaction() throws SQLException {
        DataSource dataSource = null;
        // 针对单个数据库和java应用连接
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        connection.commit();
        connection.rollback();
    }

    @Test
    public void linkedBlockQueue() {
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        linkedBlockingQueue.add(""); // 存
        linkedBlockingQueue.poll(); // 取
    }

}
