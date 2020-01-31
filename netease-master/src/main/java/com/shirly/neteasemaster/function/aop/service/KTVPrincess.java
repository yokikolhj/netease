package com.shirly.neteasemaster.function.aop.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author shirly
 * @Date 2020/1/21 14:10
 * @Description
 */
@Service
public class KTVPrincess implements KTVService {

    private Random random = new Random();

    private int bound = 5;

    @Override
    public void sing(String customer) {
        long start = System.currentTimeMillis();
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完sing服务！");
    }
}
