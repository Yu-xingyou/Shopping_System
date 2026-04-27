package com.xingyou.mapper;

import com.xingyou.entity.chat.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatMessageMapper {
    
    int insert(ChatMessage message);
    
    List<ChatMessage> selectBySessionId(@Param("sessionId") String sessionId);
    
    List<ChatMessage> selectUnreadMessages(@Param("receiverId") String receiverId);
    
    int updateReadStatus(@Param("messageIds") List<Long> messageIds);
    
    ChatMessage selectLatestMessage(@Param("sessionId") String sessionId);
}
