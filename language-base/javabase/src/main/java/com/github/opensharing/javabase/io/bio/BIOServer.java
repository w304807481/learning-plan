package com.github.opensharing.javabase.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO服务端
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            // 主线程阻塞在此，直到有客户端连接连上
            Socket socket = serverSocket.accept();
            System.out.println("New client connected: " + socket.getInetAddress());

            // 每个连接需要一个线程处理
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 处理socket请求
                    try {
                        InputStream inputStream = socket.getInputStream();
                        // 子线程阻塞在此，直到有数据可读
                        byte[] buffer = new byte[1024];
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead != -1) {
                            // 处理数据
                            System.out.println("Received data: " + new String(buffer, 0, bytesRead));
                        }

                        socket.getOutputStream().write("Hello, Client!".getBytes());
                        socket.getOutputStream().flush();
                        System.out.println("Sent data to client: Hello, Client!");

                        socket.close();
                        System.out.println("Closed socket connection.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}
