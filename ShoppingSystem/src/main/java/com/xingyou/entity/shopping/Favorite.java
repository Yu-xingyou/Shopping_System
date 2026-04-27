package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商品收藏实体类
 * 用于记录用户收藏的商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    /**
     * 收藏记录ID，主键，自增
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
     * 收藏创建时间
     */
    private LocalDateTime createTime;
}
