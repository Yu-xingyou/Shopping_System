package com.xingyou.service.impl;

import com.xingyou.entity.shopping.Favorite;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.FavoriteMapper;
import com.xingyou.service.FavoriteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏服务层实现类
 * 实现收藏管理的业务逻辑，包括添加收藏、取消收藏、查询收藏等功能
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Resource
    private FavoriteMapper favoriteMapper;
    
    @Override
    public List<Favorite> findByUserId(String userId) {
        return favoriteMapper.findByUserId(userId);
    }
    
    @Override
    public List<Favorite> findByProductId(Integer productId) {
        return favoriteMapper.findByProductId(productId);
    }
    
    /**
     * 添加收藏
     * 
     * 验证用户ID和商品ID后，检查该商品是否已在收藏夹中，
     * 如果未收藏则插入收藏记录。
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @throws BusinessException 当参数校验失败或商品已在收藏夹中时抛出异常：
     *         - 400: 用户ID不能为空
     *         - 400: 商品ID不能为空
     *         - 400: 该商品已在收藏夹中
     *         - 500: 收藏失败，请稍后重试
     */
    @Override
    public void addFavorite(String userId, Integer productId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        if (productId == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        
        Favorite existing = favoriteMapper.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            throw new BusinessException(400, "该商品已在收藏夹中");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        
        int rows = favoriteMapper.insert(favorite);
        if (rows != 1) {
            throw new BusinessException(500, "收藏失败，请稍后重试");
        }
    }
    
    /**
     * 取消收藏
     * 
     * 验证用户ID和商品ID后，删除指定的收藏记录。
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @throws BusinessException 当参数校验失败或收藏记录不存在时抛出异常：
     *         - 400: 用户ID不能为空
     *         - 400: 商品ID不能为空
     *         - 404: 收藏记录不存在
     */
    @Override
    public void removeFavorite(String userId, Integer productId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessException(400, "用户ID不能为空");
        }
        if (productId == null) {
            throw new BusinessException(400, "商品ID不能为空");
        }
        
        int rows = favoriteMapper.deleteByUserIdAndProductId(userId, productId);
        if (rows != 1) {
            throw new BusinessException(404, "收藏记录不存在");
        }
    }
    
    /**
     * 检查用户是否已收藏该商品
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return true-已收藏，false-未收藏
     */
    @Override
    public boolean isFavorited(String userId, Integer productId) {
        Favorite favorite = favoriteMapper.findByUserIdAndProductId(userId, productId);
        return favorite != null;
    }
}
