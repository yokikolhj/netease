package com.shirly.neteasemaster;

import com.shirly.neteasemaster.function.orderId_generate.IOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/11 10:36
 * @description 生成订单测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTests {
    private static final int THREAD_NUM = 100;

    private static CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

    @Autowired
    @Qualifier("redisOrderServiceImpl")
    private IOrderService orderService;

    @Test
    public void test() throws InterruptedException {
        System.out.println("测试开始");
        for (int i = 0; i < THREAD_NUM; i++) {
            new Thread(() -> {
                try {
                    // 一直等待，直到countDownLatch为0，代表所有线程都start，在运行后续代码
                    countDownLatch.await();
                    orderService.orderId();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(Thread.currentThread().getName() + " 线程执行出现问题");
                }
            }).start();
            // 启动后，倒计时器计数减一，代表又一个线程就绪了
            countDownLatch.countDown();
        }
        Thread.sleep(3000);
        while (0 != countDownLatch.getCount()) {
            if (0 == countDownLatch.getCount()){
                System.out.println("测试结束");
            }
        }
    }
}
