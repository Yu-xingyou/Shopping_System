package com.xingyou.mapper;

import com.xingyou.entity.shopping.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    
    List<Favorite> findByUserId(@Param("userId") String userId);
    
    Favorite findByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Integer productId);
    
    List<Favorite> findByProductId(@Param("productId") Integer productId);
    
    int insert(Favorite favorite);
    
    int deleteByUserIdAndProductId(@Param("userId") String userId, @Param("productId") Integer productId);
}
