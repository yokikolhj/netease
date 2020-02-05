package com.shirly.neteasemaster.function.thread.communication;

/**
 * @Author shirly
 * @Date 2020/2/5 22:52
 * @Description 2个线程依次输出1到10
 */
public class CAS2 {

    static volatile int i = 0;
    static volatile int t = 1;

    public static void main(String[] args) throws Throwable {
        new Thread(() -> {
            while (i < 10) {
                // 自旋
                while (t != 1) {}
                System.out.println("t1: " + (++i));
                t = 2;
            }
        }).start();

        new Thread(() -> {
            while (i < 10) {
                while (t != 2) {}
                System.out.println("  t2: " + (++i));
                t = 1;
            }
        }).start();
    }

}
