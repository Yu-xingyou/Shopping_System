package com.xingyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xingyou.entity.shopping.Product;
import com.xingyou.mapper.ProductMapper;
import com.xingyou.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
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
    @Override
    public Map<String, Object> findAll(Integer pageNum, Integer pageSize) {
        // 启动PageHelper分页插件，设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        
        // 查询商品列表（PageHelper会自动添加LIMIT语句）
        List<Product> products = productMapper.findAll();
        
        // 封装分页信息，包括总记录数、总页数等
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        
        // 构建返回结果Map，包含商品列表和分页元数据
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
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
    @Override
    public Map<String, Object> findByCondition(Product product, Integer pageNum, Integer pageSize) {
        // 启动PageHelper分页插件，设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        
        // 根据条件查询商品列表
        List<Product> products = productMapper.findByCondition(product);
        
        // 封装分页信息，包括总记录数、总页数等
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        
        // 构建返回结果Map，包含商品列表和分页元数据
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    /**
     * 根据商品ID查询商品
     * 
     * 根据指定的商品ID查询商品详细信息。
     *
     * @param id 商品ID，用于指定需要查询的商品
     * @return Product 返回商品对象，如果商品不存在则返回null
     */
    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }

}
