package com.shirly.neteasemaster.function.limiter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/16 16:55
 * @description 令牌桶算法
 *     单机下使用
 */
public class MyRateLimiter implements AutoCloseable {

    //
    private Semaphore semaphore;

    private int limit;

    private Timer timer;

    public MyRateLimiter(int limit) {
        super();
        this.limit = limit;
        this.semaphore = new Semaphore(limit);
        timer = new Timer();
        // 放入令牌的时间间隔
        long period =1000L /limit;
        // 通过定时器，定时放入令牌
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (semaphore.availablePermits() < limit) {
                    semaphore.release(); // 增加一个许可证，这可能会释放一个阻塞的 acquire() 方法
                }
            }
        }, period, period);

    }

    public void acquire() throws InterruptedException {
        this.semaphore.acquire(); // 阻塞，直到有一个许可证可以获得然后拿走一个许可证
    }

    public boolean tryAcquire() {
        return this.semaphore.tryAcquire();
    }

    public int availablePermits() {
        return this.semaphore.availablePermits(); // 返回此Semaphore对象中当前可用的许可数，许可的数量有可能实时在改变，并不是固定的数量
    }

    @Override
    public void close() throws Exception {
        this.timer.cancel();
    }
}
