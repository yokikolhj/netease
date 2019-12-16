package com.shirly.neteasemaster.function.limiter;

import com.shirly.neteasemaster.unit.FileUtil;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/16 17:13
 * @description redis实现令牌桶算法
 */
public class JedisRateLimiterSetter {

    private String luaScript;

    private Timer timer;

    private final Jedis jedis = new Jedis("localhost", 6379);

    public JedisRateLimiterSetter(String key, String limit) {
        super();
        try {
            String filePath = ResourceUtils.getFile("classpath:lua/limiter/rateLimitSet.lua").getPath();
            luaScript = FileUtil.getFileContent(new File(filePath), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        timer = new Timer();

        // 放入令牌的时间间隔
        long period =1000L /Long.valueOf(limit);
        // 通过定时器，定时放入令牌
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis() + " 放入令牌： " +
                        (Long) jedis.eval(luaScript, 1, key, limit));
            }
        }, period, period);
    }

    public void close() {
        this.jedis.close();
        this.timer.cancel();
    }

    public static void main(String[] args) throws Exception {
        JedisRateLimiterSetter jrls = new JedisRateLimiterSetter("shirly", "5");
        TimeUnit.SECONDS.sleep(10L);
        jrls.close();
    }
}
