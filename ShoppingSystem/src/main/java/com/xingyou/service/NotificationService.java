package com.xingyou.service;

import com.xingyou.entity.shopping.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NotificationService {
    
    List<Notification> findUnreadByUserId(String userId);
    
    List<Notification> findAllByUserId(String userId);
    
    void createRestockNotification(String userId, Integer productId, String productName);
    
    void markAsRead(Integer id, String userId);
    
    void markAllAsRead(String userId);
    
    int countUnread(String userId);
}
