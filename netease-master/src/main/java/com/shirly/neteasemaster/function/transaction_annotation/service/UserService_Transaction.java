package com.shirly.neteasemaster.function.transaction_annotation.service;

import com.shirly.neteasemaster.function.transaction_annotation.jdbc.MyTransactional;
import com.shirly.neteasemaster.function.transaction_annotation.jdbc.MyJdbcTemplate;
import com.shirly.neteasemaster.function.transaction_annotation.jdbc.MyTransactionManager;
import com.shirly.neteasemaster.function.transaction_annotation.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/10 11:23
 * @description Spring事务注解示例
 */
@Service
public class UserService_Transaction {

    @Autowired
    JdbcTemplate jdbcTemplate; // spring提供的一个jdbc工具，类似mybatis hibernate 框架

    @Transactional(rollbackFor = Exception.class) //spring注解  ACID
    public void insertUser(User user) {
        String userSql = "insert into jpa_user(last_name, email) values ('" + user.getLastName() + "','" + user.getEmail() + "')";
        jdbcTemplate.execute(userSql);
        String mapSql = "insert into roleusermap(userId, roleId) values (1,1)";
        jdbcTemplate.execute(mapSql);
        // 异常
        int i = 1/0;
    }

    @Autowired
    DataSource dataSource;// c3p0...

    // 不用事务注解控制
    public void deleteUserWithoutTransaction(Integer userId) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false); // 关闭事务提交
        Statement statement = connection.createStatement();
        try {
            String userSql = "delete from jpa_user where id=" + userId;
            statement.execute(userSql);
            String mapSql = "insert into roleusermap(userId, roleId) values (1,1)";
            statement.execute(mapSql);
            // 异常
//        int i = 1/0;
            connection.commit();
            System.out.println("事务提交");
        } catch (Exception e) {
            connection.rollback();
            System.out.println("事务回滚");
        }
    }

    @Autowired
    MyJdbcTemplate myJdbcTemplate;

    @Autowired
    MyTransactionManager myTransactionManager;

    @MyTransactional // 自己写一个控制事务
    public void deleteUser(Integer userId) throws SQLException {
        String userSql = "delete from jpa_user where id=" + userId;
        myJdbcTemplate.execute(userSql);
        String mapSql = "insert into roleusermap(userId, roleId) values (1,1)";
        myJdbcTemplate.execute(mapSql);
        // 异常
        int i = 1/0;
    }
}
