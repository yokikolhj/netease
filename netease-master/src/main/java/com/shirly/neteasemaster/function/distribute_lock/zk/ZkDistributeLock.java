package com.shirly.neteasemaster.function.distribute_lock.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/30 11:45
 * @description 基于zookeeper临时节点的分布式锁
 */
public class ZkDistributeLock implements Lock {

    Logger logger = LoggerFactory.getLogger(ZkDistributeLock.class);

    private String lockPath;

    private ZkClient client;

    public ZkDistributeLock(String lockPath) {
        super();
        this.lockPath = lockPath;
        this.client = new ZkClient("localhost:2181");
        client.setZkSerializer(new MyZkSerializer());
    }

    // 不会阻塞
    @Override
    public boolean tryLock() {
        try {
            // 加锁-创建节点
            client.createEphemeral(lockPath);
        } catch (ZkNodeExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public void unlock() {
        // 删除节点
        client.delete(lockPath);
    }

    // 如果获取不到锁，阻塞等待
    @Override
    public void lock() {
        if (!tryLock()) {
            // 阻塞自己
            waitForLock();
            // 再次尝试-这种方法会出现惊群效应
            lock();
        }
    }

    private void waitForLock() {
        CountDownLatch cdl = new CountDownLatch(1);
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
//                System.out.println("节点被修改了");
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("节点被删除了");
//                logger.debug("节点被删除了");
                cdl.countDown();
            }
        };

        // 注册watcher
        client.subscribeDataChanges(lockPath, listener);

        // 阻塞自己
        if (client.exists(lockPath)) {
            try {
                cdl.await();
            } catch (InterruptedException e) {

            }
        }
        // 唤醒之后，取消watcher注册
        client.unsubscribeDataChanges(lockPath, listener);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
