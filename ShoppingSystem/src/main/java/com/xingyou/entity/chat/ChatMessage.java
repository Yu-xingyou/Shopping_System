package com.xingyou.entity.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long messageId;
    private String chatSessionId;
    private String senderId;
    private String receiverId;
    private String content;
    private Integer messageType;
    private Integer isRead;
    private LocalDateTime sendTime;
    private LocalDateTime readTime;
    
    private String senderAvatar;
}
