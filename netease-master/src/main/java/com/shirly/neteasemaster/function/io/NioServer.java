package com.shirly.neteasemaster.function.io;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Author shirly
 * @Date 2020/1/19 14:04
 * @Description NIO服务
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        // 创建管家
        Selector selector = Selector.open();

        // 创建线程池
        ThreadPoolExecutor tpool = new ThreadPoolExecutor(3, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        // 创建通道
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.configureBlocking(false); // 设置为非阻塞
            ssc.register(selector, SelectionKey.OP_ACCEPT); // 注册给管家，让管家监听accept，使当前连接可通

            while (true) {
                int readyChannels = selector.select(); // 返回就绪的Channel个数
                if (readyChannels == 0) {
                    continue;
                }
                // 当有就绪的Channel时，获取就绪的SelectionKey
                Set<SelectionKey> selectionKeys =  selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    // 拿到具体的selectionKey
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        // 准备就绪状态下，这个channel该做什么
                        ServerSocketChannel skc = (ServerSocketChannel) key.channel();
                        SocketChannel sc = skc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ); // 监听可读状态
                    } else if (key.isConnectable()) {

                    } else if (key.isReadable()) {
                        // 可读状态下，执行其他操作
                        tpool.execute(() -> {
                            // TODO
                        });
                    } else if (key.isWritable()) {

                    }
                    keyIterator.remove(); // 拿出来之后移除
                }
            }

        } catch (IOException e) {

        }
    }

//    static class  SocketRead implements Runnable {
//
//        @Override
//        public void run() {
//            // 读我们的buffer
//        }
//    }
}
