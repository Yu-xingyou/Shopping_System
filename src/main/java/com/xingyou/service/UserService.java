package com.xingyou.service;

import com.xingyou.entity.people.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    
    User login(String userId, String password);
    
    String register(User user);
    
    User findByUserId(String userId);
}
