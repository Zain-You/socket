package com.zain.springwebsocket.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * 聊天消息实体类
 * 数据库存储每条聊天记录
 */
@Entity
@Data
public class ChatMessage {

    /** 消息 ID，自动生成 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 消息发送者，可以用 sessionId 或用户名 */
    private String sender;

    /** 消息内容 */
    private String content;

    /** 消息发送时间 */
    private LocalDateTime timestamp;
}