package com.xingyou.mapper;

import com.xingyou.entity.shopping.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    
    List<Product> findAll();
    
    List<Product> findByCondition(@Param("product") Product product);
    
    Product findById(@Param("id") Integer id);
}
