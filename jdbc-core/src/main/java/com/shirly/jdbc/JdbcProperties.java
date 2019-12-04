package com.shirly.jdbc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/3 15:06
 * @description 属性配置文件里面的内容 注入到这个对象中
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class JdbcProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
