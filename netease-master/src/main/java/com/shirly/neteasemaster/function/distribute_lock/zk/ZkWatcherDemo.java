package com.shirly.neteasemaster.function.distribute_lock.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/30 10:47
 * @description 描述
 */
public class ZkWatcherDemo {
    public static void main(String[] args) {
        ZkClient client = new ZkClient("localhost:2181");
        client.setZkSerializer(new MyZkSerializer());

        client.subscribeDataChanges("/shirly/a", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("节点被修改了");
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("节点被删除了");
            }
        });

        try {
            Thread.sleep(1000 * 60 * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
