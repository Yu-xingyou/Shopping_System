package com.xingyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xingyou.entity.shopping.Product;
import com.xingyou.mapper.ProductMapper;
import com.xingyou.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
        log.info("分页查询所有商品请求 - pageNum: {}, pageSize: {}", pageNum, pageSize);
        
        PageHelper.startPage(pageNum, pageSize);
        
        List<Product> products = productMapper.findAll();
        
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        log.info("分页查询商品完成 - 返回数量: {}, 总数: {}", products.size(), pageInfo.getTotal());
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
        log.info("条件分页查询商品请求 - pageNum: {}, pageSize: {}", pageNum, pageSize);
        log.debug("查询条件 - name: {}, category: {}, minPrice: {}, maxPrice: {}", 
                product.getName(), product.getCategory(), product.getMinPrice(), product.getMaxPrice());
        
        PageHelper.startPage(pageNum, pageSize);
        
        List<Product> products = productMapper.findByCondition(product);
        
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        log.info("条件分页查询商品完成 - 返回数量: {}, 总数: {}", products.size(), pageInfo.getTotal());
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
        log.debug("根据ID查询商品 - id: {}", id);
        Product product = productMapper.findById(id);
        
        if (product != null) {
            log.debug("查询到商品信息 - id: {}, name: {}, stock: {}", id, product.getName(), product.getStock());
        } else {
            log.warn("商品不存在 - id: {}", id);
        }
        
        return product;
    }

}
