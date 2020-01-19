package com.shirly.neteasemaster.function.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author shirly
 * @Date 2020/1/17 16:24
 * @Description
 */
public class BioService {
    // java中所谓的通信，就是向计算机端口写一段数据
    // 8080 --> Socket
    public static void main(String[] args) {
        int port = 1111; // 端口

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            // 作为服务端，时刻接收客户端发送过来的信息
            while (true) {
                Socket socket = serverSocket.accept(); // 等待客户端连接
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg;
                while ((msg = reader.readLine()) != null) {
                    System.out.println(msg);
                }
            }
        } catch (Exception e) {

        }

    }
}
