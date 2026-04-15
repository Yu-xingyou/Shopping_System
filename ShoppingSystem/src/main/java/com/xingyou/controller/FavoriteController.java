package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.shopping.Favorite;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏控制器
 * 提供收藏相关的REST API接口，包括查询收藏、添加收藏、取消收藏等功能
 */
@RequestMapping("/favorite")
@RestController
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 查询用户收藏列表
     *
     * @param userId 用户ID
     * @return Result 返回收藏列表
     */
    @GetMapping("/list")
    public Result getFavorites(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }
        
        try {
            List<Favorite> favorites = favoriteService.findByUserId(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            return Result.error(500, "查询收藏列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 添加收藏
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return Result 返回操作结果
     */
    @PostMapping("/add")
    public Result addFavorite(@RequestParam String userId, @RequestParam Integer productId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }
        if (productId == null) {
            return Result.error(400, "商品ID不能为空");
        }
        
        try {
            favoriteService.addFavorite(userId, productId);
            return Result.success("收藏成功");
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "收藏失败：" + e.getMessage());
        }
    }
    
    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return Result 返回操作结果
     */
    @DeleteMapping("/remove")
    public Result removeFavorite(@RequestParam String userId, @RequestParam Integer productId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }
        if (productId == null) {
            return Result.error(400, "商品ID不能为空");
        }
        
        try {
            favoriteService.removeFavorite(userId, productId);
            return Result.success("取消收藏成功");
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "取消收藏失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查用户是否已收藏该商品
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return Result 返回是否已收藏的布尔值
     */
    @GetMapping("/check")
    public Result checkFavorite(@RequestParam String userId, @RequestParam Integer productId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }
        if (productId == null) {
            return Result.error(400, "商品ID不能为空");
        }
        
        try {
            boolean isFavorited = favoriteService.isFavorited(userId, productId);
            return Result.success(isFavorited);
        } catch (Exception e) {
            return Result.error(500, "检查收藏状态失败：" + e.getMessage());
        }
    }
}
