package com.shirly.neteasemaster.function.orderId_generate;

import com.shirly.neteasemaster.unit.RedisUtil;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/11 11:26
 */
@Component
public class RedisUtils {

    public Long getIncr(String key, int timeout) {
        Jedis redis = null;
        try {
            redis = RedisUtil.getJedis();
//            System.out.println("redis:" + redis);
            long id = redis.incr(key);
            if (timeout > 0) {
                redis.expire(key, timeout);
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtil.release(redis);
        }
        return null;
    }

}
