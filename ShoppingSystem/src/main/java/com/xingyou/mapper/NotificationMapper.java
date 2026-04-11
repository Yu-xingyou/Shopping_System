package com.xingyou.mapper;

import com.xingyou.entity.shopping.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    
    List<Notification> findUnreadByUserId(@Param("userId") String userId);
    
    List<Notification> findAllByUserId(@Param("userId") String userId);
    
    int insert(Notification notification);
    
    int markAsRead(@Param("id") Integer id, @Param("userId") String userId);
    
    int markAllAsRead(@Param("userId") String userId);
    
    int countUnread(@Param("userId") String userId);
}
