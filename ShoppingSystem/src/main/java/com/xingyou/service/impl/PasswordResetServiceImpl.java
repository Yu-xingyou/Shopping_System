package com.xingyou.service.impl;

import com.xingyou.entity.people.PasswordResetToken;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.PasswordResetTokenMapper;
import com.xingyou.mapper.UserMapper;
import com.xingyou.service.PasswordResetService;
import com.xingyou.util.PasswordEncoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 密码重置服务实现类
 */
@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordResetTokenMapper tokenMapper;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;
    
    /**
     * 前端重置密码页面地址
     */
    @Value("${reset.password.url:http://localhost:5173/reset-password}")
    private String resetPasswordUrl;
    
    /**
     * 邮件发件人
     */
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendResetEmail(String email) {
        log.info("========== 开始发送重置密码邮件 ==========");
        log.info("发送重置密码邮件请求 - email: {}", email);
        
        // 1. 验证邮箱是否存在
        User user = userMapper.findByEmail(email);
        if (user == null) {
            log.warn("邮箱不存在 - email: {}", email);
            // 安全起见，即使邮箱不存在也返回成功（防止恶意探测）
            log.info("========== 重置密码邮件处理完成（邮箱不存在，返回成功） ==========");
            return true;
        }
        
        log.debug("找到用户 - userId: {}, name: {}, email: {}", user.getUserId(), user.getName(), email);
        
        // 2. 删除旧的 Token（如果存在）
        PasswordResetToken oldToken = tokenMapper.findByUserId(user.getUserId());
        if (oldToken != null) {
            log.info("删除旧的重置Token - userId: {}, oldToken前8位: {}", 
                    user.getUserId(), oldToken.getToken().substring(0, 8));
            tokenMapper.deleteByToken(oldToken.getToken());
        }
        
        // 3. 生成新 Token
        String token = UUID.randomUUID().toString().replace("-", "");
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getUserId());
        resetToken.setToken(token);
        resetToken.setExpireTime(LocalDateTime.now().plusMinutes(30)); // 30分钟有效期
        resetToken.setUsed(0);
        resetToken.setCreateTime(LocalDateTime.now());
        
        log.debug("生成新Token - userId: {}, token前8位: {}, 过期时间: {}", 
                user.getUserId(), token.substring(0, 8), resetToken.getExpireTime());
        
        // 4. 保存到数据库
        tokenMapper.insert(resetToken);
        log.debug("Token保存成功 - userId: {}", user.getUserId());
        
        // 5. 发送邮件
        try {
            String resetLink = resetPasswordUrl + "?token=" + token;
            log.info("开始发送邮件 - email: {}, userName: {}", email, user.getName());
            sendEmail(email, user.getName(), resetLink);
            log.info("✓ 重置密码邮件发送成功 - email: {}", email);
            log.info("========== 重置密码邮件处理完成 ==========");
            return true;
        } catch (Exception e) {
            log.error("✗ 发送邮件失败 - email: {}, 异常信息: {}", email, e.getMessage(), e);
            // 删除已生成的 Token
            tokenMapper.deleteByToken(token);
            log.warn("已删除失效Token - token前8位: {}", token.substring(0, 8));
            throw new BusinessException(500, "邮件发送失败，请稍后重试");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(String token, String newPassword) {
        log.info("========== 开始重置密码 ==========");
        log.info("重置密码请求 - token前8位: {}", token.length() >= 8 ? token.substring(0, 8) : token);
        
        // 1. 验证 Token
        PasswordResetToken resetToken = tokenMapper.findByToken(token);
        if (resetToken == null) {
            log.warn("重置密码失败 - Token不存在: token前8位: {}", token.length() >= 8 ? token.substring(0, 8) : token);
            throw new BusinessException(400, "重置链接无效或已过期");
        }
        
        log.debug("找到Token - userId: {}, used: {}, expireTime: {}", 
                resetToken.getUserId(), resetToken.getUsed(), resetToken.getExpireTime());
        
        // 2. 检查是否已使用
        if (resetToken.getUsed() == 1) {
            log.warn("重置密码失败 - Token已被使用: userId: {}", resetToken.getUserId());
            throw new BusinessException(400, "重置链接已使用，请重新申请");
        }
        
        // 3. 检查是否过期
        if (LocalDateTime.now().isAfter(resetToken.getExpireTime())) {
            log.warn("重置密码失败 - Token已过期: userId: {}, expireTime: {}", 
                    resetToken.getUserId(), resetToken.getExpireTime());
            tokenMapper.deleteByToken(token);
            throw new BusinessException(400, "重置链接已过期，请重新申请");
        }
        
        // 4. 验证密码长度
        if (newPassword == null || newPassword.length() < 6) {
            log.warn("重置密码失败 - 密码长度不足: 长度: {}", newPassword != null ? newPassword.length() : 0);
            throw new BusinessException(400, "密码长度不能少于6位");
        }
        
        // 5. 更新密码
        User user = userMapper.findByUserId(resetToken.getUserId());
        if (user == null) {
            log.error("重置密码失败 - 用户不存在: userId: {}", resetToken.getUserId());
            throw new BusinessException(404, "用户不存在");
        }
        
        log.info("准备更新密码 - userId: {}, name: {}", user.getUserId(), user.getName());
        user.setPassword(passwordEncoderUtil.encode(newPassword));
        int rows = userMapper.update(user);
        if (rows != 1) {
            log.error("重置密码失败 - 密码更新失败: userId: {}, 影响行数: {}", user.getUserId(), rows);
            throw new BusinessException(500, "密码更新失败");
        }
        
        // 6. 标记 Token 为已使用
        tokenMapper.markAsUsed(token);
        log.debug("Token标记为已使用 - userId: {}", user.getUserId());
        
        log.info("✓ 密码重置成功 - userId: {}, name: {}", user.getUserId(), user.getName());
        log.info("========== 重置密码完成 ==========");
        return true;
    }
    
    /**
     * 发送邮件
     */
    private void sendEmail(String toEmail, String userName, String resetLink) {
        log.debug("构建邮件内容 - toEmail: {}, userName: {}", toEmail, userName);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("【购物系统】密码重置通知");
        
        String content = String.format(
            "尊敬的 %s，您好！\n\n" +
            "您正在申请重置购物系统密码。\n" +
            "请点击以下链接重置密码（30分钟内有效）：\n\n" +
            "%s\n\n" +
            "如果这不是您本人的操作，请忽略此邮件。\n\n" +
            "此邮件为系统自动发送，请勿回复。\n\n" +
            "购物系统团队",
            userName,
            resetLink
        );
        
        message.setText(content);
        
        log.debug("开始调用邮件服务器发送邮件 - from: {}, to: {}", fromEmail, toEmail);
        mailSender.send(message);
        log.debug("邮件发送调用完成");
    }
}
