package com.shirly.neteasemaster.function.limiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/16 11:22
 * @description 时间窗请求数限流
 */
public class TwLimiter {

    // key-时间，value-时间计数
    private Map<Long, AtomicLong> map = new HashMap<>();

    private long before;

    TwLimiter(long before) {
        super();
        this.before = before;
    }

    public AtomicLong get(Long key) {
        if (!this.map.containsKey(key)) {
            synchronized (map) {
                if (!this.map.containsKey(key)) {
                    map.put(key, new AtomicLong(0L));
                    // 移除指定秒数之前的计数
                    this.removeBefore(key);
                }
            }
        }
        return this.map.get(key);
    }

    private void removeBefore(Long cKey) {
        for (Long key : this.map.keySet()) {
            if (key + before < cKey) {
                this.map.remove(key);
            }
        }
    }
}
