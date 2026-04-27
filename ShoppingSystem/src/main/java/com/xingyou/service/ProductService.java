package com.xingyou.service;

import com.xingyou.entity.shopping.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品服务层接口
 * 
 * 提供商品的查询业务逻辑，支持分页查询、条件筛选等功能。
 */
@Service
public interface ProductService {
    
    /**
     * 分页查询所有商品
     * 
     * 使用PageHelper插件实现商品列表的分页查询，返回包含商品列表和分页信息的Map对象。
     *
     * @param pageNum 页码，从1开始，指定需要查询的页数
     * @param pageSize 每页大小，指定每页返回的商品数量
     * @return Map<String, Object> 返回包含以下键值对的分页结果：
     *         - "list": 当前页的商品列表
     *         - "total": 总记录数
     *         - "pageNum": 当前页码
     *         - "pageSize": 每页大小
     */
    Map<String, Object> findAll(Integer pageNum, Integer pageSize);
    
    /**
     * 根据条件分页查询商品
     * 
     * 使用PageHelper插件实现商品的条件筛选和分页查询，支持按商品属性（如名称、分类、价格等）进行模糊或精确匹配。
     * 返回包含商品列表和分页信息的Map对象。
     *
     * @param product 查询条件对象，包含需要筛选的商品属性（非空字段作为查询条件）
     * @param pageNum 页码，从1开始，指定需要查询的页数
     * @param pageSize 每页大小，指定每页返回的商品数量
     * @return Map<String, Object> 返回包含以下键值对的分页结果：
     *         - "list": 当前页符合条件的商品列表
     *         - "total": 总记录数
     *         - "pageNum": 当前页码
     *         - "pageSize": 每页大小
     */
    Map<String, Object> findByCondition(Product product, Integer pageNum, Integer pageSize);
    
    /**
     * 根据商品ID查询商品
     * 
     * 根据指定的商品ID查询商品详细信息。
     *
     * @param id 商品ID，用于指定需要查询的商品
     * @return Product 返回商品对象，如果商品不存在则返回null
     */
    Product findById(Integer id);
}
