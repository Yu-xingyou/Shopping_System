package com.xingyou.mapper;

import com.xingyou.entity.people.Admin;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员数据访问层接口
 * 
 * 提供管理员相关的数据库操作，包括管理员登录、信息管理，
 * 以及管理员对用户、员工、订单的管理功能。
 */
@Mapper
public interface AdminMapper {
    
    void insert(Admin admin);
    
    Admin findByAdminId(@Param("adminId") Integer adminId);
    
    List<User> findAllUsers();
    
    User findUserByUserId(@Param("userId") String userId);
    
    List<User> findUserByName(@Param("name") String name);
    
    List<Staff> findAllStaffs();
    
    Staff findStaffByStaffId(@Param("staffId") Integer staffId);
    
    List<Staff> findStaffByName(@Param("name") String name);
    
    List<Order> findAllOrders();
    
    void addStaff(Staff staff);
    
    void updateStaff(Staff staff);
    
    void updateProductStock(@Param("id") Integer id, @Param("stock") Integer stock);
    
    void update(Admin admin);
    
    /**
     * 查询已完成订单的总收益（按时间范围筛选）
     * 
     * 只统计状态为3（已完成/已收货）的订单总金额。
     *
     * @param days 时间范围（天数），null表示查询全部
     * @return 总收益金额，如果没有订单则返回0
     */
    Double getTotalRevenue(@Param("days") Integer days);
}
