package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.shopping.Product;
import com.xingyou.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/product")
@RestController
public class ProductController {
    
    @Resource
    private ProductService productService;
    
    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Map<String, Object> result = productService.findAll(pageNum, pageSize);
        return Result.success(result);
    }
    
    @PostMapping("/search")
    public Result search(
            @RequestBody Product product,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Map<String, Object> result = productService.findByCondition(product, pageNum, pageSize);
        return Result.success(result);
    }
    
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }
        return Result.success(product);
    }
}
