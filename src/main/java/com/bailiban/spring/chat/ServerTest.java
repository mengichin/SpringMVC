package com.bailiban.spring.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("开启服务器，等待客户端连接");
        Socket socket =serverSocket.accept();
        new ReceiveThread("Client",socket).start();
        new SendThread("Server",socket).start();
    }
}
