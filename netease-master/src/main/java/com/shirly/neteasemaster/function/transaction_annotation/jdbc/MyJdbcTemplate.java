package com.shirly.neteasemaster.function.transaction_annotation.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/10 14:52
 * @description 封装事务
 *   设计模式的体现 spring（***Template）、jdk(AQS模板方法模式)
 */
@Component
public class MyJdbcTemplate {

    @Autowired
    MyTransactionManager myTransactionManager;

    // 仅执行语句
    public void execute(String sql) throws SQLException {
        Connection connection = myTransactionManager.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(sql);
    }
}
