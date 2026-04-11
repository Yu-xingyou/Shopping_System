package com.xingyou.entity.people;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * 用于存储购物系统普通用户的基本信息和账户数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * 用户ID，主键
     */
    private String userId;
    
    /**
     * 用户姓名
     */
    private String name;
    
    /**
     * 登录密码
     */
    private String password;
    
    /**
     * 性别
     */
    private String sex;
    
    /**
     * 账户余额
     */
    private Double money;
}