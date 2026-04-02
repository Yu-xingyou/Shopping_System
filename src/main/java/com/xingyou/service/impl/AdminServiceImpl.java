package com.xingyou.service.impl;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.AdminMapper;
import com.xingyou.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public List<User> findAllUsers() {
        return adminMapper.findAllUsers();
    }

    @Override
    public User findUserByUserId(String userId) {
        User user = adminMapper.findUserByUserId(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    public List<User> findUserByName(String name) {
        return adminMapper.findUserByName(name);
    }

    @Override
    public List<Staff> findAllStaffs() {
        return adminMapper.findAllStaffs();
    }

    @Override
    public Staff findStaffByStaffId(Integer staffId) {
        Staff staff = adminMapper.findStaffByStaffId(staffId);
        if (staff != null) {
            staff.setPassword(null);
        }
        return staff;
    }

    @Override
    public List<Staff> findStaffByName(String name) {
        return adminMapper.findStaffByName(name);
    }

    @Override
    public void addStaff(Staff staff) {
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            throw new BusinessException(400, "员工姓名不能为空");
        }
        
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new BusinessException(400, "密码不能为空");
        }
        
        if (staff.getPassword().length() < 6) {
            throw new BusinessException(400, "密码长度不能少于 6 位");
        }
        
        if (staff.getStatus() == null) {
            staff.setStatus(1);
        }
        
        int rows = adminMapper.insertStaff(staff);
        if (rows != 1) {
            throw new BusinessException(500, "添加员工失败，请稍后重试");
        }
    }

    @Override
    public void updateStaff(Staff staff) {
        if (staff.getStaffId() == null) {
            throw new BusinessException(400, "员工 ID 不能为空");
        }
        
        Staff existingStaff = adminMapper.findStaffByStaffId(staff.getStaffId());
        if (existingStaff == null) {
            throw new BusinessException(404, "员工不存在");
        }
        
        if (staff.getStatus() != null && (staff.getStatus() < 0 || staff.getStatus() > 1)) {
            throw new BusinessException(400, "员工状态必须是 0 或 1");
        }
        
        int rows = adminMapper.updateStaff(staff);
        if (rows != 1) {
            throw new BusinessException(500, "更新员工信息失败，请稍后重试");
        }
    }

    @Override
    public void updateProductStock(Integer id, Integer stock) {
        if (id == null) {
            throw new BusinessException(400, "商品 ID 不能为空");
        }
        
        if (stock == null || stock < 0) {
            throw new BusinessException(400, "库存数量不能为负数");
        }
        
        int rows = adminMapper.updateProductStock(id, stock);
        if (rows != 1) {
            throw new BusinessException(500, "更新库存失败，请稍后重试");
        }
    }
}

