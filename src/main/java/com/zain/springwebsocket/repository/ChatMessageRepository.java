package com.zain.springwebsocket.repository;

import com.zain.springwebsocket.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * 聊天消息数据库操作接口
 * 继承 JpaRepository，自动提供 CRUD 功能
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}