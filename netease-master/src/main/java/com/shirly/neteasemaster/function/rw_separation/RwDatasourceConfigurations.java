package com.shirly.neteasemaster.function.rw_separation;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author shirly
 * @Date 2020/1/16 16:06
 * @Description
 */
//@Configuration //配置类，用来取代spring.xml那一堆<beans></beans>头文件
public class RwDatasourceConfigurations {

    /**
     * '@Primary' 标志这个 Bean 如果在多个同类 Bean 候选时，该 Bean 优先被考虑。
     * 多数据源配置的时候注意，必须要有一个主数据源，用 @Primary 标志该 Bean
     * @return 写数据源
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.write")
    public DataSource writeDataSource() {
        return new DruidDataSource();
    }

    /**
     * 有多少个读库就要设置多少个读数据源，Bean名为read+序号
     * @return 读数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "mysql.datasource.read")
    public DataSource readDataSource() {
        return new DruidDataSource();
    }

    /**
     * @return 动态数据源
     */
    @Bean
    public DynamicDataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setWriteDataSource(writeDataSource());
        dataSource.setReadDataSource(readDataSource());
        return dataSource;
    }

//    /**
//     * 设置数据源路由，通过该类中的determineCurrentLookupKey决定使用哪个数据源
//     */
//    @Bean
//    public AbstractRoutingDataSource routingDataSource() {
//        MyAbstractRoutingDataSource proxy = new MyAbstractRoutingDataSource();
//        Map<Object, Object> targetDataSources = new HashMap<>(2);
//        targetDataSources.put(DynamicDataSourceGlobal.WRITE, writeDataSource());
//        targetDataSources.put(DynamicDataSourceGlobal.READ, readDataSource());
//        proxy.setDefaultTargetDataSource(writeDataSource());
//        proxy.setTargetDataSources(targetDataSources);
//        return proxy;
//    }

    /**
     * 多数据源需要自己设置sqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 实体类对应的位置
        bean.setTypeAliasesPackage("com.shirly.entity");
        // mybatis的XML的配置
        bean.setMapperLocations(resolver.getResources("classpath:com/shirly/..."));
        bean.setConfigLocation(resolver.getResource("classpath:com/shirly/..."));
        bean.setPlugins();
        return bean.getObject();
    }

    /**
     * 设置事务，事务需要知道当前使用的是哪个数据源才能进行事务处理
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
