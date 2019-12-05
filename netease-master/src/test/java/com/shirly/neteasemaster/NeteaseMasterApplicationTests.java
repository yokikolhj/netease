package com.shirly.neteasemaster;

import com.shirly.neteasemaster.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class NeteaseMasterApplicationTests {

    @Autowired
    UserService userService;

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
}
