package com.shirly.neteasemaster.function.redis;

import java.io.IOException;
import java.net.Socket;

/**
 * @author shirly
 * @version 1.0
 * @date 2020/1/2 10:57
 * @description 描述redis做底层优化
 *     写一个简单的redis，把jedis、redisson代替掉
 */
public class RedisClient {

    Socket connection;

    public RedisClient(String host, int port) throws IOException {
        connection = new Socket("127.0.0.1", 6379);
    }

    // 获取key
    public String get(String key) throws IOException {
        // 0.命令的内容
//        *<number of arguments> CR LF           参数数量(分为几个部分)
//        $<number of bytes of argument 1> CR LF 第一个参数长度
//        <arguments data> CR LF                 第一个参数的值
//        ...
//        $<number of bytes of argument N> CR LF 第N个参数长度
//        <arguments data> CR LF                 第N个参数的值

        StringBuffer command = new StringBuffer();
        command.append("*2").append("\r\n"); // 分为几个部分
        command.append("$3").append("\r\n"); // 第一个参数长度
        command.append("GET").append("\r\n"); // 第一个参数的值
        command.append("$").append(key.getBytes().length).append("\r\n"); // 第二个参数长度
        command.append(key).append("\r\n"); // 第二个参数的值
        System.out.println(command.toString());

        // 1.告诉redis执行某一条命令
        connection.getOutputStream().write(command.toString().getBytes());
        // 2.获取到redis执行结果
        byte[] response = new byte[1024];
        connection.getInputStream().read(response);
        return new String(response);
    }

    public static void main(String[] args) throws IOException {

        RedisClient jedis = new RedisClient("127.0.0.1", 6379);
        String value = jedis.get("shirly");
        System.out.println("shirly：" + value);

        // redisTemplate底层还是会去集成各种redis客户端jedis、redisson
        /*Jedis jedis = new Jedis("127.0.0.1", 6379); // 这么一段代码，每秒做到十几万
        String value = jedis.get("netease:function:order:id");
        System.out.println("netease:function:order:id：" + value);*/

        // TCP连接-JDK操作Socket api-java程序和redis建立了连接
//        Socket socket = new Socket("127.0.0.1", 6379);
    }
}
