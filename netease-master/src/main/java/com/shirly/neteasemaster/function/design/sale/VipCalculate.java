package com.shirly.neteasemaster.function.design.sale;

import org.springframework.stereotype.Service;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 15:35
 * @description VIP打折 - 策略模式
 */
@Service
public class VipCalculate implements Calculate {
    @Override
    public String userType() {
        return "vip";
    }

    @Override
    public double discount(double fee) {
        return fee * 0.8;
    }
}
