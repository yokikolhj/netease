package com.shirly.neteasemaster.function.cas;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author shirly
 * @Date 2020/2/7 17:07
 * @Description 利用CAS实现锁
 */
public class MyLock implements Lock {

    // 锁的拥有者
    AtomicReference<Thread> owner = new AtomicReference<>();

    // 等待队列
    private LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    @Override
    public void lock() {
        if (!tryLock()) {
            // 进入等待队列
            waiters.offer(Thread.currentThread());

            // 伪唤醒
            for(;;) {
                // 只有队列头线程才会被唤醒
                Thread head = waiters.peek(); // 查看等待队里中头部的线程
                if (head == Thread.currentThread()) {
                    if (!tryLock()) {
                        LockSupport.park(); // 挂起当前线程
                    } else {
                        // 抢到锁之后退出循环
                        waiters.poll(); // 移除线程
                        return;
                    }
                } else {
                    LockSupport.park(); // 挂起当前线程
                }
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() { // 尝试去拿锁
        return owner.compareAndSet(null, Thread.currentThread());
    }

    /**
     * 添加一个尝试去解锁的方法
     * @return
     */
    private boolean tryUnlock() {
        return owner.compareAndSet(Thread.currentThread(), null);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        if (tryUnlock()) {
            Thread th = waiters.peek(); // 查看等待队里中头部的线程
            if (th != null) {
                // 唤醒等待队列中头部线程
                LockSupport.unpark(th);
            }
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }


}
