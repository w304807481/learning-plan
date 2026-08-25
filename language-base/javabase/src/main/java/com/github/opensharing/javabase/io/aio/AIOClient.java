package com.github.opensharing.javabase.io.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * AIO 客户端
 * 使用异步 I/O 连接 AIOServer（端口 8080），发送数据并接收服务端响应。
 */
public class AIOClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        // 1. 打开异步 SocketChannel
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();

        // 用于等待连接完成
        CountDownLatch latch = new CountDownLatch(1);

        // 2. 异步连接服务端
        socketChannel.connect(
                new InetSocketAddress(SERVER_HOST, SERVER_PORT),
                latch,
                new ConnectHandler(socketChannel)
        );

        // 等待连接完成
        latch.await();
        System.out.println("[客户端] 已连接到服务端 " + SERVER_HOST + ":" + SERVER_PORT);

        // 3. 连接成功后，启动一次异步读取（后续在 ReadHandler 中链式读取）
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        socketChannel.read(readBuffer, readBuffer, new ReadHandler(socketChannel));

        // 4. 发送初始消息
        String initialMsg = "Hello, AIOServer!";
        ByteBuffer writeBuffer = ByteBuffer.wrap(initialMsg.getBytes());
        socketChannel.write(writeBuffer, null, new WriteHandler());
        System.out.println("[客户端] 发送 -> " + initialMsg);

        // 5. 从控制台读取用户输入并发送
        Scanner scanner = new Scanner(System.in);
        System.out.println("[客户端] 请输入消息（输入 quit 退出）：");

        while (true) {
            String line = scanner.nextLine();
            if (line == null || "quit".equalsIgnoreCase(line.trim())) {
                System.out.println("[客户端] 断开连接");
                socketChannel.close();
                break;
            }

            if (!line.isEmpty()) {
                ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());
                socketChannel.write(buffer, null, new WriteHandler());
                System.out.println("[客户端] 发送 -> " + line);
            }
        }

        scanner.close();
        socketChannel.close();
        System.out.println("[客户端] 已关闭");
    }

    /**
     * 连接回调处理器
     */
    private static class ConnectHandler implements CompletionHandler<Void, CountDownLatch> {
        private final AsynchronousSocketChannel socketChannel;

        public ConnectHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Void result, CountDownLatch latch) {
            // 连接成功，释放 latch 让主线程继续
            latch.countDown();
        }

        @Override
        public void failed(Throwable exc, CountDownLatch latch) {
            System.err.println("[客户端] 连接失败: " + exc.getMessage());
            latch.countDown();
        }
    }

    /**
     * 写入回调处理器（仅处理写入完成，不触发 read）
     */
    private static class WriteHandler implements CompletionHandler<Integer, Void> {
        @Override
        public void completed(Integer result, Void attachment) {
            // 写入完成，无需额外操作
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            System.err.println("[客户端] 写入失败: " + exc.getMessage());
        }
    }

    /**
     * 读取回调处理器（链式读取：处理完当前数据后，发起下一次 read）
     */
    private static class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
        private final AsynchronousSocketChannel socketChannel;

        public ReadHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            if (result == -1) {
                System.out.println("[客户端] 服务端已关闭连接");
                return;
            }

            if (result > 0) {
                attachment.flip();
                byte[] data = new byte[attachment.remaining()];
                attachment.get(data);
                System.out.println("[客户端] 收到 <- " + new String(data));
            }

            // 清空缓冲区，链式发起下一次异步读取
            attachment.clear();
            socketChannel.read(attachment, attachment, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            System.err.println("[客户端] 读取失败: " + exc.getMessage());
        }
    }
}
