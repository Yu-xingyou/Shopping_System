package com.xingyou.service.impl;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.StaffMapper;
import com.xingyou.service.StaffService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StaffServiceImpl implements StaffService {
    
    @Resource
    private StaffMapper staffMapper;
    
    @Override
    public Staff login(Integer staffId, String password) {
        Staff staff = staffMapper.findById(staffId);
        
        if (staff == null) {
            throw new BusinessException(401, "员工 ID 不存在");
        }
        
        if (!password.equals(staff.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }
        
        if (staff.getStatus() != null && staff.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }
        
        // 不要在登录时清除密码，否则前端无法获取完整的用户信息
        // staff.setPassword(null);
        return staff;
    }
    
    @Override
    public List<User> findAllUsers() {
        return staffMapper.findAllUsers();
    }
    
    @Override
    public User findUserByUserId(String userId) {
        User user = staffMapper.findUserByUserId(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
    
    @Override
    public List<User> findUserByName(String name) {
        return staffMapper.findUserByName(name);
    }
}
