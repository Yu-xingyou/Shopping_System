package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Resource
    private AdminService adminService;
    
    @GetMapping("/users")
    public Result getUsers(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name) {
        
        if (userId != null && !userId.trim().isEmpty()) {
            User user = adminService.findUserByUserId(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            return Result.success(user);
        } else if (name != null && !name.trim().isEmpty()) {
            List<User> users = adminService.findUserByName(name);
            return Result.success(users);
        } else {
            List<User> users = adminService.findAllUsers();
            return Result.success(users);
        }
    }
    
    @GetMapping("/staffs")
    public Result getStaffs(
            @RequestParam(required = false) Integer staffId,
            @RequestParam(required = false) String name) {
        
        if (staffId != null) {
            Staff staff = adminService.findStaffByStaffId(staffId);
            if (staff == null) {
                return Result.error(404, "员工不存在");
            }
            return Result.success(staff);
        } else if (name != null && !name.trim().isEmpty()) {
            List<Staff> staffs = adminService.findStaffByName(name);
            return Result.success(staffs);
        } else {
            List<Staff> staffs = adminService.findAllStaffs();
            return Result.success(staffs);
        }
    }
    
    @PostMapping("/staffs")
    public Result addStaff(@RequestBody Staff staff) {
        try {
            adminService.addStaff(staff);
            
            Map<String, Object> result = new HashMap<>();
            result.put("staffId", staff.getStaffId());
            result.put("name", staff.getName());
            
            return Result.success("添加员工成功", result);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "添加员工失败：" + e.getMessage());
        }
    }
    
    @PutMapping("/staffs/{staffId}")
    public Result updateStaff(
            @PathVariable Integer staffId,
            @RequestBody Staff staff) {
        try {
            staff.setStaffId(staffId);
            adminService.updateStaff(staff);
            
            Map<String, Object> result = new HashMap<>();
            result.put("staffId", staffId);
            
            return Result.success("更新员工信息成功", result);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "更新员工信息失败：" + e.getMessage());
        }
    }
    
    @PutMapping("/products/{id}/stock")
    public Result updateProductStock(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer stock = request.get("stock");
            adminService.updateProductStock(id, stock);
            
            Map<String, Object> result = new HashMap<>();
            result.put("productId", id);
            result.put("stock", stock);
            
            return Result.success("更新库存成功", result);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "更新库存失败：" + e.getMessage());
        }
    }
}
