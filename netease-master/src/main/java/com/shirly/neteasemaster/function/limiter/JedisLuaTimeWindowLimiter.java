package com.shirly.neteasemaster.function.limiter;

import com.shirly.neteasemaster.unit.FileUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.nio.file.Files;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/13 16:55
 * @description 描述
 */
public class JedisLuaTimeWindowLimiter {

    private String luaScript;
    private String key;
    private String limit;
    private String expire;

    public JedisLuaTimeWindowLimiter(String key, String limit, String expire, String scriptFile) {
        super();
        this.key = key;
        this.limit = limit;
        this.expire = expire;
        try {
            String filePath = ResourceUtils.getFile("classpath:lua/timeWindowLimit.lua").getPath();
            luaScript = FileUtil.getFileContent(new File(filePath), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean acquire() {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            return (Long) jedis.eval(luaScript, 1, key, limit, expire) == 1L;
        }
    }
}
