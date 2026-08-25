package com.github.opensharing.javabase.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO服务端
 */
public class AIOServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));

        System.out.println("Server started on port 8080");

        serverSocketChannel.accept(null, new AIOServerHandler(serverSocketChannel));

        //模拟主线程干其他事情
        while (true) {
            System.out.println("Server is running...");
            Thread.sleep(1000);
        }
    }

    private static class AIOServerHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        private final AsynchronousServerSocketChannel serverSocketChannel;

        public AIOServerHandler(AsynchronousServerSocketChannel serverSocketChannel) {
            this.serverSocketChannel = serverSocketChannel;

        }

        @Override
        public void completed(AsynchronousSocketChannel result, Void attachment) {
            // 继续接受下一个连接
            this.serverSocketChannel.accept(null, this);
            System.out.println("New client connected");

            // 读取客户端数据
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            result.read(buffer, buffer, new AIOClientHandler(result));
        }

        @Override
        public void failed(Throwable exc, Void attachment) {
            exc.printStackTrace();
        }

        private static class AIOClientHandler implements CompletionHandler<Integer, ByteBuffer> {

            private final AsynchronousSocketChannel socketChannel;

            public AIOClientHandler(AsynchronousSocketChannel result) {
                this.socketChannel = result;

            }

            @Override
            public void completed(Integer result, ByteBuffer attachment) {

                if (result == -1) {
                    System.out.println("Client disconnected");
                    return;
                }

                attachment.flip();
                byte[] data = new byte[attachment.remaining()];
                attachment.get(data);
                System.out.println("Received data from client: " + new String(data));
                attachment.clear();

                this.socketChannel.read(attachment, attachment, this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        }
    }
}
