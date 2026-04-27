package com.xingyou;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "123456";
        
        System.out.println("=== 测试 BCrypt 加密 ===");
        System.out.println("原始密码: " + password);
        
        String encoded1 = encoder.encode(password);
        System.out.println("加密值1: " + encoded1);
        System.out.println("验证1: " + encoder.matches(password, encoded1));
        
        String encoded2 = encoder.encode(password);
        System.out.println("加密值2: " + encoded2);
        System.out.println("验证2: " + encoder.matches(password, encoded2));
        
        String dbPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        System.out.println("\n=== 测试数据库密码 ===");
        System.out.println("数据库密码: " + dbPassword);
        System.out.println("验证结果: " + encoder.matches(password, dbPassword));
        
        if (!encoder.matches(password, dbPassword)) {
            System.out.println("⚠️ 数据库密码不匹配！");
            System.out.println("请使用下面的新密码更新数据库：");
            System.out.println(encoded1);
        } else {
            System.out.println("✓ 数据库密码正确");
        }
    }
}
