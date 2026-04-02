package com.xingyou.service;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StaffService {
    
    Staff login(Integer staffId, String password);
    
    List<User> findAllUsers();
    
    User findUserByUserId(String userId);
    
    List<User> findUserByName(String name);
}
