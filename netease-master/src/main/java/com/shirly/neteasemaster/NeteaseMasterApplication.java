package com.shirly.neteasemaster;

import com.github.pagehelper.PageHelper;
import com.shirly.neteasemaster.function.aop.service.SPAService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Properties;

@SpringBootApplication
@EnableAspectJAutoProxy // 开启AOP
@EnableAsync //开启异步支持
@EnableScheduling //开启定时任务
public class NeteaseMasterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NeteaseMasterApplication.class, args);

        /*TimeUtil.SECONDS.sleep(30L);
        // 容器自动关闭
        context.close();*/

        SPAService spa = context.getBean(SPAService.class);
        spa.aromaOilMassage("shirly");
        spa.aromaOilMassage("li");
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
