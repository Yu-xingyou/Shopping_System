package com.xingyou.mapper;

import com.xingyou.entity.people.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据访问层接口
 * 
 * 提供用户注册、登录、信息查询和更新等基础功能。
 */
@Mapper
public interface UserMapper {
    
    /**
     * 新增用户（用户注册）
     *
     * @param user 用户对象，包含待插入的用户信息
     * @return 受影响的行数
     */
    int insert(User user);
    
    /**
     * 根据用户ID查询用户信息（用于登录验证）
     *
     * @param userId 用户ID
     * @return 用户对象，不存在则返回null
     */
    User findByUserId(@Param("userId") String userId);
    
    /**
     * 选择性更新用户信息
     * 
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * 支持更新姓名、性别、密码三个字段。
     *
     * @param user 用户对象，包含待更新的信息
     * @return 受影响的行数
     */
    int update(User user);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱地址
     * @return 用户对象
     */
    User findByEmail(@Param("email") String email);
}
