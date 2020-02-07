package com.shirly.neteasemaster.function.cas;

import java.util.concurrent.locks.Lock;

/**
 * @Author shirly
 * @Date 2020/2/7 17:22
 * @Description 用锁实现i++
 */
public class CounterLock {

    volatile int i = 0;

    Lock lock = new MyLock();

    private void add() {
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CounterLock cl = new CounterLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    cl.add();
                }
            }).start();
        }
        Thread.sleep(6000L);
        System.out.println("cl.add():" + cl.i);
    }
}
