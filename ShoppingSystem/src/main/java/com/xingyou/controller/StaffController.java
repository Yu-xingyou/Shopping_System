package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.StaffService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    
    @Resource
    private StaffService staffService;
    
    /**
     * 员工登录接口
     * 
     * @param staff 员工信息对象，必须包含员工ID和密码
     * @return Result 返回登录结果，成功时包含登录后的员工信息
     * @throws IllegalArgumentException 当员工ID为空或密码为空时抛出异常
     */
    @PostMapping("/login")
    public Result login(@RequestBody Staff staff) {
        // 验证员工ID不能为空
        if (staff.getStaffId() == null) {
            throw new IllegalArgumentException("员工 ID 不能为空");
        }
        
        // 验证密码不能为空
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 调用服务层进行登录验证
        Staff loginStaff = staffService.login(staff.getStaffId(), staff.getPassword());
        
        return Result.success(loginStaff);
    }
    
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
     * 更新员工信息接口
     * 
     * @param staffId 员工ID，从路径变量中获取
     * @param staff 员工信息对象，包含需要更新的字段
     * @return Result 返回更新结果，成功或失败信息
     */
    @PutMapping("/{staffId}")
    public Result update(@PathVariable Integer staffId, @RequestBody Staff staff) {
        // 验证员工ID不能为空
        if (staffId == null) {
            return Result.error(400, "员工 ID 不能为空");
        }
        
        // 执行更新操作并处理异常
        try {
            staff.setStaffId(staffId);
            staffService.update(staffId, staff);
            return Result.success("个人信息更新成功");
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "更新失败：" + e.getMessage());
        }
    }

}
