package com.xingyou.service;

import com.xingyou.entity.people.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 员工服务层接口
 * 
 * 提供员工相关的业务逻辑，包括员工登录、信息更新，
 * 以及员工对用户管理的查询功能。
 */
@Service
public interface StaffService {
    
    /**
     * 员工登录
     * 
     * 验证员工ID、密码和账号状态，校验通过后生成JWT令牌并返回员工信息。
     * 只有状态为1（正常）的员工才能登录系统。
     *
     * @param staffId 员工ID，用于标识登录的员工
     * @param password 密码，用于验证员工身份
     * @return Map<String, Object> 登录成功时返回包含员工信息和JWT令牌的Map:
     *         - user: 员工用户对象
     *         - token: JWT认证令牌
     */
    Map<String, Object> login(String staffId, String password);
    
    /**
     * 更新员工信息
     * 
     * 验证员工存在性后，选择性更新员工的姓名和密码信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * 更新后会验证受影响的行数，确保操作成功。
     *
     * @param staffId 员工ID，用于指定需要更新的员工
     * @param staff 员工用户对象，包含待更新的信息（姓名、密码），只有非空字段会被更新
     */
    void update(String staffId, User staff);
    
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
}
