package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户通知实体类
 * 用于存储用户收到的系统通知信息，如商品补货提醒等
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    /**
     * 通知ID，主键，自增
     */
    private Integer id;
    
    /**
     * 用户ID，关联用户表
     */
    private String userId;
    
    /**
     * 商品ID，关联商品表
     */
    private Integer productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 是否已读：0-未读，1-已读
     */
    private Integer isRead;
    
    /**
     * 通知创建时间
     */
    private LocalDateTime createTime;
}
