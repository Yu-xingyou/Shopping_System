package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.OrderService;
import com.xingyou.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {
    
    @Resource
    private UserService userService;
    
    @Resource
    private OrderService orderService;
    
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("账号不能为空");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        User loginUser = userService.login(user.getUserId(), user.getPassword());
        return Result.success(loginUser);
    }
    
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

