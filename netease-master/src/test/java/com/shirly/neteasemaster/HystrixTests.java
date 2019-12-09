package com.shirly.neteasemaster;

import com.shirly.neteasemaster.function.hystrix.CommodityService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/9 16:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HystrixTests {
    private long timed;
    private static final int THREAD_NUM = 1000;
    // 到计数器
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

    @Autowired
    CommodityService commodityService;

    @Before
    public void start() {
        System.out.println("开始测试");
        timed = System.currentTimeMillis();
    }
    @After
    public void end() {
        System.out.println("结束测试，执行时长：" + (System.currentTimeMillis()-timed));
    }

    @Test
    @ConditionalOnMissingClass("com.shirly.neteasemaster.function.hystrix.CommodityService")
    public void benchmark() throws IOException {
        // 创建 并不是马上发起请求
        for (int i = 0; i < THREAD_NUM; i++) {
            final String code = "code_" + (i + 1);
            Thread thread = new Thread(() -> {
                try {
                    // 一直等待，直到countDownLatch为0，代表所有线程都start，在运行后续代码
                    countDownLatch.await();
                    // http请求，实际上是多线程调用这个方法
                    Map<String, Object> result = commodityService.queryCommodity(code);
                    System.out.println(Thread.currentThread().getName() + " 查询结束，结果：" + result);
                } catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " 线程执行出现问题");
                }
            });
            thread.setName("price-thread-" + code);
            thread.start();
            // 启动后，倒计时器计数减一，代表又一个线程就绪了
            countDownLatch.countDown();
        }

        // 输出任意内容退出
        System.in.read();
    }

}
