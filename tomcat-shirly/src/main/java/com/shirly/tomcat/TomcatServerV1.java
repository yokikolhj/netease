package com.shirly.tomcat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author shirly
 * @Date 2020/2/3 17:42
 * @Description tomcat启动服务器，获取数据http请求数据，无返回结果
 */
public class TomcatServerV1 {

    private static ExecutorService threadPool = Executors.newCachedThreadPool(); // 多线程用线程池简单示例

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080); // os->jvm->JDK API提供开发人员的
        System.out.println("tomcat 服务器启动成功");
        while (!serverSocket.isClosed()) {
            // 获取新连接（请求，响应）（浏览器/httpClient发起的请求）
            Socket socket = serverSocket.accept(); // 阻塞

            threadPool.submit(() -> {
                // 接收数据，打印
                InputStream inputStream = socket.getInputStream();
                System.out.println("收到请求： ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String msg;
                while ((msg = reader.readLine()) != null) {
                    if (msg.length() == 0) {
                        break;
                    }
                    System.out.println(msg);
                }
                System.out.println("--------------收到请求");
                socket.close();

                // 返回内容
                return null;
            });
        }
        serverSocket.close();
    }
}
