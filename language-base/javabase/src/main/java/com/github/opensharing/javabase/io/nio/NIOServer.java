package com.github.opensharing.javabase.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 服务端
 * 使用 Selector 监听 ACCEPT、READ 事件，处理客户端连接和数据读取，
 * 并将收到的消息回写给客户端。
 */
public class NIOServer {

    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;

        try {
            // 1. 打开 Selector 和 ServerSocketChannel
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
            serverSocketChannel.bind(new InetSocketAddress(PORT)); // 绑定端口
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("[服务端] 启动成功，监听端口: " + PORT);

            // 2. 事件循环
            while (true) {
                int readyCount = selector.select(); // 阻塞，直到有事件发生
                if (readyCount == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove(); // 必须手动移除，防止重复处理

                    try {
                        if (key.isAcceptable()) {
                            handleAccept(key, selector);
                        } else if (key.isReadable()) {
                            handleRead(key, selector);
                        }
                    } catch (IOException e) {
                        // 单个客户端异常不影响其他连接
                        System.out.println("[服务端] 处理客户端异常: " + e.getMessage());
                        closeKey(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[服务端] 启动异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 3. 关闭资源
            closeQuietly(serverSocketChannel);
            closeQuietly(selector);
            System.out.println("[服务端] 资源已释放");
        }
    }

    /**
     * 处理 ACCEPT 事件：接收新客户端连接
     */
    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel client = serverChannel.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            System.out.println("[服务端] 新客户端连接: " + client.getRemoteAddress());
        }
    }

    /**
     * 处理 READ 事件：读取客户端数据并回写响应
     */
    private static void handleRead(SelectionKey key, Selector selector) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int bytesRead = client.read(buffer);

        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String receivedData = new String(data);
            System.out.println("[服务端] 收到数据: " + receivedData);

            // 回写响应给客户端
            String response = "Server received: " + receivedData;
            ByteBuffer writeBuffer = ByteBuffer.wrap(response.getBytes());
            client.write(writeBuffer);
            System.out.println("[服务端] 已响应: " + response);
        } else if (bytesRead == -1) {
            // 客户端已关闭连接
            System.out.println("[服务端] 客户端断开: " + client.getRemoteAddress());
            closeKey(key);
        }
    }

    /**
     * 关闭 SelectionKey 及其关联的 Channel
     */
    private static void closeKey(SelectionKey key) {
        try {
            if (key != null) {
                key.cancel(); // 取消注册
                if (key.channel() != null) {
                    key.channel().close();
                }
            }
        } catch (IOException e) {
            System.err.println("[服务端] 关闭资源异常: " + e.getMessage());
        }
    }

    /**
     * 静默关闭 ServerSocketChannel
     */
    private static void closeQuietly(ServerSocketChannel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            System.err.println("[服务端] 关闭 ServerSocketChannel 异常: " + e.getMessage());
        }
    }

    /**
     * 静默关闭 Selector
     */
    private static void closeQuietly(Selector selector) {
        try {
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            System.err.println("[服务端] 关闭 Selector 异常: " + e.getMessage());
        }
    }
}
