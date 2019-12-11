package com.shirly.neteasemaster.unit;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
* @author shirly
* @date 2019年8月5日 下午5:50:02
* @description redis工具包
*/
public class RedisUtil {
	
	//被volatile修饰的变量不会被本地线程缓存，对该变量的读写都是直接操作共享内存。
	private static volatile JedisPool jedisPool = null;
	
	public static JedisPool getPoolInstance() {
		if (null == jedisPool) {
			synchronized(JedisPool.class) {
				if (null == jedisPool) {
					System.out.println("创建jedisPool");
					JedisPoolConfig poolConfig = new JedisPoolConfig();
					 poolConfig.setMaxTotal(1000);
			         poolConfig.setMaxIdle(32);
			         poolConfig.setMaxWaitMillis(100*1000);
			         poolConfig.setTestOnBorrow(true);
					jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);
				}
			}
		}
		return jedisPool;
	}
	
	/**
	 * 用完Jedis实例需要返还给JedisPool
	 * @param jedis 实例
	 */
	public static void release(Jedis jedis) {
		if (null != jedis) {
			jedis.close();
		}
	}

	/**
	 * 获取jedis实例
	 * @return jedis实例
	 */
	public static Jedis getJedis() {
		jedisPool = getPoolInstance();
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
}