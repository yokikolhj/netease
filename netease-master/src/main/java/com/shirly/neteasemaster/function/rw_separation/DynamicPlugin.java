package com.shirly.neteasemaster.function.rw_separation;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author shirly
 * @Date 2020/1/16 17:18
 * @Description Mybatis插件，通过拦截Executor
 */
@Intercepts({ // mybatis执行流程
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class DynamicPlugin implements Interceptor {

    private  static final String REGEX = ".*insert$|.*delete$|.*update$";

    private static final Map<String, DynamicDataSourceGlobal> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 如果拥有事务上下文，则将连接绑定到事务上下文中
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement ms = (MappedStatement) objects[0]; //SQL语句第一个参数
            DynamicDataSourceGlobal dynamicDataSourceGlobal;
            if ((dynamicDataSourceGlobal = cacheMap.get(ms.getId())) == null) {
                // 读方法
                if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    // !selectKey 为自增id查询主键（SELECT LAST_INSERT_ID()）方法，使用主库
                    if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                    } else {
                        BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);

                        String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                        if (sql.matches(REGEX)) {
                            dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                        } else {
                            // 负载均衡，多个读库
                            dynamicDataSourceGlobal = DynamicDataSourceGlobal.READ;
                        }
                    }
                } else {
                    // 写方法
                    dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                }
                System.out.println("设置方法[{" + ms.getId() + "}] use [{" + dynamicDataSourceGlobal.name()
                        + "}] Strategy,SqlCommandType [{" + ms.getSqlCommandType().name() + "}]");
                cacheMap.put(ms.getId(), dynamicDataSourceGlobal);
            }
            // 设置当前线程使用的数据源
            DynamicDataSourceHolder.putDataSource(dynamicDataSourceGlobal);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
