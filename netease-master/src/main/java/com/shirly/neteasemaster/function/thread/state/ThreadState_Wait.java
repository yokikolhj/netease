package com.shirly.neteasemaster.function.thread.state;

/**
 * @Author shirly
 * @Date 2020/2/5 16:28
 * @Description 运行线程wait时状态
 */
public class ThreadState_Wait {

    public static void main(String[] args) throws Throwable {
        Object object = new Object(); // 当锁用
        Thread t1 = new Thread(() -> {
            synchronized (object) {
                try {
                    System.out.println("t1将wait(3000L)...");
                    object.wait(3000L);
                    System.out.println("t1将wait()...");
                    object.wait();
                    System.out.println("t1执行完");
                } catch (Exception e) {}
            }
        });

        t1.start();
        Thread.sleep(1000L);

        synchronized (object) {
            System.out.println("t1的状态：" + t1.getState());
            object.notify();
            Thread.sleep(1000L);
            System.out.println("t1的状态：" + t1.getState());
        }
        Thread.sleep(3000L);
        System.out.println("t1的状态：" + t1.getState());

        Thread.sleep(1000L);
        synchronized (object) {
            object.notify();
        }
        System.out.println("t1的状态：" + t1.getState());
        Thread.sleep(1000L);
        System.out.println("t1的状态：" + t1.getState());
    }
}
