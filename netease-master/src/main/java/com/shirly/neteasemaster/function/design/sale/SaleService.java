package com.shirly.neteasemaster.function.design.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/6 15:12
 * @description 电商场景2
 */
@Service
public class SaleService {

//    @Autowired // 注入
//    VipCalculate vipCalculate;
//
//    @Autowired
//    NormalCalculate normalCalculate;
//
//    /*@Autowired
//    Calculate calculate; // 有问题：到底注入哪一个实现类？*/

    // 保存所有用户类型的打折策略
    ConcurrentHashMap<String, Calculate> calculateConcurrentHashMap = new ConcurrentHashMap<>();

    // Spring特有功能，注入某一个接口的所有实现类
    @Autowired
    public SaleService(List<Calculate> calculates) {
        for (Calculate calculate : calculates) {
            calculateConcurrentHashMap.put(calculate.userType(), calculate);
        }
    }

    // 不同用户不同折扣
    public double sale(String userType, double fee) {
        Calculate calculate = calculateConcurrentHashMap.get(userType);
        return calculate.discount(fee);

        // 这样的if else 违背了开闭原则
        /*if (vipCalculate.userType().equals(userType)) {
            // 此处的打折策略是简单实例，真实场景及其复杂
            return vipCalculate.discount(fee);
        } else if (normalCalculate.userType().equals(userType)) {
            return normalCalculate.discount(fee);
        } else {
            return fee;
        }*/

        /*if ("vip".equals(userType)) {
            // 此处的打折策略是简单实例，真实场景及其复杂
            return fee * 0.8;
        } else if ("normal".equals(userType)) {
            return fee * 0.9;
        } else {
            return fee;
        }*/
    }
}
