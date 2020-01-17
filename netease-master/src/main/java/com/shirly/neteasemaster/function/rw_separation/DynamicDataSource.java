package com.shirly.neteasemaster.function.rw_separation;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shirly
 * @Date 2020/1/16 15:55
 * @Description 获取数据源，用于动态切换数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    // 写数据源 3307
    private Object writeDataSource;

    // 读数据源 3308
    private Object readDataSource;

    @Override
    public void afterPropertiesSet() {
        if (this.writeDataSource == null) {
            throw new IllegalArgumentException("Property 'writeDataSource' is required");
        }
        // 覆盖父类中的默认数据源为write
        setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceGlobal.WRITE.name(), writeDataSource);
        if (readDataSource != null) {
            // 如果读数据源不为空就压入map
            targetDataSources.put(DynamicDataSourceGlobal.READ.name(), readDataSource);
        }
        // 设置目标数据源为map中压入的数据源
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * 读取与数据源相关的key
     * 在通过determineTargetDataSource获取目标数据源时使用
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 使用DynamicDataSourceHolder保证线程安全，并且得到当前线程中的数据源可以
        DynamicDataSourceGlobal dataSourceGlobal = DynamicDataSourceHolder.getDataSource(); // 其值通过mybatis插件sql语句的值
        if (dataSourceGlobal == null || dataSourceGlobal == DynamicDataSourceGlobal.WRITE) {
            return DynamicDataSourceGlobal.WRITE.name();
        }
        return DynamicDataSourceGlobal.READ.name();
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public void setReadDataSource(Object readDataSource) {
        this.readDataSource = readDataSource;
    }
}
