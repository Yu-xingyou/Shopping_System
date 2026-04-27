package com.xingyou.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码编码工具类
 * 
 * 使用 Spring Security 的 BCryptPasswordEncoder 进行密码加密和验证。
 * BCrypt 是一种安全的密码哈希算法，具有自动加盐的特性。
 */
@Component
public class PasswordEncoderUtil {
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public PasswordEncoderUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * 对明文密码进行加密
     * 
     * @param rawPassword 明文密码
     * @return String 加密后的密码（BCrypt 哈希值）
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * 验证明文密码是否与加密后的密码匹配
     * 
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码（BCrypt 哈希值）
     * @return boolean true-密码匹配，false-密码不匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
