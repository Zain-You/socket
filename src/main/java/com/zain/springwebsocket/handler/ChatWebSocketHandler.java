package com.zain.springwebsocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zain.springwebsocket.model.ChatMessage;
import com.zain.springwebsocket.repository.ChatMessageRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 消息处理类
 * 处理客户端消息、存数据库、广播到其他客户端
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    /** 存储所有在线 WebSocket 会话 */
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final ChatMessageRepository repository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatWebSocketHandler(ChatMessageRepository repository, RedisTemplate<String, String> redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 当客户端连接成功后调用
     * @param session WebSocketSession 对象
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("新连接：" + session.getId());
    }

    /**
     * 处理客户端发送的消息
     * @param session WebSocketSession
     * @param message 客户端消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // 存储消息到数据库
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(session.getId());
        chatMessage.setContent(payload);
        chatMessage.setTimestamp(LocalDateTime.now());
        repository.save(chatMessage);

        // 发布消息到 Redis（实现分布式广播）
        redisTemplate.convertAndSend("chatChannel", objectMapper.writeValueAsString(chatMessage));
    }

    /**
     * 客户端断开连接时调用
     * @param session WebSocketSession
     * @param status 关闭状态
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        System.out.println("连接关闭：" + session.getId());
    }

    /**
     * 从 Redis 订阅到消息后，广播给本节点所有在线客户端
     * @param chatMessage 聊天消息对象
     */
    public void broadcastMessage(ChatMessage chatMessage) {
        sessions.values().forEach(s -> {
            try {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(chatMessage.getSender() + ": " + chatMessage.getContent()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
