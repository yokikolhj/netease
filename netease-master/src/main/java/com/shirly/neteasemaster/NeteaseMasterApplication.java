package com.shirly.neteasemaster;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Properties;

@SpringBootApplication
@EnableAspectJAutoProxy // 开启AOP
public class NeteaseMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeteaseMasterApplication.class, args);
    }

    // 配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql"); // 配置mysql数据库方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    // 配置shiro的web filter,将servlet filter交给spring委托代理
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> getShiroFilter() {
        FilterRegistrationBean<DelegatingFilterProxy> frb = new FilterRegistrationBean<>(
                new DelegatingFilterProxy("shiroFilter"));
        frb.addUrlPatterns("/");
        frb.addInitParameter("targetFilterLifecycle", "true");
        return frb;
    }
}
