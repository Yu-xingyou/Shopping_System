package com.xingyou.service;

import com.xingyou.entity.chat.ChatMessage;
import com.xingyou.entity.chat.ChatSession;
import com.xingyou.entity.people.User;

import java.util.List;

public interface ChatService {
    
    ChatSession getOrCreateSession(String userId, String targetId);
    
    List<ChatSession> getUserSessions(String userId, Integer userRole);
    
    void sendMessage(String senderId, String senderRole, String receiverId, String content);
    
    List<ChatMessage> getMessageHistory(String sessionId);
    
    void markMessagesAsRead(List<Long> messageIds);
    
    List<ChatMessage> getUnreadMessages(String userId);
    
    List<User> getAvailableContacts(String userId, Integer role);
    
    ChatSession createSession(String userId, String targetId, Integer userRole);
}
