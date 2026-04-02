package com.xingyou.service;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    
    List<User> findAllUsers();
    
    User findUserByUserId(String userId);
    
    List<User> findUserByName(String name);
    
    List<Staff> findAllStaffs();
    
    Staff findStaffByStaffId(Integer staffId);
    
    List<Staff> findStaffByName(String name);
    
    void addStaff(Staff staff);
    
    void updateStaff(Staff staff);
    
    void updateProductStock(Integer id, Integer stock);
}
