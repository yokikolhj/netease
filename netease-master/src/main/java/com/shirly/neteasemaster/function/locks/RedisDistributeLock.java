package com.shirly.neteasemaster.function.locks;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/8 0:45
 */
public class
RedisDistributeLock {
    private static final long SLEEP_PER = 5; // 获取锁时睡眠等待时间片，毫秒
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String key = "lock_key";
    private static final String value = "5000";
    private static final long expireTime = 5; // 毫秒

    /**
     * 获取分布式锁
     */
    public void lock() {
        try (Jedis jedis = JedisUtil.getJedis();) {
            while (!(tryGetDistributedLock(jedis, key, value, expireTime))) {
                Thread.sleep(SLEEP_PER);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试获取redis分布式锁
     * @param jedis Redis客户端
     * @param key 锁
     * @param value 请求标识
     * @param expireTime 超时时间
     * @return 是否成功
     */
    private boolean tryGetDistributedLock(Jedis jedis, String key, String value, long expireTime) {
        SetParams params = new SetParams();
        params.nx();
        params.px(expireTime);
        String result = jedis.set(key, value, params);
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

    public boolean tryLock() {
        try (Jedis jedis = JedisUtil.getJedis();) {
            return (tryGetDistributedLock(jedis, key, value, expireTime));
        }
    }

    public void unlock() {
        try (Jedis jedis = JedisUtil.getJedis();) {
            unLock(jedis, key, value);
        }
    }

    /**
     * 释放分布式锁
     * @param jedis
     * @param lockKey
     * @param value
     */
    private boolean unLock(Jedis jedis, String lockKey, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[2] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(value));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}
