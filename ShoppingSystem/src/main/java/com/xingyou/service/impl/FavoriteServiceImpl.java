package com.xingyou.service.impl;

import com.xingyou.entity.shopping.Favorite;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.FavoriteMapper;
import com.xingyou.service.FavoriteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    @Override
    public boolean isFavorited(String userId, Integer productId) {
        Favorite favorite = favoriteMapper.findByUserIdAndProductId(userId, productId);
        return favorite != null;
    }
}
