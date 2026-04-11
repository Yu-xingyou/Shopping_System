package com.xingyou.service.impl;

import com.xingyou.entity.shopping.Notification;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.NotificationMapper;
import com.xingyou.service.NotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Resource
    private NotificationMapper notificationMapper;
    
    @Override
    public List<Notification> findUnreadByUserId(String userId) {
        return notificationMapper.findUnreadByUserId(userId);
    }
    
    @Override
    public List<Notification> findAllByUserId(String userId) {
        return notificationMapper.findAllByUserId(userId);
    }
    
    @Override
    public void createRestockNotification(String userId, Integer productId, String productName) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        if (productId == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setProductId(productId);
        notification.setProductName(productName);
        notification.setContent("您收藏的商品【" + productName + "】已经补货啦，快去看看吧！");
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
    }
    
    @Override
    public void markAsRead(Integer id, String userId) {
        if (id == null) {
            throw new BusinessException(400, "通知ID不能为空");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        int rows = notificationMapper.markAsRead(id, userId);
        if (rows != 1) {
            throw new BusinessException(404, "通知不存在或无权操作");
        }
    }
    
    @Override
    public void markAllAsRead(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        notificationMapper.markAllAsRead(userId);
    }
    
    @Override
    public int countUnread(String userId) {
        return notificationMapper.countUnread(userId);
    }
}
