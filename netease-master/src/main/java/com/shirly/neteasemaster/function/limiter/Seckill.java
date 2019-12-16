package com.shirly.neteasemaster.function.limiter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/13 15:22
 * @description 秒杀限流
 */
public class Seckill {

    private final long LIMIT_NUM = 5;

    private AtomicLong count = new AtomicLong(0L); //CAS + volatile

    /**
     * 计数器限流
     * @param name 商品名
     * @return
     */
    public String doOrderByCounter(String name) {
        long c = count.incrementAndGet();
        if (c > LIMIT_NUM) {
            return "秒杀结束，谢谢参与！count=" + count;
        }
        return "恭喜，秒杀成功！count=" + count;
    }

    /**
     * redis限流
     * @param name 商品名
     * @return
     */
    public String doOrderByRedis(String name) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long c = jedis.incr(name);
            if (c > LIMIT_NUM) {
                return "秒杀结束，谢谢参与！count=" + count;
            }
            return "恭喜，秒杀成功！count=" + count;
        }
    }

    /**
     * redis限流 - 单位时间的请求次数
     * 多线程下会出错，用doOrderByRedisExpireBetter方法
     * @param name 商品名
     * @return
     */
    public String doOrderByRedisExpire(String name) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long c = jedis.incr(name);
            if (c > LIMIT_NUM) {
                return System.currentTimeMillis() / 1000 + "秒杀结束，谢谢参与！count=" + count;
            } else {
                if (c == 1) { // 第一次进来
                    // 设置过期时间
                    jedis.expire(name, 1);
                }
            }
            return System.currentTimeMillis() / 1000 + "恭喜，秒杀成功！count=" + count;
        }
    }

    JedisLuaTimeWindowLimiter jtwl = new JedisLuaTimeWindowLimiter("key", "5", "1", "timeWindowLimit.lua");

    /**
     * redis限流 - 单位时间的请求次数
     * 利用lua脚本和redis单线程模型保证两个操作的原子性
     * @param name 商品名
     * @return
     */
    public String doOrderByRedisExpireBetter(String name) {
        if (!jtwl.acquire()) {
            return System.currentTimeMillis() / 1000 + "秒杀结束，谢谢参与！count=" + count;
        }
        return System.currentTimeMillis() / 1000 + "恭喜，秒杀成功！count=" + count;
    }

    // 时间窗请求数限制 5r/s
    LoadingCache<Long, AtomicLong> twCounter = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, AtomicLong>() {
                @Override
                public AtomicLong load(Long aLong) throws Exception {
                    return new AtomicLong(0L);
                }
            });

    TwLimiter twLimiter = new TwLimiter(2L); // 自动移除2秒前的计数

    /**
     * 时间窗请求数限制
     * @return
     * @throws Exception
     */
    public String doOrderByTimeWindows() throws Exception {
        long currentTime = System.currentTimeMillis() / 1000L;

//        long c = twLimiter.get(currentTime).incrementAndGet();
        long c = twCounter.get(currentTime).incrementAndGet();
        if (c > LIMIT_NUM) {
            return "系统正忙，请重试！count=" + currentTime;
        } else {
            return "您的年度消费统计！count=" + currentTime;
        }
    }

    // 平滑限流请求-Guava RateLimit
//    RateLimiter rateLimiter = RateLimiter.create(5); // 5r/s
    MyRateLimiter rateLimiter = new MyRateLimiter(5);

    /**
     * 单机环境下平滑限流
     * @return
     */
    public String doOrderByRateLimit() {
        if (rateLimiter.tryAcquire()) {
            return "您的年度消费统计！count=" + System.currentTimeMillis() / 1000L;
        } else {
            return "系统正忙，请重试！count=" + System.currentTimeMillis() / 1000L;
        }
    }

}
