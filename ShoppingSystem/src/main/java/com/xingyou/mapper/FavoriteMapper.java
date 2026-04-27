package com.xingyou.mapper;

import com.xingyou.entity.shopping.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏数据访问层接口
 * 提供收藏记录的查询、添加和删除等数据库操作
 */
@Mapper
public interface FavoriteMapper {
    
    /**
     * 根据用户ID查询收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<Favorite> findByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户ID和商品ID查询收藏记录
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 收藏记录，不存在则返回null
     */
    Favorite findByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Integer productId);
    
    /**
     * 根据商品ID查询所有收藏了该商品的用户记录
     *
     * @param productId 商品ID
     * @return 收藏该商品的所有用户记录
     */
    List<Favorite> findByProductId(@Param("productId") Integer productId);
    
    /**
     * 插入收藏记录
     *
     * @param favorite 收藏对象
     * @return 影响行数
     */
    int insert(Favorite favorite);
    
    /**
     * 删除收藏记录
     *
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 影响行数
     */
    int deleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Integer productId);
}
