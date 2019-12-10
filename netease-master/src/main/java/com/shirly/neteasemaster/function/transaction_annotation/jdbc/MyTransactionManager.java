package com.shirly.neteasemaster.function.transaction_annotation.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/10 15:06
 * @discription 为实现事存勋在
 */
@Component // spring默认创建一个单例
public class MyTransactionManager {

    @Autowired
    DataSource dataSource;// c3p0...//

    ThreadLocal<Connection> connection = new ThreadLocal<>(); // 一个对象一个值

    public Connection getConnection() throws SQLException {
        if (connection.get() == null) {
            connection.set(dataSource.getConnection());
        }
        return connection.get();
    }

}
