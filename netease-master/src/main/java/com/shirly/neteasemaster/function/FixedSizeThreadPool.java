package com.shirly.neteasemaster.function;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/2 19:58
 * @description 不用JUC的API,创建一个固定大小的线程池
 */
public class FixedSizeThreadPool {

    // 阻塞队列
    private BlockingQueue<Runnable> taskQueue;

    // 存放线程的集合
    private List<Worker> workers;

    // 线程池是否在工作
    private volatile boolean working = true;

    private FixedSizeThreadPool(int poolSize, int taskQueueSize) {
        if (poolSize <= 0 || taskQueueSize <= 0) {
            throw new IllegalArgumentException("参数错误");
        }
        // 初始化任务队列
        this.taskQueue = new LinkedBlockingDeque<>(taskQueueSize);
        // 创建线程集合
        this.workers = new ArrayList<>();
        // 初始化工作线程
        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(this);
            this.workers.add(worker);
            worker.start();
        }
    }

    // 工作线程
    private static class Worker extends Thread {
        private FixedSizeThreadPool pool;
        Worker(FixedSizeThreadPool pool) {
            super();
            this.pool = pool;
        }
        public void run() {
//            int taskCount = 0;
            while (this.pool.working || this.pool.taskQueue.size() > 0) {
                Runnable task = null;
                // 从队列中取任务执行
                try {
                    if (this.pool.working) {
                        task = this.pool.taskQueue.take(); // 阻塞
                    } else {
                        task = this.pool.taskQueue.poll(); // 无数据时返回null
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    try {
                        task.run();
                        System.out.println(Thread.currentThread().getName() + "执行完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(Thread.currentThread().getName() + "结束");
        }
    }

    // 提供任务
    private boolean submit(Runnable task) {
        return this.taskQueue.offer(task);
    }

    // 关闭线程池
    private void shutdown() {
        if (this.working) {
            this.working = false;
            // 如果工作线程处于阻塞状态，唤醒
            for (Thread thread: this.workers) {
                if (thread.getState().equals(Thread.State.BLOCKED) || thread.getState().equals(Thread.State.WAITING)) {
                    thread.interrupt(); // 中断阻塞状态
                }
            }
        }
        System.out.println("线程池关闭");
    }

    public static void main(String[] args) {
        FixedSizeThreadPool pool = new FixedSizeThreadPool(3,5);
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
               System.out.println("任务开始");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            });
        }
        // 等待其他线程执行完
        while (true) {
            if (0 == pool.taskQueue.size()) {
                pool.shutdown();
                break;
            }
        }
    }
}
