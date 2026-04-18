package com.xingyou.entity.people;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 密码重置Token实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 重置Token（UUID）
     */
    private String token;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 是否已使用：0-未使用，1-已使用
     */
    private Integer used;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
