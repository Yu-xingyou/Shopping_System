package com.xingyou.service;

import com.xingyou.entity.shopping.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    
    Map<String, Object> findAll(Integer pageNum, Integer pageSize);
    
    Map<String, Object> findByCondition(Product product, Integer pageNum, Integer pageSize);
    
    Product findById(Integer id);
}
