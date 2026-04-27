package com.xingyou.mapper;

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
    
    /**
     * 根据管理员ID查询管理员信息
     * @param adminId 管理员ID
     * @return 管理员用户对象
     */
    User findByAdminId(@Param("adminId") String adminId);
    
    /**
     * 查询所有普通用户
     * @return 普通用户列表
     */
    List<User> findAllUsers();
    
    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    User findUserByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户名模糊查询用户
     * @param name 用户名
     * @return 用户列表
     */
    List<User> findUserByName(@Param("name") String name);
    
    /**
     * 查询所有员工
     * @return 员工列表
     */
    List<User> findAllStaffs();
    
    /**
     * 根据员工ID查询员工信息
     * @param staffId 员工ID
     * @return 员工用户对象
     */
    User findStaffByStaffId(@Param("staffId") String staffId);
    
    /**
     * 根据员工姓名模糊查询
     * @param name 员工姓名
     * @return 员工列表
     */
    List<User> findStaffByName(@Param("name") String name);
    
    /**
     * 查询所有订单
     * @return 订单列表
     */
    List<Order> findAllOrders();
    
    /**
     * 添加员工
     * @param staff 员工用户对象
     */
    void addStaff(User staff);
    
    /**
     * 更新员工信息
     * @param staff 员工用户对象
     */
    void updateStaff(User staff);
    
    /**
     * 更新商品库存
     * @param id 商品ID
     * @param stock 库存数量
     */
    void updateProductStock(@Param("id") Integer id, @Param("stock") Integer stock);
    
    /**
     * 更新管理员信息
     * @param admin 管理员用户对象
     */
    void update(User admin);
    
    /**
     * 查询已完成订单的总收益（按时间范围筛选）
     * 
     * 只统计状态为3（已完成/已收货）的订单总金额。
     *
     * @param days 时间范围（天数），null表示查询全部
     * @return 总收益金额，如果没有订单则返回0
     */
    Double getTotalRevenue(@Param("days") Integer days);
    
    /**
     * 查询所有管理员列表
     * @return 管理员列表
     */
    List<User> findAllAdmins();
}
