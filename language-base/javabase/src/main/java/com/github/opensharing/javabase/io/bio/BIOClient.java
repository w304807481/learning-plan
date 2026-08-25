package com.github.opensharing.javabase.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * BIOClient
 */
public class BIOClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        System.out.println("Connected to server: " + socket.getInetAddress());

        socket.getOutputStream().write("Hello, Server!".getBytes());
        socket.getOutputStream().flush();
        System.out.println("Sent data to server: Hello, Server!");

        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);
        if (bytesRead != -1) {
            System.out.println("Received data from server: " + new String(buffer, 0, bytesRead));
        }

        socket.close();
    }
}
