package com.example.demo.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof PingWebSocketFrame ping) {
            log.info("Received ping from {}", ctx.channel().remoteAddress());
            ctx.writeAndFlush(new PongWebSocketFrame(ping.content().retain()));
        } else if (frame instanceof TextWebSocketFrame text) {
            String msg = text.text();
            log.info("Received text: {}", msg);
            if ("ping".equalsIgnoreCase(msg)) {
                ctx.writeAndFlush(new TextWebSocketFrame("pong"));
            } else {
                ctx.writeAndFlush(new TextWebSocketFrame("echo: " + msg));
            }
        } else {
            log.warn("Unsupported frame type: {}", frame.getClass().getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("WebSocket error", cause);
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("Client connected: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("Client disconnected: {}", ctx.channel().remoteAddress());
    }
}
