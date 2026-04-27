package com.xingyou.interceptor;

import com.xingyou.service.UserService;
import com.xingyou.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT令牌验证拦截器
 * 用于拦截请求并验证JWT令牌的有效性和权限
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

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
            
            // 将完整的claims存储到request属性中
            request.setAttribute("claims", claims);
            
            // 提取用户ID
            String userId = claims.get("userId", String.class);
            if (userId != null) {
                request.setAttribute("userId", userId);
            }
            
            // 提取用户角色
            Integer role = claims.get("role", Integer.class);
            if (role != null) {
                request.setAttribute("role", role);
            }
            
            // 提取用户名（可选）
            String name = claims.get("name", String.class);
            if (name != null) {
                request.setAttribute("userName", name);
            }
            
            // 检查用户是否被拉黑（仅对普通用户role=0进行检查）
            if (role != null && role == 0 && userId != null) {
                if (userService.isUserBlacklisted(userId)) {
                    // 被拉黑的用户只能访问商品相关接口
                    String requestURI = request.getRequestURI();
                    boolean canAccess = requestURI.startsWith("/product/") || 
                                       requestURI.equals("/product") ||
                                       requestURI.startsWith("/order/top-selling");
                    
                    if (!canAccess) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"code\":403,\"message\":\"您的账号已被拉黑，只能浏览商品\",\"data\":null}");
                        return false;
                    }
                }
            }
            
            // 权限验证：根据请求路径验证角色权限
            String requestURI = request.getRequestURI();
            
            // 管理员专属接口（只有role=2的管理员可以访问）
            if (requestURI.startsWith("/admin/") && role != 2) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"权限不足，仅管理员可访问\",\"data\":null}");
                return false;
            }
            
            // 员工专属接口（只有role=1的员工和role=2的管理员可以访问）
            if (requestURI.startsWith("/staff/") && role != 1 && role != 2) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"权限不足，仅员工或管理员可访问\",\"data\":null}");
                return false;
            }
            
            // 普通用户个人信息接口（只能查看/修改自己的信息）
            if (requestURI.matches("/user/U\\d+") && request.getMethod().equals("GET")) {
                String pathUserId = requestURI.substring(requestURI.lastIndexOf("/") + 1);
                if (!pathUserId.equals(userId) && !isAdmin(role) && !isStaffOrAdmin(role)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":403,\"message\":\"无权查看其他用户信息\",\"data\":null}");
                    return false;
                }
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
    
    /**
     * 判断是否为管理员
     * @param role 用户角色
     * @return true-是管理员，false-不是
     */
    private boolean isAdmin(Integer role) {
        return role != null && role == 2;
    }
    
    /**
     * 判断是否为员工或管理员
     * @param role 用户角色
     * @return true-是员工或管理员，false-不是
     */
    private boolean isStaffOrAdmin(Integer role) {
        return role != null && (role == 1 || role == 2);
    }
}
