package com.shirly.neteasemaster.function.distribute_lock.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/30 11:45
 * @description 基于zookeeper临时顺序节点的分布式锁
 */
public class ZkImproveDistributeLock implements Lock {

    /*
    * 利用临时顺序节点来实现分布式锁
    * 获取锁：取排队号（创建自己的临时顺序节点），然后判断自己是否是最小号，如果是，则获得锁
    * 释放锁：删除自己创建的临时顺序节点
    * */

    private String lockPath;

    private ZkClient client;

    private String currentPath;

    private String beforePath;

    public ZkImproveDistributeLock(String lockPath) {
        super();
        this.lockPath = lockPath;
        this.client = new ZkClient("localhost:2181");
        client.setZkSerializer(new MyZkSerializer());
        if (!this.client.exists(lockPath)) {
            try {
                // 创建临时顺序节点
                this.client.createPersistent(lockPath);
            } catch (ZkNodeExistsException e) {

            }
        }
    }

    // 不会阻塞
    @Override
    public boolean tryLock() {
        if (currentPath == null) {
            // 创建临时顺序节点
            currentPath = client.createEphemeralSequential(lockPath + "/", "aaa");
        }
        // 获得所有的子节点
        List<String> children = client.getChildren(lockPath);
        // 排序list
        Collections.sort(children);

        // 判断当前节点是否是最小的
        if (currentPath.equals(lockPath + "/" + children.get(0))) {
            return true;
        } else {
            // 取到前一个
            // 得到字节的索引号
            int curIndex = children.indexOf(currentPath.substring(lockPath.length() + 1));
            beforePath = lockPath + "/" + children.get(curIndex - 1);
        }
        return false;
    }

    @Override
    public void unlock() {
        // 删除节点
        client.delete(currentPath);
    }

    // 如果获取不到锁，阻塞等待
    @Override
    public void lock() {
        if (!tryLock()) {
            // 阻塞自己
            waitForLock();
            // 再次尝试
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
        client.subscribeDataChanges(beforePath, listener);

        // 阻塞自己
        if (client.exists(beforePath)) {
            try {
                cdl.await();
            } catch (InterruptedException e) {

            }
        }
        // 唤醒之后，取消watcher注册
        client.unsubscribeDataChanges(beforePath, listener);
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
