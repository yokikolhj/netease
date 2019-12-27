package com.shirly.neteasemaster;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/26 16:45
 * @description zookeeper测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperTests {

    @Value("${config.zookeeper.url}")
    String zkUrl;

    @Value("${config.zookeeper.nodename}")
    String nodeName;

    @Test
    public void zk() throws Exception {
        CuratorFramework zkClient = new CuratorFrameworkFactory.newClient(zkUrl, new RetryOneTime(1000));
        zkClient.start(); // 启动和zookeeper的连接

        // 获取节点对应的值
        byte[] bytes = zkClient.getData().forPath("/" + nodeName + "/pay.alipay.url");
        System.out.println("/pay.alipay.url对应的值为：" + new String(bytes));

        // 获取节点下的子节点值，每一个子节点代表一项配置
        List<String> strings = zkClient.getChildren().forPath("/" + nodeName);
        System.out.println("pay-server-config节点下有这些子节点");
        /*strings.forEach(s -> {
            System.out.println(s);
        });*/
        strings.forEach(System.out::println);
    }
}
