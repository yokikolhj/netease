package com.shirly.neteasemaster.function.orderId_generate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/11 10:35
 */
@Repository("redisOrderServiceImpl")
public class RedisOrderServiceImpl implements IOrderService {

    @Autowired
    private RedisUtils redis;

    @Override
    public void orderId() {
        String key = "netease:function:order:id"; // key=系统名+模块+功能+key
        String prefix = getPrefix(new Date());
        long id = redis.getIncr(key, -1); // -1代表永远不失效
        // 加工
        System.out.println("id:" + prefix + String.format("%1$05d", id));
    }

    /**
     * 通过时间生成前缀，可根据业务自定义
     * @param date 日期
     * @return
     */
    private String getPrefix(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String dayFmt = String.format("%1$03d", day); // 3位，不足补0
        String hourFmt = String.format("%1$02d", hour);
        return (year - 2000) + dayFmt + hourFmt;
    }
}
