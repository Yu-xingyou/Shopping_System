package com.xingyou.mapper;

import com.xingyou.entity.people.Admin;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    
    int insert(Admin admin);
    
    Admin findByAdminId(@Param("adminId") Integer adminId);
    
    int update(Admin admin);
    
    List<User> findAllUsers();
    
    User findUserByUserId(@Param("userId") String userId);
    
    List<User> findUserByName(@Param("name") String name);
    
    List<Staff> findAllStaffs();
    
    Staff findStaffByStaffId(@Param("staffId") Integer staffId);
    
    List<Staff> findStaffByName(@Param("name") String name);
    
    void addStaff(Staff staff);
    
    void updateStaff(Staff staff);
    
    void updateProductStock(@Param("id") Integer id, @Param("stock") Integer stock);
    
    List<Order> findAllOrders();
}
