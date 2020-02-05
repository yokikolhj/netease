package com.shirly.neteasemaster.function.thread.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author shirly
 * @Date 2020/2/5 16:28
 * @Description 运行线程等待IO时状态
 */
public class ThreadState_IO {

    public static void main(String[] args) throws Throwable {
        Charset charset = Charset.forName("UTF-8");
        Thread t1 = new Thread(() -> {
            try (ServerSocket ss = new ServerSocket(9000)) {
                while (true) {
                    System.out.println("t1将接受连接...");
                    try (Socket s = ss.accept()) {
                        System.out.println("t1接收到连接...");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        System.out.println("t1将接受连接的数据...");
                        String msg;
                        while ((msg = reader.readLine()) != null) {
                            System.out.println(msg);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        Thread.sleep(3000L);
        System.out.println("t1的状态：" + t1.getState());
        Thread.sleep(20000L);
        System.out.println("t1的状态：" + t1.getState());
    }
}
