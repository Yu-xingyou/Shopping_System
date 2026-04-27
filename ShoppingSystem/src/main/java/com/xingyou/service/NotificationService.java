package com.xingyou.service;

import com.xingyou.entity.shopping.Notification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知服务层接口
 * 提供通知管理的业务逻辑，包括补货通知创建、已读标记等功能
 */
@Service
public interface NotificationService {
    
    /**
     * 根据用户ID查询未读通知列表
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    List<Notification> findUnreadByUserId(String userId);
    
    /**
     * 根据用户ID查询所有通知列表
     *
     * @param userId 用户ID
     * @return 所有通知列表
     */
    List<Notification> findAllByUserId(String userId);
    
    /**
     * 创建商品补货通知
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param productName 商品名称
     */
    void createRestockNotification(String userId, Integer productId, String productName);
    
    /**
     * 标记单条通知为已读
     *
     * @param id 通知ID
     * @param userId 用户ID
     */
    void markAsRead(Integer id, String userId);
    
    /**
     * 标记用户所有未读通知为已读
     *
     * @param userId 用户ID
     */
    void markAllAsRead(String userId);
    
    /**
     * 查询用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    int countUnread(String userId);
}
