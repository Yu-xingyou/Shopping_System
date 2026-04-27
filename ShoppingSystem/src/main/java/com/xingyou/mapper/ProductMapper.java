package com.xingyou.mapper;

import com.xingyou.entity.shopping.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品数据访问层接口
 * 
 * 提供商品的查询功能，支持全量查询、条件筛选和单件查询，
 * 条件查询支持按名称或分类进行模糊匹配，并按相关度排序。
 */
@Mapper
public interface ProductMapper {
    
    /**
     * 查询所有商品
     *
     * @return 系统中所有商品的列表
     */
    List<Product> findAll();
    
    /**
     * 根据条件动态查询商品
     * 
     * 支持以下查询条件：
     * - id: 精确匹配商品ID
     * - name: 模糊匹配商品名称或分类
     * 
     * 排序规则：
     * - 按名称匹配的相关度排序（名称完全匹配优先）
     * - 相同相关度按ID升序排列
     *
     * @param product 查询条件对象，包含筛选条件
     * @return 匹配的商品列表
     */
    List<Product> findByCondition(@Param("product") Product product);
    
    /**
     * 根据商品ID查询商品
     *
     * @param id 商品ID
     * @return 商品对象，不存在则返回null
     */
    Product findById(@Param("id") Integer id);
}
