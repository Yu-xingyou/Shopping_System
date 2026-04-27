package com.xingyou.entity.shopping;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通知实体类
 * 用于存储系统发送给用户的各类通知信息，如商品补货通知等
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
     * 商品ID，关联商品表（可选，用于补货通知）
     */
    private Integer productId;
    
    /**
     * 商品名称（冗余字段，方便显示）
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
