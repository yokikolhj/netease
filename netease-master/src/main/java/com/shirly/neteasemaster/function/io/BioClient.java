package com.shirly.neteasemaster.function.io;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author shirly
 * @Date 2020/1/17 16:34
 * @Description
 */
public class BioClient implements Runnable {

    private String address;
    private int port;

    public BioClient(String address, int port) {
        super();
        this.address = address;
        this.port = port;
    }

    // IO应用场景 物联网-设备- 服务器端进行通信
    // 心跳通信
    // 即时状态上报
    // 服务器端对客户端远程操作
    @Override
    public void run() {
        // 即时聊天？
        try (Socket socket = new Socket(address, port)) {
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入：");
            String msg = scanner.nextLine();
            outputStream.write(msg.getBytes("UTF-8"));
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        BioClient bioClient = new BioClient("localhost", 1111);
        bioClient.run();
    }
}
