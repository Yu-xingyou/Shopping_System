package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.chat.ChatMessage;
import com.xingyou.entity.chat.ChatSession;
import com.xingyou.entity.people.User;
import com.xingyou.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @GetMapping("/sessions")
    public Result getSessions(
            @RequestParam String userId,
            @RequestParam Integer role) {
        List<ChatSession> sessions = chatService.getUserSessions(userId, role);
        return Result.success(sessions);
    }
    
    @GetMapping("/messages")
    public Result getMessages(@RequestParam String sessionId) {
        List<ChatMessage> messages = chatService.getMessageHistory(sessionId);
        return Result.success(messages);
    }
    
    @PostMapping("/send")
    public Result sendMessage(@RequestBody MessageRequest request) {
        log.info("收到发送消息请求: {} -> {}", request.getSenderId(), request.getReceiverId());
        chatService.sendMessage(request.getSenderId(), request.getSenderRole(), 
                               request.getReceiverId(), request.getContent());
        return Result.success("发送成功");
    }
    
    @PostMapping("/read")
    public Result markAsRead(@RequestBody List<Long> messageIds) {
        chatService.markMessagesAsRead(messageIds);
        return Result.success("标记已读成功");
    }
    
    @GetMapping("/contacts")
    public Result getContacts(
            @RequestParam String userId,
            @RequestParam Integer role) {
        List<User> contacts = chatService.getAvailableContacts(userId, role);
        return Result.success(contacts);
    }
    
    @PostMapping("/session/create")
    public Result createSession(
            @RequestParam String userId,
            @RequestParam String targetId,
            @RequestParam Integer role) {
        log.info("创建聊天会话: {} -> {}", userId, targetId);
        ChatSession session = chatService.createSession(userId, targetId, role);
        return Result.success(session);
    }
    
    public static class MessageRequest {
        private String senderId;
        private String senderRole;
        private String receiverId;
        private String content;
        
        public String getSenderId() {
            return senderId;
        }
        
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
        
        public String getSenderRole() {
            return senderRole;
        }
        
        public void setSenderRole(String senderRole) {
            this.senderRole = senderRole;
        }
        
        public String getReceiverId() {
            return receiverId;
        }
        
        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}
