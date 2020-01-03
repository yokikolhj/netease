package com.shirly.neteasemaster.function.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/25 17:12
 * @description shiro过滤器
 */
@Configuration //配置类，用来取代spring.xml那一堆<beans></beans>头文件
public class ShiroFilterConfiguration {

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager manager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置
        shiroFilterFactoryBean.setSecurityManager(manager);
        /*
         * 重要，设置自定义拦截器，当访问某些自定义url时，使用这个filter进行验证
         */
        Map<String, Filter> filters = new LinkedHashMap<>();
        //如果map里面key值为authc,表示所有名为authc的过滤条件使用这个自定义的filter
        //map里面key值为myFilter,表示所有名为myFilter的过滤条件使用这个自定义的filter，具体见下方
        filters.put("myFilter", new MyFilter());
        shiroFilterFactoryBean.setFilters(filters);

        //拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin/**", "authc,roles[admin]");
        filterChainDefinitionMap.put("/docs/**", "authc,perms[document:read]");
        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //　　anon:所有url都都可以匿名访问;
        //　　authc: 需要认证才能进行访问;
        //　　user:配置记住我或认证通过可以访问；
        //放开静态资源的过滤
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        //放开登录url的过滤
        filterChainDefinitionMap.put("/loginController", "anon");
        //对于指定的url，使用自定义filter进行验证
        filterChainDefinitionMap.put("/targetUrl", "myFilter");
        //可以配置多个filter，用逗号分隔，按顺序过滤，下方表示先通过自定义filter的验证，再通过shiro默认过滤器的验证
        //filterChainDefinitionMap.put("/targetUrl", "myFilter,authc");
        //过滤链定义，url从上向下匹配，当条件匹配成功时，就会进入指定filter并return(不会判断后续的条件)，因此这句需要在最下边
        filterChainDefinitionMap.put("/**", "authc");

        // 默认约定获取表单中的username和password
        //如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/loginSuccess");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /*@Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        return securityManager;
    }*/

    //配置核心安全事务管理器
    @Bean(name="securityManager")
    public SecurityManager securityManager(@Qualifier("shiroRealm") JdbcRealm shiroRealm) {
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(shiroRealm);
        return manager;
    }

    //配置自定义的权限登录器
    @Bean(name="shiroRealm")
    public JdbcRealm shiroRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher, DataSource dataSource) {
        JdbcRealm shiroRealm = new JdbcRealm();
        shiroRealm.setCredentialsMatcher(matcher);
        shiroRealm.setDataSource(dataSource);
        shiroRealm.setPermissionsLookupEnabled(true);
        // 自己写查询语句
        shiroRealm.setAuthenticationQuery("select user_name,password,password_salt from user where user_name=?");
        return shiroRealm;
    }

    //配置自定义的密码比较器
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return (authenticationToken, authenticationInfo) -> {
            return false;
        };
    }

    // 配置方法级别访问控制,配置代理生成bean
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return  new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator proxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor sourceAdvisor(SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(manager);
        return sourceAdvisor;
    }

}
