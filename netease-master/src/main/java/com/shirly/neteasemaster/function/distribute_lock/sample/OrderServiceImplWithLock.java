package com.shirly.neteasemaster.function.distribute_lock.sample;

import com.shirly.neteasemaster.function.distribute_lock.zk.ZkDistributeLock;
import com.shirly.neteasemaster.function.distribute_lock.zk.ZkImproveDistributeLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/27 15:11
 * @description 描述
 */
public class OrderServiceImplWithLock implements OrderService {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderServiceImplWithLock.class);

    private static OrderCodeGenerator codeGenerator = new OrderCodeGenerator();

//    private Lock lock = new ReentrantLock();

    @Override
    public void createOrder() {
        String orderCode = null;
        // 用户分布式锁
//        Lock lock = new ZkDistributeLock("/shirly/test_lock");
        Lock lock = new ZkImproveDistributeLock("/shirly/test_lock1");
        try {
            // 每个服务一把锁，分布式服务不能做到唯一性
//            lock.lock();
            lock.lock();
            // 获取订单编号
            orderCode = codeGenerator.getOrderCode();
        } finally {
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getName() + "=========>" + orderCode);
//        LOGGER.info(Thread.currentThread().getName() + "=========>" + orderCode);
    }
}
