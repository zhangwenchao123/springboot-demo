package com.example.demo.websocket;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyWebSocketServerRunner implements ApplicationRunner {

    private final NettyWebSocketServer server;

    public NettyWebSocketServerRunner(NettyWebSocketServer server) {
        this.server = server;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                server.start();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "netty-websocket-server");
        thread.setDaemon(true);
        thread.start();
    }
}
