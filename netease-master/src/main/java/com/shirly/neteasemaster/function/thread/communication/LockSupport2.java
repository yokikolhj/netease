package com.shirly.neteasemaster.function.thread.communication;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author shirly
 * @Date 2020/2/5 22:52
 * @Description 2个线程依次输出1到10
 */
public class LockSupport2 {

    static int i = 0;
    static Thread t1,t2;

    public static void main(String[] args) throws Throwable {
        t1 = new Thread(() -> {
            while (i < 10) {
                System.out.println("t1: " + (++i));
                LockSupport.unpark(t2); // 唤醒t2线程
                LockSupport.park(); // 自己阻塞
            }
        });

        t2 = new Thread(() -> {
            while (i < 10) {
                LockSupport.park(); // 先自己锁住，如果先执行LockSupport.unpark(t2);再马上执行这条代码也不会阻塞
                System.out.println("  t2: " + (++i));
                LockSupport.unpark(t1); // 唤醒t1线程
            }
        });
        t1.start();
        t2.start();
    }

}
