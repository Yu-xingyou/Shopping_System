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
    
    /**
     * 用户登录接口
     * 
     * @param user 用户信息对象，必须包含用户ID和密码
     * @return Result 返回登录结果，成功时包含登录后的用户信息
     * @throws IllegalArgumentException 当账号为空或密码为空时抛出异常
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        // 验证账号不能为空
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("账号不能为空");
        }
        
        // 验证密码不能为空
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 调用服务层进行登录验证
        User loginUser = userService.login(user.getUserId(), user.getPassword());
        return Result.success(loginUser);
    }
    
    /**
     * 用户注册接口
     * 
     * @param user 用户信息对象，必须包含密码等注册所需信息
     * @return Result 返回注册结果，成功时包含生成的用户ID
     * @throws IllegalArgumentException 当密码为空或密码长度少于6位时抛出异常
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 验证密码不能为空
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 验证密码长度不少于6位
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于 6 位");
        }
        
        // 调用服务层执行注册操作
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
        // 验证用户ID不能为空
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户 ID 不能为空");
        }
        
        // 查询用户信息并验证用户是否存在
        try {
            User user = userService.findByUserId(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            
            // 查询该用户的所有订单
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
        // 验证用户ID不能为空
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户 ID 不能为空");
        }
        
        // 执行更新操作并处理异常
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

