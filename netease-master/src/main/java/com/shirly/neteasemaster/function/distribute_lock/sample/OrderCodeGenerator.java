package com.shirly.neteasemaster.function.distribute_lock.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/27 15:05
 * @description 订单编号生成类
 */
public class OrderCodeGenerator {
    // 自增序列
    private int i = 0;
    // 按照“年-月-日-时-分-秒-自增序列”规则生成订单编号
    String getOrderCode() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        return sdf.format(now) + ++i;
    }
}
