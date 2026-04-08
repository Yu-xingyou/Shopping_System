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
    
    /**
     * 新增管理员
     *
     * @param admin 管理员对象，包含待插入的管理员信息
     * @return 受影响的行数
     */
    int insert(Admin admin);
    
    /**
     * 根据管理员ID查询管理员信息
     *
     * @param adminId 管理员ID
     * @return 管理员对象，不存在则返回null
     */
    Admin findByAdminId(@Param("adminId") Integer adminId);
    
    /**
     * 选择性更新管理员信息
     * 
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     *
     * @param admin 管理员对象，包含待更新的信息
     * @return 受影响的行数
     */
    int update(Admin admin);
    
    /**
     * 查询所有用户列表（管理员权限）
     *
     * @return 系统中所有用户的列表
     */
    List<User> findAllUsers();
    
    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户对象，不存在则返回null
     */
    User findUserByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户名模糊查询用户
     *
     * @param name 用户名（支持模糊匹配）
     * @return 匹配的用户列表
     */
    List<User> findUserByName(@Param("name") String name);
    
    /**
     * 查询所有员工列表（管理员权限）
     *
     * @return 系统中所有员工的列表
     */
    List<Staff> findAllStaffs();
    
    /**
     * 根据员工ID查询员工信息
     *
     * @param staffId 员工ID
     * @return 员工对象，不存在则返回null
     */
    Staff findStaffByStaffId(@Param("staffId") Integer staffId);
    
    /**
     * 根据员工姓名模糊查询员工
     *
     * @param name 员工姓名（支持模糊匹配）
     * @return 匹配的员工列表
     */
    List<Staff> findStaffByName(@Param("name") String name);
    
    /**
     * 新增员工
     *
     * @param staff 员工对象，包含待插入的员工信息
     */
    void addStaff(Staff staff);
    
    /**
     * 选择性更新员工信息
     * 
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     *
     * @param staff 员工对象，包含待更新的信息
     */
    void updateStaff(Staff staff);
    
    /**
     * 更新商品库存（直接设置库存值）
     *
     * @param id 商品ID
     * @param stock 新的库存数量
     */
    void updateProductStock(@Param("id") Integer id, @Param("stock") Integer stock);
    
    /**
     * 查询所有订单列表（管理员权限）
     *
     * @return 系统中所有订单的列表
     */
    List<Order> findAllOrders();
}
