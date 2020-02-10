package com.shirly.neteasemaster.function.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/26 16:03
 * @description zookeeper之配置中心
 *     数据库配置信息
 *     把配置信息放到数据库表
 */
@Component
public class SystemConfigUtil {

    private Properties remoteProperties = new Properties(); // 本质就是hashtable，缓存远程服务器的配置信息

    // 保证统一性
    @Autowired
    private Environment environment; // 操作系统、jvm...配置参数都可以通过它获取

    // 获取配置项的值
    public String getProperties(String key) {
        // Refresh Bean
        if (remoteProperties.getProperty(key) != null) {
            return remoteProperties.getProperty(key);
        }
        // 本地配置信息
        return environment.getProperty(key);
    }

    @Value("${config.zookeeper.url}")
    String zkUrl;

    @Value("${config.zookeeper.nodename}")
    String nodeName;

    // 启动系统的时候读取远程配置中心中的系统配置信息表
//    @PostConstruct -- 使用注解开启
    public void init() {
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkUrl, new RetryOneTime(1000));
        zkClient.start(); // 启动和zookeeper的连接

        try {
            // 获取节点下的子节点值，每一个子节点代表一项配置
            List<String> configNames = zkClient.getChildren().forPath("/" + nodeName);
            System.out.println("pay-server-config节点下有这些子节点");
            for (String configName : configNames) {
                System.out.println("配置名：" + configName);
                String value = new String(zkClient.getData().forPath("/" + nodeName + configName));
                remoteProperties.put(configName, value);
            }

            // 用watch功能监听节点是否更改
            // NIO长连接底层数据网络编程技术
            TreeCache treeCache = new TreeCache(zkClient, "/" + nodeName);
            treeCache.start();
            treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
                System.out.println("数据有变化");
                if (treeCacheEvent.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
                    // 包含具体那一项配置发生变更
                    String configName = treeCacheEvent.getData().getPath().replace("/" + nodeName + "/", "");
                    System.out.println(configName + "数据修改");
                    String value = new String(treeCacheEvent.getData().getData());
                    remoteProperties.put(configName, value); // 更新本地配置项
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*//TODO 读取数据库表
        //TODO 保存到java内存的remoteProperties对象中
        //问题： 配置更新时如何知晓？轮询？*/
    }
}
