package com.zain.springwebsocket.config;

import com.zain.springwebsocket.handler.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

/**
 * WebSocket 配置类
 * 注册 WebSocket Handler 并设置访问路径
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    /**
     * 注册 WebSocket Handler
     * @param registry WebSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /ws 为客户端连接路径，允许所有跨域请求
        registry.addHandler(chatWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
