package com.shirly.neteasemaster.function.thread.state;

/**
 * @Author shirly
 * @Date 2020/2/5 16:28
 * @Description 运行线程join时状态
 *     join实现线程之间的等待
 */
public class ThreadState_Join {

    public static void main(String[] args) throws Throwable {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(10000L);
            } catch (Exception e) {}
        });
        Thread t2 = new Thread(() -> {
            try {
                System.out.println("t2中执行ti.join(5000L)...");
                t1.join(5000L); // t2等待t1 5秒之后再执行
                System.out.println("t2中执行ti.join()...");
                t1.join(); // t2等待t1执行完
                System.out.println("t2执行完");
            } catch (Exception e) {}
        });

        t1.start();
        t2.start();
        Thread.sleep(1000L);
        System.out.println("t2的状态：" + t2.getState());

        Thread.sleep(5000L);
        System.out.println("t2的状态：" + t2.getState());
    }
}
