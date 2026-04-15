package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.AdminService;
import com.xingyou.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
public class StaffController {
    
    @Autowired
    private StaffService staffService;
    
    @Autowired
    private AdminService adminService;
    
    /**
     * 查询用户信息接口
     * 支持按用户ID、用户名查询或查询所有用户
     * 
     * @param userId 用户ID，可选参数
     * @param name 用户名称，可选参数
     * @return Result 返回查询结果，可能是单个用户对象或用户列表
     */
    @GetMapping("/users")
    public Result getUsers(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name) {
        
        // 按用户ID查询单个用户
        if (userId != null && !userId.trim().isEmpty()) {
            User user = staffService.findUserByUserId(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            return Result.success(user);
        } 
        // 按用户名模糊查询用户列表
        else if (name != null && !name.trim().isEmpty()) {
            List<User> users = staffService.findUserByName(name);
            return Result.success(users);
        } 
        // 查询所有用户
        else {
            List<User> users = staffService.findAllUsers();
            return Result.success(users);
        }
    }
    
    /**
     * 查询所有订单列表
     * 
     * @return Result 返回包含所有订单信息的列表
     */
    @GetMapping("/orders")
    public Result getOrders() {
        List<Order> orders = adminService.findAllOrders();
        return Result.success(orders);
    }
    
    /**
     * 更新员工个人信息接口
     * 
     * @param staffId 员工ID，从路径变量中获取
     * @param staff 员工用户对象，包含需要更新的字段
     * @return Result 返回更新结果，成功或失败信息
     */
    @PutMapping("/{staffId}")
    public Result update(@PathVariable Integer staffId, @RequestBody User staff) {
        // 验证员工ID不能为空
        if (staffId == null) {
            return Result.error(400, "员工 ID 不能为空");
        }
        
        // 执行更新操作并处理异常
        try {
            staff.setUserId(staffId.toString());
            staffService.update(staffId, staff);
            return Result.success("个人信息更新成功");
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "更新失败：" + e.getMessage());
        }
    }

}
