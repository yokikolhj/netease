package com.shirly.neteasemaster.function.thread.state;

/**
 * @Author shirly
 * @Date 2020/2/5 16:28
 * @Description 运行线程sleep时状态
 */
public class ThreadState_Sleep {

    static volatile boolean running = true;

    public static void main(String[] args) throws Throwable {
        Thread t1 = new Thread(() -> {
            try {
                while (running) {}
                System.out.println("t1 running is false,t1将sleep了");
                Thread.sleep(10000L);
            } catch (Exception e) {}
        });
        System.out.println("new t1 的状态：" + t1.getState());
        t1.start();
        Thread.sleep(2000L);
        System.out.println("t1.start()后的状态：" + t1.getState());

        // 让子线程退出循环
        running = false;
        Thread.sleep(2000L);
        System.out.println("t1.sleep()后的状态：" + t1.getState());
    }
}
