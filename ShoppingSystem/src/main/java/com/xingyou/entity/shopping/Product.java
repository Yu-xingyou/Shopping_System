package com.xingyou.entity.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品实体类
 * 用于存储购物系统中的商品信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    /**
     * 商品ID，主键，自增
     */
    private Integer id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 商品库存数量
     */
    private Integer stock;
    
    /**
     * 商品分类
     */
    private String category;
    
    /**
     * 商品图片URL
     */
    private String imageUrl;
    
    /**
     * 商品状态：0-下架，1-上架
     */
    private Integer status;
}
