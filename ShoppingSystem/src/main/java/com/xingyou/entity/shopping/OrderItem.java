package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单项实体类
 * 用于存储订单中的具体商品信息，一个订单可包含多个订单项
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {
    /**
     * 订单项ID，主键，自增
     */
    private Integer id;
    
    /**
     * 订单ID，关联订单表
     */
    private Integer orderId;
    
    /**
     * 商品ID，关联商品表
     */
    private Integer productId;
    
    /**
     * 商品名称（快照，保存下单时的商品名称）
     */
    private String productName;
    
    /**
     * 商品单价（快照，保存下单时的商品价格）
     */
    private Double productPrice;
    
    /**
     * 购买数量
     */
    private Integer quantity;
    
    /**
     * 商品图片URL（快照，保存下单时的商品图片）
     */
    private String productImage;
}
