package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 密码重置控制器
 */
@RestController
@RequestMapping("/user/password")
public class PasswordResetController {
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    /**
     * 发送重置密码邮件
     *
     * @param request 包含邮箱地址
     * @return 操作结果
     */
    @PostMapping("/forgot")
    public Result forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        
        if (email == null || email.trim().isEmpty()) {
            return Result.error(400, "邮箱不能为空");
        }
        
        // 验证邮箱格式
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return Result.error(400, "邮箱格式不正确");
        }
        
        try {
            passwordResetService.sendResetEmail(email);
            return Result.success("重置邮件已发送，请查收");
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
    
    /**
     * 重置密码
     *
     * @param request 包含token和新密码
     * @return 操作结果
     */
    @PostMapping("/reset")
    public Result resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        
        if (token == null || token.trim().isEmpty()) {
            return Result.error(400, "重置链接无效");
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }
        
        if (!newPassword.equals(confirmPassword)) {
            return Result.error(400, "两次输入的密码不一致");
        }
        
        try {
            passwordResetService.resetPassword(token, newPassword);
            return Result.success("密码重置成功，请使用新密码登录");
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}
