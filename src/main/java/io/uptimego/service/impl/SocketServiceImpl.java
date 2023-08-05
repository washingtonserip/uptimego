package io.uptimego.service.impl;

import io.uptimego.service.SocketService;

import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketServiceImpl implements SocketService {

    public void connectSocket(String host, int port) throws Exception {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
        }
    }
}
