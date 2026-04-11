package com.xingyou.service;

import com.xingyou.entity.shopping.Favorite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {
    
    List<Favorite> findByUserId(String userId);
    
    void addFavorite(String userId, Integer productId);
    
    void removeFavorite(String userId, Integer productId);
    
    boolean isFavorited(String userId, Integer productId);
}
