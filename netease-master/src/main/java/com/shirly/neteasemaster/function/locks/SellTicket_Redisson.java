package com.shirly.neteasemaster.function.locks;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/7 23:11
 * @description 售票 模拟多线程下资源竞争
 * redisson接口
 */
public class SellTicket_Redisson implements Runnable { // extends thread

    private volatile int tickets = 100;

    RLock lock = getRedissionLock();

    private RLock getRedissionLock() {
        Config config = new Config();
//        config.useClusterServers().addNodeAddress("redis://127.0.0.1:6379");
        config.useSingleServer().setAddress("redis://localhost:6379");
        Redisson redisson = (Redisson) Redisson.create(config);
        RLock lock = redisson.getLock("shirly");
        return lock;
    }

    @Override
    public void run() {
        while (tickets > 0) {
            lock.lock(); // 开启锁
            try {
                if (tickets > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "正在出售第 " + tickets-- +" 张票");
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        SellTicket_Redisson sellTicket = new SellTicket_Redisson();
        for (int i = 1; i < 6; i++) {
            new Thread(sellTicket, "窗口" + i).start();
        }
    }
}
