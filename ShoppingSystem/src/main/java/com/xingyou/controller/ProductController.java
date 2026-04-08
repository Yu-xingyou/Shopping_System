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
    
    /**
     * 分页查询商品列表
     * 
     * @param pageNum 页码,默认为1
     * @param pageSize 每页数量,默认为10
     * @return Result 返回分页后的商品列表数据
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Map<String, Object> result = productService.findAll(pageNum, pageSize);
        return Result.success(result);
    }
    
    /**
     * 条件搜索商品(支持分页)
     * 
     * @param product 搜索条件对象,包含商品名称、分类等筛选条件
     * @param pageNum 页码,默认为1
     * @param pageSize 每页数量,默认为10
     * @return Result 返回符合条件的分页商品列表数据
     */
    @PostMapping("/search")
    public Result search(
            @RequestBody Product product,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Map<String, Object> result = productService.findByCondition(product, pageNum, pageSize);
        return Result.success(result);
    }
    
    /**
     * 根据商品ID查询商品详情
     * 
     * @param id 商品ID
     * @return Result 返回商品详情信息,如果商品不存在则返回404错误
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }
        return Result.success(product);
    }
}
