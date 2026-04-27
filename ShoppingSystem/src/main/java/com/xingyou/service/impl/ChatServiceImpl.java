package com.xingyou.service.impl;

import com.xingyou.entity.chat.ChatMessage;
import com.xingyou.entity.chat.ChatSession;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.AdminMapper;
import com.xingyou.mapper.ChatMessageMapper;
import com.xingyou.mapper.ChatSessionMapper;
import com.xingyou.mapper.StaffMapper;
import com.xingyou.mapper.UserMapper;
import com.xingyou.service.ChatService;
import com.xingyou.websocket.ChatWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    private ChatSessionMapper chatSessionMapper;
    
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StaffMapper staffMapper;
    
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public ChatSession getOrCreateSession(String userId, String targetId) {
        if (userId == null || targetId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        if (userId.equals(targetId)) {
            throw new BusinessException(400, "不能与自己聊天");
        }
        
        ChatSession session = chatSessionMapper.selectByUsers(userId, targetId);
        
        if (session == null) {
            session = new ChatSession();
            session.setSessionId(UUID.randomUUID().toString().replace("-", ""));
            session.setUserId(userId);
            session.setTargetId(targetId);
            session.setUnreadCount(0);
            session.setCreateTime(LocalDateTime.now());
            session.setUpdateTime(LocalDateTime.now());
            
            chatSessionMapper.insert(session);
            log.info("创建新会话: {} <-> {}", userId, targetId);
        }
        
        return session;
    }
    
    @Override
    public List<ChatSession> getUserSessions(String userId, Integer userRole) {
        if (userId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        List<ChatSession> sessions = chatSessionMapper.selectByUserId(userId);
        
        for (ChatSession session : sessions) {
            String otherUserId;
            if (session.getUserId().equals(userId)) {
                otherUserId = session.getTargetId();
            } else {
                otherUserId = session.getUserId();
            }
            
            User otherUser = findUserById(otherUserId, null);
            if (otherUser != null) {
                session.setTargetName(otherUser.getName());
                session.setTargetAvatar(otherUser.getAvatar());
            }
        }
        
        return sessions;
    }
    
    @Override
    @Transactional
    public void sendMessage(String senderId, String senderRoleStr, String receiverId, String content) {
        if (senderId == null || receiverId == null) {
            throw new BusinessException(400, "发送者和接收者ID不能为空");
        }
        
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException(400, "消息内容不能为空");
        }
        
        Integer senderRole = Integer.parseInt(senderRoleStr);
        validateChatPermission(senderId, receiverId, senderRole);
        
        ChatSession session = getOrCreateSession(senderId, receiverId);
        
        ChatMessage message = new ChatMessage();
        message.setChatSessionId(session.getSessionId());
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content.trim());
        message.setMessageType(1);
        message.setIsRead(0);
        message.setSendTime(LocalDateTime.now());
        
        chatMessageMapper.insert(message);
        
        chatSessionMapper.updateLastMessage(session.getSessionId(), content.trim());
        chatSessionMapper.incrementUnreadCount(session.getSessionId());
        
        log.info("尝试推送消息: senderId={}, receiverId={}, 在线状态={}", senderId, receiverId, ChatWebSocket.isOnline(receiverId));
        
        if (ChatWebSocket.isOnline(receiverId)) {
            try {
                User sender = findUserById(senderId, null);
                if (sender != null) {
                    message.setSenderAvatar(sender.getAvatar());
                }
                
                Map<String, Object> pushData = Map.of(
                    "type", "new_message",
                    "data", message
                );
                ChatWebSocket.sendMessageToUser(receiverId, pushData);
                log.info("✅ 实时推送消息成功给: {}, 消息内容: {}", receiverId, content);
            } catch (Exception e) {
                log.error("❌ 推送消息失败, receiverId={}", receiverId, e);
            }
        } else {
            log.warn("⚠️ 用户 {} 不在线，消息已保存到数据库", receiverId);
        }
        
        log.info("发送消息: {} -> {}, 会话ID: {}", senderId, receiverId, session.getSessionId());
    }
    
    @Override
    public List<ChatMessage> getMessageHistory(String sessionId) {
        if (sessionId == null) {
            throw new BusinessException(400, "会话ID不能为空");
        }
        
        List<ChatMessage> messages = chatMessageMapper.selectBySessionId(sessionId);
        
        for (ChatMessage message : messages) {
            User sender = findUserById(message.getSenderId(), null);
            if (sender != null) {
                message.setSenderAvatar(sender.getAvatar());
            }
        }
        
        return messages;
    }
    
    @Override
    @Transactional
    public void markMessagesAsRead(List<Long> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return;
        }
        
        chatMessageMapper.updateReadStatus(messageIds);
        log.info("标记消息已读: {} 条", messageIds.size());
    }
    
    @Override
    public List<ChatMessage> getUnreadMessages(String userId) {
        if (userId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        return chatMessageMapper.selectUnreadMessages(userId);
    }

    @Override
    public List<User> getAvailableContacts(String userId, Integer role) {
        if (userId == null || role == null) {
            throw new BusinessException(400, "用户ID和角色不能为空");
        }
        
        List<User> contacts = new ArrayList<>();
        
        if (role == 0) {
            contacts = userMapper.selectUsersByRole(1);
        } else if (role == 1) {
            contacts.addAll(userMapper.selectUsersByRole(0));
            contacts.addAll(staffMapper.findAllStaffs());
            contacts.addAll(adminMapper.findAllAdmins());
        } else if (role == 2) {
            contacts = staffMapper.findAllStaffs();
        } else {
            throw new BusinessException(403, "无权查看联系人");
        }
        
        contacts.removeIf(user -> user.getUserId().equals(userId));
        
        return contacts;
    }
    
    @Override
    @Transactional
    public ChatSession createSession(String userId, String targetId, Integer userRole) {
        if (userId == null || targetId == null) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        validateChatPermission(userId, targetId, userRole);
        
        return getOrCreateSession(userId, targetId);
    }
    
    private void validateChatPermission(String senderId, String receiverId, Integer senderRole) {
        Integer receiverRole = getRoleById(receiverId);
        
        if (receiverRole == null) {
            throw new BusinessException(404, "用户不存在");
        }
        
        boolean allowed = false;
        
        if (senderRole == 0) {
            allowed = (receiverRole == 1);
        } else if (senderRole == 1) {
            allowed = (receiverRole == 0 || receiverRole == 1 || receiverRole == 2);
        } else if (senderRole == 2) {
            allowed = (receiverRole == 1);
        }
        
        if (!allowed) {
            log.warn("权限验证失败: 发送者ID={}, 发送者角色={}, 接收者ID={}, 接收者角色={}", 
                     senderId, senderRole, receiverId, receiverRole);
            throw new BusinessException(403, "无权与该用户聊天");
        }
    }

    private Integer getRoleById(String userId) {
        if (userId == null) {
            return null;
        }
        
        if (userId.startsWith("U")) {
            User user = userMapper.findByUserId(userId);
            return user != null ? user.getRole() : null;
        } else {
            User staff = staffMapper.findByStaffId(userId);
            if (staff != null) {
                return staff.getRole();
            }
            User admin = adminMapper.findByAdminId(userId);
            return admin != null ? admin.getRole() : null;
        }
    }

    private User findUserById(String userId, Integer role) {
        if (userId == null) {
            return null;
        }
        
        if (userId.startsWith("U")) {
            return userMapper.findByUserId(userId);
        } else {
            User staff = staffMapper.findByStaffId(userId);
            if (staff != null) {
                return staff;
            }
            return adminMapper.findByAdminId(userId);
        }
    }

}
