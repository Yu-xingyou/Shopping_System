package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单实体类
 * 用于存储用户的购物订单信息，包含订单状态、收货信息等
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    /**
     * 订单ID，主键，自增
     */
    private Integer id;
    
    /**
     * 订单编号，唯一标识
     */
    private String orderNum;
    
    /**
     * 用户ID，关联用户表
     */
    private String userId;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 订单状态：0-待支付，1-已支付，2-缺货等待，3-已完成，4-已取消
     */
    private Integer status;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 发货时间
     */
    private LocalDateTime deliverTime;
    
    /**
     * 订单完成时间
     */
    private LocalDateTime finishTime;
    
    /**
     * 订单备注
     */
    private String remark;
    
    /**
     * 小票图片链接
     */
    private String receiptImage;
    
    /**
     * 订单原价（打折前的金额）
     */
    private BigDecimal originalAmount;
    
    /**
     * 折扣率（0-1之间，如0.8表示8折，1表示无折扣）
     */
    private BigDecimal discount;
}
