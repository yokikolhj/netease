package com.shirly.neteasemaster.function.thread.communication;

/**
 * @Author shirly
 * @Date 2020/2/5 22:52
 * @Description 3个线程依次输出1到20
 */
public class WaitNotify3 {

    static int i = 0;
    static Object object = new Object();
    static int limit = 20;

    public static void main(String[] args) throws Throwable {
        new Thread(() -> {
            synchronized (object) { // 锁有一个等待队列
                while (i < limit) {
                    if (i % 3 == 0) {
                        System.out.println("t1: " + (++i));
                    }
                    object.notifyAll(); // 把锁的其他阻塞线程唤醒
                    try {
                        object.wait(); // 把锁释放，另外两个线程抢锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notifyAll(); // 这很重要，如果最后没有唤醒，可能程序会一直处于等待状态
            }
        }).start();

        new Thread(() -> {
            synchronized (object) {
                while (i < limit) {
                    if (i % 3 == 1) {
                        System.out.println("  t2: " + (++i));
                    }
                    object.notifyAll(); // 把锁的其他阻塞线程唤醒
                    try {
                        object.wait(); // 把锁释放，另外两个线程抢锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notifyAll(); // 这很重要，如果最后没有唤醒，可能程序会一直处于等待状态
            }
        }).start();

        new Thread(() -> {
            synchronized (object) {
                while (i < limit) {
                    if (i % 3 == 2) {
                        System.out.println("    t3: " + (++i));
                    }
                    object.notifyAll(); // 把锁的其他阻塞线程唤醒
                    try {
                        object.wait(); // 把锁释放，另外两个线程抢锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                object.notifyAll(); // 这很重要，如果最后没有唤醒，可能程序会一直处于等待状态
            }
        }).start();
    }

}
