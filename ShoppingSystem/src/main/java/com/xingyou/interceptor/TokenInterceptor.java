package com.xingyou.interceptor;

import com.xingyou.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT令牌验证拦截器
 * 用于拦截请求并验证JWT令牌的有效性
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     * 
     * @param request 当前HTTP请求
     * @param response 当前HTTP响应
     * @param handler 被调用的处理器
     * @return true表示继续流程（如调用下一个拦截器或处理器），false表示流程中断
     * @throws Exception 处理异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的Authorization字段
        String authorization = request.getHeader("Authorization");
        
        // 检查Authorization头是否存在
        if (authorization == null || authorization.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\",\"data\":null}");
            return false;
        }
        
        // 检查是否以Bearer开头
        String token = authorization;
        if (authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        
        try {
            // 解析JWT令牌
            Claims claims = JwtUtils.parseJWT(token);
            
            // 将解析出的用户信息存储到request属性中，供后续使用
            request.setAttribute("claims", claims);
            
            // 可以继续传递用户ID等信息
            String userId = claims.get("userId", String.class);
            if (userId != null) {
                request.setAttribute("userId", userId);
            }
            
            return true;
        } catch (Exception e) {
            // JWT解析失败（令牌过期、签名错误等）
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"令牌无效或已过期，请重新登录\",\"data\":null}");
            return false;
        }
    }
}
