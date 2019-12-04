package com.shirly.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/3 14:49
 */
@Configuration // 配置类
@EnableConfigurationProperties(JdbcProperties.class) // 启用配置属性类--spring IOC会有一个JdbcProperties对象
public class JdbcConfig {

    @Bean // 注解，类似于xml中<bean />，返回值作为bean对象存到spring IOC容器，名称根据方法名首字母小写等
    public DataSource dataSource(JdbcProperties jdbcProperties) {
        System.out.println("初始化了自定义的DataSource");
        DruidDataSource dataSource = new DruidDataSource();
        // 数据源需要url、username、password属性配置信息
        dataSource.setUrl(jdbcProperties.getUrl());
        dataSource.setUsername(jdbcProperties.getUsername());
        dataSource.setPassword(jdbcProperties.getPassword());
        dataSource.setDriverClassName(jdbcProperties.getDriverClassName());
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource); // 数据库操作工具，操作指定的数据源
    }

}
