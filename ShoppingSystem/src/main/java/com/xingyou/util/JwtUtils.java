package com.xingyou.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和解析JWT令牌
 */
@Component
public class JwtUtils {

    /**
     * JWT签名密钥（从配置文件读取）
     */
    private static String signKey;

    /**
     * JWT过期时间（毫秒）（从配置文件读取）
     */
    private static Long expire;

    /**
     * 注入签名密钥
     * @param key 签名密钥
     */
    @Value("${jwt.signKey:eGluZ3lvdQ==}")
    public void setSignKey(String key) {
        JwtUtils.signKey = key;
    }

    /**
     * 注入过期时间
     * @param exp 过期时间（毫秒）
     */
    @Value("${jwt.expire:43200000}")
    public void setExpire(Long exp) {
        JwtUtils.expire = exp;
    }

    /**
     * 生成JWT令牌
     * 
     * @param claims JWT负载数据（包含userId、role等信息）
     * @return 生成的JWT令牌字符串
     */
    public static String generateJwt(Map<String,Object> claims){
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * 
     * @param jwt JWT令牌字符串
     * @return JWT负载内容（Claims对象）
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}
