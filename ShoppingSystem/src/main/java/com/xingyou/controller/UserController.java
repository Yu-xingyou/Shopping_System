package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.AdminService;
import com.xingyou.service.OrderService;
import com.xingyou.service.StaffService;
import com.xingyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private StaffService staffService;
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 统一登录接口（支持所有角色）
     * 
     * 根据 userId 格式自动识别角色：
     * - 以U开头：普通用户登录（role=0）
     * - 纯数字：先尝试管理员登录，失败则尝试员工登录
     *   - 管理员登录成功：role=2
     *   - 员工登录成功：role=1
     * 
     * 注意：员工和管理员的账号ID必须保证不重复，否则管理员ID会被优先识别
     * 
     * @param loginRequest 登录请求，包含 userId 和 password
     * @return Result 返回登录结果，包含用户信息和JWT令牌
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, Object> loginRequest) {
        String userId = (String) loginRequest.get("userId");
        String password = (String) loginRequest.get("password");
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("账号不能为空");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        try {
            Map<String, Object> loginResult;
            
            // 判断用户类型
            if (userId.startsWith("U")) {
                // 普通用户登录
                loginResult = userService.login(userId, password);
            } else {
                // 尝试解析为整数ID（员工或管理员）
                try {
                    Integer id = Integer.parseInt(userId);
                    // 先尝试管理员登录
                    try {
                        loginResult = adminService.login(id, password);
                    } catch (BusinessException e) {
                        // 管理员登录失败，尝试员工登录
                        if (e.getCode() == 401 || e.getCode() == 404) {
                            loginResult = staffService.login(id, password);
                        } else {
                            throw e;
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new BusinessException(400, "账号格式不正确");
                }
            }
            
            return Result.success(loginResult);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "登录失败：" + e.getMessage());
        }
    }
    
    /**
     * 用户注册接口（仅普通用户）
     * 
     * @param user 用户信息对象，必须包含密码等注册所需信息
     * @return Result 返回注册结果，成功时包含生成的用户ID
     * @throws IllegalArgumentException 当密码为空或密码长度少于6位时抛出异常
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于 6 位");
        }
        
        String userId = userService.register(user);
        
        return Result.success("注册成功，您的账号为：" + userId, null);
    }
    
    /**
     * 查询用户订单接口
     * 
     * @param userId 用户ID，必填参数
     * @return Result 返回查询结果，成功时包含该用户的订单列表
     */
    @GetMapping("/orders")
    public Result getOrders(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户 ID 不能为空");
        }
        
        try {
            User user = userService.findByUserId(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            
            List<Order> orders = orderService.findByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(500, "查询订单失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新用户信息接口
     * 
     * @param userId 用户ID，从路径变量中获取
     * @param user 用户信息对象，包含需要更新的字段
     * @return Result 返回更新结果，成功或失败信息
     */
    @PutMapping("/{userId}")
    public Result update(@PathVariable String userId, @RequestBody User user) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户 ID 不能为空");
        }
        
        try {
            userService.update(userId, user);
            return Result.success("个人信息更新成功");
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "更新失败：" + e.getMessage());
        }
    }

}

