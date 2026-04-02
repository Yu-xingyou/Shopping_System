package com.xingyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xingyou.entity.shopping.Product;
import com.xingyou.mapper.ProductMapper;
import com.xingyou.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Resource
    private ProductMapper productMapper;
    
    @Override
    public Map<String, Object> findAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.findAll();
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public Map<String, Object> findByCondition(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.findByCondition(product);
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", products);
        result.put("total", pageInfo.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        
        return result;
    }
    
    @Override
    public Product findById(Integer id) {
        return productMapper.findById(id);
    }
}
