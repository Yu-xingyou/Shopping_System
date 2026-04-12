package com.xingyou.service;

import com.xingyou.entity.people.Admin;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 管理员服务层接口
 * 
 * 提供管理员相关的业务逻辑，包括管理员登录、信息管理，
 * 以及管理员对用户、员工、订单的管理功能。
 */
@Service
public interface AdminService {
    
    /**
     * 查询所有用户
     * 
     * 获取系统中所有的用户列表信息。
     *
     * @return List<User> 返回所有用户的列表，如果没有用户则返回空列表
     */
    List<User> findAllUsers();
    
    /**
     * 根据用户ID查询用户信息
     * 
     * 查询指定用户ID的用户信息，为安全起见会清除密码字段后返回。
     *
     * @param userId 用户ID，用于指定需要查询的用户
     * @return User 返回用户对象，如果用户不存在则返回null；密码字段会被设置为null以保证安全
     */
    User findUserByUserId(String userId);
    
    /**
     * 根据用户名模糊查询用户
     * 
     * 根据提供的用户名进行模糊匹配查询，返回所有匹配的用户列表。
     *
     * @param name 用户名（支持模糊匹配），用于搜索包含该名称的用户
     * @return List<User> 返回匹配的用户列表，如果没有匹配结果则返回空列表
     */
    List<User> findUserByName(String name);
    
    /**
     * 查询所有员工
     * 
     * 获取系统中所有的员工列表信息。
     *
     * @return List<Staff> 返回所有员工的列表，如果没有员工则返回空列表
     */
    List<Staff> findAllStaffs();
    
    /**
     * 根据员工ID查询员工信息
     * 
     * 查询指定员工ID的员工信息，为安全起见会清除密码字段后返回。
     *
     * @param staffId 员工ID，用于指定需要查询的员工
     * @return Staff 返回员工对象，如果员工不存在则返回null；密码字段会被设置为null以保证安全
     */
    Staff findStaffByStaffId(Integer staffId);
    
    /**
     * 根据员工姓名模糊查询员工
     * 
     * 根据提供的员工姓名进行模糊匹配查询，返回所有匹配的员工列表。
     *
     * @param name 员工姓名（支持模糊匹配），用于搜索包含该姓名的员工
     * @return List<Staff> 返回匹配的员工列表，如果没有匹配结果则返回空列表
     */
    List<Staff> findStaffByName(String name);
    
    /**
     * 新增员工
     * 
     * 验证员工信息的合法性后，将新员工添加到系统中。
     * 默认状态设置为启用（status=1）。
     *
     * @param staff 员工对象，包含待添加的员工信息，必须包含姓名和密码
     */
    void addStaff(Staff staff);
    
    /**
     * 更新员工信息
     * 
     * 验证员工ID和状态信息的合法性后，更新系统中已有员工的信息。
     * 员工状态必须在有效范围内（1-正常、2-冻结、3-离职）。
     *
     * @param staff 员工对象，包含待更新的员工信息，必须包含有效的员工ID
     */
    void updateStaff(Staff staff);
    
    /**
     * 更新商品库存
     * 
     * 验证商品ID和库存数量的合法性后，更新指定商品的库存信息。
     * 库存数量必须为非负数。
     *
     * @param id 商品ID，用于指定需要更新库存的商品
     * @param stock 新的库存数量，必须大于等于0
     */
    void updateProductStock(Integer id, Integer stock);
    
    /**
     * 管理员登录
     * 
     * 验证管理员ID和密码的合法性,校验通过后生成JWT令牌并返回管理员信息。
     * 包括参数校验、用户存在性验证和密码匹配验证。
     *
     * @param adminId 管理员ID,用于标识登录的管理员
     * @param password 密码,用于验证管理员身份
     * @return Map<String, Object> 登录成功时返回包含管理员信息和JWT令牌的Map:
     *         - admin: 管理员对象(密码已清除)
     *         - token: JWT认证令牌
     */
    Map<String, Object> login(Integer adminId, String password);
    
    /**
     * 查询所有订单
     * 
     * 获取系统中所有的订单列表信息。
     *
     * @return List<Order> 返回所有订单的列表，如果没有订单则返回空列表
     */
    List<Order> findAllOrders();
    
    /**
     * 更新管理员信息
     * 
     * 验证管理员是否存在后，选择性更新管理员的姓名和密码信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     *
     * @param adminId 管理员ID，用于指定需要更新的管理员
     * @param admin 管理员对象，包含待更新的信息（姓名、密码），只有非空字段会被更新
     */
    void update(Integer adminId, Admin admin);
    
    /**
     * 查询管理员总收益（按时间范围筛选）
     * 
     * 只统计状态为3（已完成/已收货）的订单总金额。
     *
     * @param days 时间范围（天数），可选参数：
     *             - null: 查询全部时间
     *             - 1: 1天内
     *             - 7: 7天内
     *             - 30: 一个月内
     * @return Double 返回总收益金额，如果没有订单则返回0.0
     */
    Double getTotalRevenue(Integer days);
}
