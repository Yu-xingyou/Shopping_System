package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.UserMapper;
import com.xingyou.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    
    @Resource
    private UserMapper userMapper;
    
    @Override
    public User login(String userId, String password) {
        User user = userMapper.findByUserId(userId);
        
        if (user == null) {
            throw new BusinessException(401, "账号不存在");
        }
        
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }
        
        user.setPassword(null);
        return user;
    }
    
    @Override
    public String register(User user) {
        String userId = generateUserId();
        
        while (true) {
            User existingUser = userMapper.findByUserId(userId);
            if (existingUser == null) {
                break;
            }
            userId = generateUserId();
        }
        
        user.setUserId(userId);
        
        if (user.getSex() == null || user.getSex().trim().isEmpty()) {
            user.setSex("未知");
        }
        user.setMoney(0.0);
        
        int rows = userMapper.insert(user);
        if (rows != 1) {
            throw new BusinessException(500, "注册失败，请稍后重试");
        }
        
        return userId;
    }
    
    @Override
    public User findByUserId(String userId) {
        User user = userMapper.findByUserId(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
    
    private String generateUserId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf(new Random().nextInt(9000) + 1000);
        return "U" + timestamp + random;
    }
}
