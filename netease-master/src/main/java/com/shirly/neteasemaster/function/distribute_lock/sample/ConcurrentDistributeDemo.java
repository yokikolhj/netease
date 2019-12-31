package com.shirly.neteasemaster.function.distribute_lock.sample;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/27 15:02
 * @description 集群高并发
 */
public class ConcurrentDistributeDemo {

    public static void main(String[] args) {

        // 并发数
        int currency = 50;

        // 循环屏障
        CyclicBarrier barrier = new CyclicBarrier(currency);

        // 多线程模拟高并发
        for (int i = 0; i < currency; i++) {
            new Thread(() -> {
                // 模拟分布式集群场景,JVM锁不能保证唯一性，需要用分布式锁
                OrderService orderService = new OrderServiceImplWithLock();

                System.out.println(Thread.currentThread().getName() + "=========准备好了");
                // 等待一起出发
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                orderService.createOrder();
            }).start();
        }
    }
}
