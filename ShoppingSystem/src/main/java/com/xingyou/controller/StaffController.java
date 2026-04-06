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
    
    @PostMapping("/login")
    public Result login(@RequestBody Staff staff) {
        if (staff.getStaffId() == null) {
            throw new IllegalArgumentException("员工 ID 不能为空");
        }
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        Staff loginStaff = staffService.login(staff.getStaffId(), staff.getPassword());
        
        return Result.success(loginStaff);
    }
    
    @GetMapping("/users")
    public Result getUsers(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name) {
        
        if (userId != null && !userId.trim().isEmpty()) {
            User user = staffService.findUserByUserId(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            return Result.success(user);
        } else if (name != null && !name.trim().isEmpty()) {
            List<User> users = staffService.findUserByName(name);
            return Result.success(users);
        } else {
            List<User> users = staffService.findAllUsers();
            return Result.success(users);
        }
    }
    
    @PutMapping("/{staffId}")
    public Result update(@PathVariable Integer staffId, @RequestBody Staff staff) {
        if (staffId == null) {
            return Result.error(400, "员工 ID 不能为空");
        }
        
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
