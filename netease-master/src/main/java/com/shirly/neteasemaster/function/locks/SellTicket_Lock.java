package com.shirly.neteasemaster.function.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/7 23:11
 * @description 售票 模拟多线程下资源竞争
 * Lock接口
 */
public class SellTicket_Lock implements Runnable { // extends thread

    private volatile int tickets = 100;

    Lock lock = new ReentrantLock(); // 可重入

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
        SellTicket_Lock sellTicket = new SellTicket_Lock();
        for (int i = 1; i < 6; i++) {
            new Thread(sellTicket, "窗口" + i).start();
        }
    }
}
