package com.bailiban.spring.chat;

import java.io.IOException;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.1.136",8080);
        System.out.println("成功连接服务器");
        new ReceiveThread("Server",socket).start();
        new SendThread("Client",socket).start();

    }
}
