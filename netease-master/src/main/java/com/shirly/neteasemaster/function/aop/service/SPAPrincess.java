package com.shirly.neteasemaster.function.aop.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author shirly
 * @Date 2020/1/21 14:05
 * @Description
 */
@Service
public class SPAPrincess implements SPAService {

    private Random random = new Random();

    private int bound = 5;

    @Override
    public void fullBodyMassage(String customer) {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完fullBodyMassage服务！");
    }

    @Override
    public void aromaOilMassage(String customer) {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完aromaOilMassage服务！");
    }

    @Override
    public void rest() {

    }
}
