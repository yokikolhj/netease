package com.shirly.neteasemaster.function.distribute_lock.sample;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/27 15:02
 * @description 高并发
 */
public class ConcurrentDemo {

    public static void main(String[] args) {

        OrderService orderService = new OrderServiceImplWithLock();

        // 并发数
        int currency = 20;

        // 循环屏障
        CyclicBarrier barrier = new CyclicBarrier(currency);

        // 多线程模拟高并发
        for (int i = 0; i < currency; i++) {
            new Thread(() -> {
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
