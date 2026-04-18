package com.xingyou.service;

/**
 * 密码重置服务层
 */
public interface PasswordResetService {
    
    /**
     * 发送重置密码邮件
     *
     * @param email 用户邮箱
     * @return 是否发送成功
     */
    boolean sendResetEmail(String email);
    
    /**
     * 重置密码
     *
     * @param token 重置Token
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(String token, String newPassword);
}
