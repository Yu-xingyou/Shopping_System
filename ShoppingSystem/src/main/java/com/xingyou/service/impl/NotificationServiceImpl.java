package com.xingyou.service.impl;

import com.xingyou.entity.shopping.Notification;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.NotificationMapper;
import com.xingyou.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知服务层实现类
 * 实现通知管理的业务逻辑，包括补货通知创建、已读标记等功能
 */
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Override
    public List<Notification> findUnreadByUserId(String userId) {
        log.debug("查询用户未读通知 - userId: {}", userId);
        return notificationMapper.findUnreadByUserId(userId);
    }
    
    @Override
    public List<Notification> findAllByUserId(String userId) {
        log.debug("查询用户所有通知 - userId: {}", userId);
        return notificationMapper.findAllByUserId(userId);
    }
    
    /**
     * 创建商品补货通知
     * 
     * 当管理员将库存从0补充到大于0时，系统会自动为收藏了该商品的用户创建补货通知。
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @param productName 商品名称
     * @throws BusinessException 当用户ID或商品ID为空时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRestockNotification(String userId, Integer productId, String productName) {
        log.info("创建补货通知 - userId: {}, productId: {}, productName: {}", userId, productId, productName);
        
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
        log.info("创建补货通知成功 - userId: {}, productId: {}", userId, productId);
    }
    
    /**
     * 标记单条通知为已读
     *
     * @param id 通知ID
     * @param userId 用户ID
     * @throws BusinessException 当通知不存在或无权操作时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Integer id, String userId) {
        log.info("标记通知为已读 - notificationId: {}, userId: {}", id, userId);
        
        if (id == null) {
            throw new BusinessException(400, "通知ID不能为空");
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        int rows = notificationMapper.markAsRead(id, userId);
        if (rows != 1) {
            log.warn("标记通知已读失败 - 通知不存在或无权操作: notificationId: {}, userId: {}", id, userId);
            throw new BusinessException(404, "通知不存在或无权操作");
        }
        
        log.info("标记通知已读成功 - notificationId: {}", id);
    }
    
    /**
     * 标记用户所有未读通知为已读
     *
     * @param userId 用户ID
     * @throws BusinessException 当用户ID为空时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(String userId) {
        log.info("标记所有通知为已读 - userId: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        
        notificationMapper.markAllAsRead(userId);
        log.info("标记所有通知已读成功 - userId: {}", userId);
    }
    
    @Override
    public int countUnread(String userId) {
        log.debug("统计用户未读通知数量 - userId: {}", userId);
        return notificationMapper.countUnread(userId);
    }
}
