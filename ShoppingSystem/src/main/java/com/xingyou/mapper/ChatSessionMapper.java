package com.xingyou.mapper;

import com.xingyou.entity.chat.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatSessionMapper {
    
    int insert(ChatSession session);
    
    ChatSession selectByUsers(@Param("userId") String userId, @Param("targetId") String targetId);
    
    List<ChatSession> selectByUserId(@Param("userId") String userId);
    
    int updateLastMessage(@Param("sessionId") String sessionId, 
                         @Param("lastMessage") String lastMessage);
    
    int incrementUnreadCount(@Param("sessionId") String sessionId);
    
    int resetUnreadCount(@Param("sessionId") String sessionId);
}
