package com.shirly.neteasemaster.function.limiter;

import com.shirly.neteasemaster.unit.FileUtil;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.File;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/16 17:13
 * @description redis实现令牌桶算法
 */
public class JedisRateLimiter {

    private String luaScript;

    private String key;

    public JedisRateLimiter(String key) {
        super();
        this.key = key;
        try {
            String filePath = ResourceUtils.getFile("classpath:lua/limiter/rateLimit.lua").getPath();
            luaScript = FileUtil.getFileContent(new File(filePath), "UTF-8");
//            System.out.println(luaScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean acquire() {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            return (Long) jedis.eval(luaScript, 1, key) == 1L;
        }
    }

    public static void main(String[] args) {
        JedisRateLimiter jedisRateLimiter = new JedisRateLimiter("shirly");
        System.out.println(jedisRateLimiter.acquire());
    }
}
