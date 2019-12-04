package com.shirly.neteasemaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
}
