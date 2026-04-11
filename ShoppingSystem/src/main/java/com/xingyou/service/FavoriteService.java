package com.xingyou.service;

import com.xingyou.entity.shopping.Favorite;
import com.xingyou.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏服务层接口
 * 提供收藏管理的业务逻辑，包括添加收藏、取消收藏、查询收藏等功能
 */
@Service
public interface FavoriteService {
    
    /**
     * 根据用户ID查询收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<Favorite> findByUserId(String userId);
    
    /**
     * 根据商品ID查询收藏了该商品的所有用户记录
     *
     * @param productId 商品ID
     * @return 收藏该商品的所有用户记录
     */
    List<Favorite> findByProductId(Integer productId);
    
    /**
     * 添加收藏
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @throws BusinessException 当参数校验失败或商品已在收藏夹中时抛出异常
     */
    void addFavorite(String userId, Integer productId);
    
    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @throws BusinessException 当参数校验失败或收藏记录不存在时抛出异常
     */
    void removeFavorite(String userId, Integer productId);
    
    /**
     * 检查用户是否已收藏该商品
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return true-已收藏，false-未收藏
     */
    boolean isFavorited(String userId, Integer productId);
}
