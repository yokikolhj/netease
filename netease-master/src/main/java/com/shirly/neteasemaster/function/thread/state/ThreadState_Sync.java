package com.shirly.neteasemaster.function.thread.state;

/**
 * @Author shirly
 * @Date 2020/2/5 16:28
 * @Description 运行线程抢不到锁时状态
 */
public class ThreadState_Sync {

    public static void main(String[] args) throws Throwable {
        Thread t1 = new Thread(() -> {
            synchronized (ThreadState_Sync.class) {
                System.out.println("t1抢到锁");
            }
        });

        synchronized (ThreadState_Sync.class) {
            t1.start();
            Thread.sleep(1000L);
            System.out.println("t1抢不到锁时的状态：" + t1.getState());
        }
    }
}
