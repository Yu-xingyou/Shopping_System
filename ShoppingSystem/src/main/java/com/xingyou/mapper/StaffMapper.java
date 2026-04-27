package com.xingyou.mapper;

import com.xingyou.entity.people.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工数据访问层接口
 * 
 * 提供员工登录、信息查询、更新等功能，
 * 同时包含员工对用户管理的查询操作。
 */
@Mapper
public interface StaffMapper {
    
    /**
     * 根据员工ID查询员工信息（用于业务逻辑）
     *
     * @param staffId 员工ID
     * @return 员工用户对象，不存在则返回null
     */
    User findByStaffId(@Param("staffId") String staffId);
    
    /**
     * 根据员工ID查询员工信息（用于登录验证）
     *
     * @param staffId 员工ID
     * @return 员工用户对象，不存在则返回null
     */
    User findById(@Param("staffId") String staffId);
    
    /**
     * 选择性更新员工信息
     * 
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     *
     * @param staff 员工用户对象，包含待更新的信息
     * @return 受影响的行数
     */
    int update(User staff);
    
    /**
     * 查询所有用户列表（员工权限）
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
     * 查询所有员工列表
     *
     * @return 系统中所有员工的列表
     */
    List<User> findAllStaffs();
}
