package com.shirly.neteasemaster.function.jdbc_starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/3 16:02
 */
@Component
public class UserService {

    @Autowired //程序使用某个类的对象，创建并注入
            JdbcTemplate jdbcTemplate; //spring-jdbc包

    public Map<String, Object> find(Integer id) {
        String sql = "select * from jpa_user where id=" + id;
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return result;
    }

    public List<Map<String, Object>> connectMyCat() {
        String sql = "SHOW TABLES";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return result;
    }
    //观察者模式：定义对象间一对多的依赖关系，当一个对象的状态发送改变时，所有依赖于它的对象都得到**通知**并被自动更新
}
