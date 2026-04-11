package com.xingyou.entity.people;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工实体类
 * 用于存储系统员工（如客服、配送员等）的基本信息和账户数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    /**
     * 员工ID，主键，自增
     */
    private Integer staffId;
    
    /**
     * 员工姓名
     */
    private String name;
    
    /**
     * 登录密码
     */
    private String password;
    
    /**
     * 员工状态：0-禁用，1-正常
     */
    private Integer status;
}
