package com.example.demo.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebSocketPingPongTest {

    @Test
    void pingReturnsPong() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> received = new AtomicReference<>();

        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8081/ws")) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                send("ping");
            }

            @Override
            public void onMessage(String message) {
                received.set(message);
                latch.countDown();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {}

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                latch.countDown();
            }
        };

        client.connectBlocking(5, TimeUnit.SECONDS);
        boolean replied = latch.await(5, TimeUnit.SECONDS);
        client.closeBlocking();

        assertThat(replied).isTrue();
        assertThat(received.get()).isEqualTo("pong");
    }
}
