package com.xingyou.service;

import com.xingyou.entity.people.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务层接口
 * 
 * 提供用户相关的业务逻辑，包括用户注册、登录、信息查询和更新等功能。
 */
@Service
public interface UserService {
    
    /**
     * 用户登录
     * 
     * 验证用户ID和密码的合法性，校验通过后返回用户信息。
     * 为安全起见，返回前会清除密码字段。
     *
     * @param userId 用户ID，用于标识登录的用户
     * @param password 密码，用于验证用户身份
     * @return User 登录成功时返回用户对象（密码字段已清空）
     */
    User login(String userId, String password);
    
    /**
     * 用户注册
     * 
     * 生成唯一的用户ID，设置默认属性（性别默认为"未知"，余额默认为0.0），
     * 并将新用户信息保存到数据库中。通过循环检查确保生成的用户ID不重复。
     *
     * @param user 用户对象，包含待注册的用户基本信息（姓名、密码等）
     * @return String 返回生成的唯一用户ID
     */
    String register(User user);
    
    /**
     * 根据用户ID查询用户信息
     * 
     * 查询指定用户ID的用户信息，为安全起见会清除密码字段后返回。
     *
     * @param userId 用户ID，用于指定需要查询的用户
     * @return User 返回用户对象，如果用户不存在则返回null；密码字段会被设置为null以保证安全
     */
    User findByUserId(String userId);
    
    /**
     * 更新用户信息
     * 
     * 验证用户存在性后，选择性更新用户的姓名、性别和密码信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * 更新后会验证受影响的行数，确保操作成功。
     *
     * @param userId 用户ID，用于指定需要更新的用户
     * @param user 用户对象，包含待更新的信息（姓名、性别、密码），只有非空字段会被更新
     */
    void update(String userId, User user);
}
