package com.shirly.neteasemaster.function.rw_separation;

/**
 * @Author shirly
 * @Date 2020/1/16 16:50
 * @Description 使用threadLocal技术来记录当前线程中的数据源key
 */
public class DynamicDataSourceHolder {

    // 使用ThreadLocal记录当前线程中的数据源key
    private static final ThreadLocal<DynamicDataSourceGlobal> holder = new ThreadLocal<>();

    /**
     * 设置数据源
     */
    public static void putDataSource(DynamicDataSourceGlobal dataSource) {
        holder.set(dataSource);
    }

    /**
     * 获取数据源
     * @return
     */
    public static DynamicDataSourceGlobal getDataSource() {
        return holder.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        holder.remove();
    }

}
