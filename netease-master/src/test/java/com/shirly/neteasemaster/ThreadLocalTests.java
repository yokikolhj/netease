package com.shirly.neteasemaster;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/10 16:29
 */
public class ThreadLocalTests {
    static ThreadLocal<String> value = new ThreadLocal<>(); //一个变量，感觉应该是多线程共享同一个变量

    public static void main(String[] args) throws InterruptedException {
        value.set("Hello shirly");
        System.out.println(Thread.currentThread() + "- (1)获取到value的值为：" + value.get());

        new Thread(() -> {
            System.out.println(Thread.currentThread() + "- (2)获取到value的值为：" + value.get());
        }).start();
        Thread.sleep(2000);
    }
}
