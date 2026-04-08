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
        
        if (staff.getStatus() == null || staff.getStatus() != 1) {
            String statusMsg = getStatusMessage(staff.getStatus());
            throw new BusinessException(403, "登录失败：" + statusMsg);
        }
        
        return staff;
    }
    
    private String getStatusMessage(Integer status) {
        if (status == null) {
            return "账号状态异常";
        }
        switch (status) {
            case 2:
                return "账号已冻结，无法登录";
            case 3:
                return "账号已离职，无法登录";
            default:
                return "账号状态异常";
        }
    }
    
    @Override
    public void update(Integer staffId, Staff staff) {
        Staff existingStaff = staffMapper.findByStaffId(staffId);
        if (existingStaff == null) {
            throw new BusinessException(404, "员工不存在");
        }
        
        if (staff.getName() != null) {
            existingStaff.setName(staff.getName());
        }
        if (staff.getPassword() != null && !staff.getPassword().trim().isEmpty()) {
            existingStaff.setPassword(staff.getPassword());
        }
        
        int rows = staffMapper.update(existingStaff);
        if (rows != 1) {
            throw new BusinessException(500, "更新失败，请稍后重试");
        }
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
