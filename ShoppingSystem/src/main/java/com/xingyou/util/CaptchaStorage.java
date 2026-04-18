package com.xingyou.util;



import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 验证码存储管理工具类
 * 使用内存存储验证码，支持自动过期清理
 * 适合前后端分离架构，无需 Session
 */
@Slf4j
public class CaptchaStorage {
    
    /**
     * 验证码存储（token -> 验证码信息）
     * 使用 ConcurrentHashMap 保证线程安全
     */
    private static final Map<String, CaptchaInfo> CAPTCHA_MAP = new ConcurrentHashMap<>();
    
    /**
     * 验证码有效期（分钟）
     */
    private static final int EXPIRE_MINUTES = 5;
    
    /**
     * 定时清理过期验证码
     */
    private static final ScheduledExecutorService CLEANUP_SCHEDULER = Executors.newScheduledThreadPool(1);
    
    /**
     * 静态初始化块：应用启动时开始定时清理
     */
    static {
        // 每2分钟清理一次过期验证码
        CLEANUP_SCHEDULER.scheduleAtFixedRate(
            CaptchaStorage::cleanupExpired,
            2, 2, TimeUnit.MINUTES
        );
        log.info("验证码定时清理任务已启动，清理间隔：2分钟");
    }
    
    /**
     * 生成并存储验证码
     * 
     * @param code 验证码文本
     * @return String 返回唯一的token（用于前端传递）
     */
    public static String storeCaptcha(String code) {
        String token = UUID.randomUUID().toString().replace("-", "");
        
        CaptchaInfo captchaInfo = new CaptchaInfo();
        captchaInfo.setCode(code);
        captchaInfo.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        
        CAPTCHA_MAP.put(token, captchaInfo);
        
        log.debug("存储验证码成功 - token: {}, code: {}, expire: {}", 
            token, code, captchaInfo.getExpireTime());
        
        return token;
    }
    
    /**
     * 验证验证码
     * 
     * @param token 验证码token（前端传入）
     * @param inputCode 用户输入的验证码
     * @return boolean 验证是否成功
     */
    public static boolean verifyCaptcha(String token, String inputCode) {
        if (token == null || inputCode == null) {
            return false;
        }
        
        CaptchaInfo captchaInfo = CAPTCHA_MAP.get(token);
        
        if (captchaInfo == null) {
            log.warn("验证码不存在或已使用 - token: {}", token);
            return false;
        }
        
        // 检查是否过期
        if (LocalDateTime.now().isAfter(captchaInfo.getExpireTime())) {
            CAPTCHA_MAP.remove(token);
            log.warn("验证码已过期 - token: {}", token);
            return false;
        }
        
        // 验证验证码（忽略大小写）
        boolean isValid = captchaInfo.getCode().equalsIgnoreCase(inputCode.trim());
        
        if (isValid) {
            // 验证成功后删除（防止重复使用）
            CAPTCHA_MAP.remove(token);
            log.info("验证码验证成功 - token: {}", token);
        } else {
            log.warn("验证码错误 - token: {}, input: {}", token, inputCode);
        }
        
        return isValid;
    }
    
    /**
     * 清理过期的验证码
     */
    private static void cleanupExpired() {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        
        for (Map.Entry<String, CaptchaInfo> entry : CAPTCHA_MAP.entrySet()) {
            if (now.isAfter(entry.getValue().getExpireTime())) {
                CAPTCHA_MAP.remove(entry.getKey());
                count++;
            }
        }
        
        if (count > 0) {
            log.debug("清理过期验证码 - 数量: {}", count);
        }
    }
    
    /**
     * 获取当前存储的验证码数量（用于监控）
     */
    public static int getCaptchaCount() {
        return CAPTCHA_MAP.size();
    }
    
    /**
     * 验证码信息内部类
     */
    private static class CaptchaInfo {
        /**
         * 验证码文本
         */
        private String code;
        
        /**
         * 过期时间
         */
        private LocalDateTime expireTime;
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public LocalDateTime getExpireTime() {
            return expireTime;
        }
        
        public void setExpireTime(LocalDateTime expireTime) {
            this.expireTime = expireTime;
        }
    }
}
