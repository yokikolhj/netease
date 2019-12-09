package com.shirly.neteasemaster.function.locks;

import redis.clients.jedis.Jedis;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/8 17:04
 * @description Redis客户端相关实现
 */
public class JedisUtil {
    public static Jedis getJedis() {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        return jedis;
    }
}
