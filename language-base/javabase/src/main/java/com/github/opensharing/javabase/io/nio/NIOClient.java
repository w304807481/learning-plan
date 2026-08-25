package com.github.opensharing.javabase.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * NIO 客户端
 * 连接 NIOServer（端口 8080），发送数据并接收服务端响应。
 * 使用 Selector 实现非阻塞 I/O，同时监听 READ 和 WRITE 事件。
 */
public class NIOClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        // 1. 打开 SocketChannel 和 Selector
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();

        // 2. 发起连接（非阻塞模式下 connect 不会阻塞）
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        // 注册 OP_CONNECT，等待连接完成
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        System.out.println("[客户端] 正在连接服务端 " + SERVER_HOST + ":" + SERVER_PORT + " ...");

        // 用于暂存待发送的数据
        ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        // 标记是否已经发送过初始消息（避免重复发送）
        boolean initialMessageSent = false;

        while (true) {
            selector.select(100); // 带超时阻塞（100ms），定期唤醒检查控制台输入

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove(); // 必须手动移除，否则下次循环会重复处理

                if (key.isConnectable()) {
                    // 3. 完成连接握手
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()) {
                        channel.finishConnect(); // 完成连接
                    }
                    System.out.println("[客户端] 已连接到服务端");
                    // 连接成功后，注册 OP_READ，并准备发送第一条消息
                    channel.register(selector, SelectionKey.OP_READ);

                    // 准备初始消息
                    String initialMsg = "Hello, NIOServer!";
                    writeBuffer.clear();
                    writeBuffer.put(initialMsg.getBytes());
                    writeBuffer.flip(); // 切换为读模式，供 write 使用
                    channel.write(writeBuffer);
                    System.out.println("[客户端] 发送 -> " + initialMsg);
                    initialMessageSent = true;
                }

                if (key.isReadable()) {
                    // 4. 读取服务端响应
                    SocketChannel channel = (SocketChannel) key.channel();
                    readBuffer.clear();
                    int bytesRead = channel.read(readBuffer);

                    if (bytesRead > 0) {
                        readBuffer.flip();
                        byte[] data = new byte[readBuffer.remaining()];
                        readBuffer.get(data);
                        String response = new String(data);
                        System.out.println("[客户端] 收到 <- " + response);
                    } else if (bytesRead == -1) {
                        System.out.println("[客户端] 服务端已关闭连接");
                        channel.close();
                        selector.close();
                        return;
                    }
                }
            }

            // 5. 初始消息发送后，从控制台读取用户输入并发送
            if (initialMessageSent && !Thread.currentThread().isInterrupted()) {
                // 非阻塞检查控制台是否有输入（简单轮询）
                if (System.in.available() > 0) {
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();
                    if (line != null && !line.isEmpty()) {
                        if ("quit".equalsIgnoreCase(line.trim())) {
                            System.out.println("[客户端] 断开连接");
                            socketChannel.close();
                            selector.close();
                            return;
                        }
                        writeBuffer.clear();
                        writeBuffer.put(line.getBytes());
                        writeBuffer.flip();
                        socketChannel.write(writeBuffer);
                        System.out.println("[客户端] 发送 -> " + line);
                    }
                }
            }
        }
    }
}
