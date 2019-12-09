package com.shirly.neteasemaster.function.locks;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/7 23:11
 * @description 售票 模拟多线程下资源竞争
 * sync关键字
 */
public class SellTicket_Sync implements Runnable { // extends thread

    private volatile int tickets = 100;

    @Override
    public void run() {
        while (tickets > 0) {
            synchronized (this) { //锁代码块
                if (tickets > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "正在出售第 " + tickets-- +" 张票");
                }
            }
        }
    }

    public static void main(String[] args) {
        SellTicket_Sync sellTicket = new SellTicket_Sync();
        for (int i = 1; i < 6; i++) {
            new Thread(sellTicket, "窗口" + i).start();
        }
    }
}
