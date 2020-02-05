package com.shirly.neteasemaster.function.thread.communication;

/**
 * @Author shirly
 * @Date 2020/2/5 22:52
 * @Description 2个线程依次输出1到10
 */
public class WaitNotify2 {

    static int i = 0;
    static Object object = new Object();

    public static void main(String[] args) throws Throwable {
        new Thread(() -> {
            synchronized (object) { // 锁有一个等待队列
                while (i < 10) {
                    System.out.println("t1: " + (++i));
                    object.notify(); // 把锁的其他阻塞线程唤醒
                    try {
                        object.wait(); // 把锁释放，t2线程抢到锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notify(); // 这很重要，如果最后没有唤醒，可能程序会一直处于等待状态
            }
        }).start();

        new Thread(() -> {
            synchronized (object) {
                while (i < 10) {
                    System.out.println("  t2: " + (++i));
                    object.notify();
                    try {
                        object.wait();// 把锁释放，t1线程抢到锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notify(); // 这很重要
            }
        }).start();
    }

}
