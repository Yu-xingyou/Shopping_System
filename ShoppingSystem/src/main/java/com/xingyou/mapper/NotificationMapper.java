package com.xingyou.mapper;

import com.xingyou.entity.shopping.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知数据访问层接口
 * 提供通知的查询、插入和更新等数据库操作
 */
@Mapper
public interface NotificationMapper {
    
    /**
     * 根据用户ID查询未读通知列表
     *
     * @param userId 用户ID
     * @return 未读通知列表
     */
    List<Notification> findUnreadByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户ID查询所有通知列表
     *
     * @param userId 用户ID
     * @return 所有通知列表
     */
    List<Notification> findAllByUserId(@Param("userId") String userId);
    
    /**
     * 插入通知记录
     *
     * @param notification 通知对象
     * @return 影响行数
     */
    int insert(Notification notification);
    
    /**
     * 标记单条通知为已读
     *
     * @param id 通知ID
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAsRead(@Param("id") Integer id, @Param("userId") String userId);
    
    /**
     * 标记用户所有未读通知为已读
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int markAllAsRead(@Param("userId") String userId);
    
    /**
     * 查询用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    int countUnread(@Param("userId") String userId);
}
