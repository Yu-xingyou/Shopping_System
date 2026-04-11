package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.Admin;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
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
    
    /**
     * 管理员登录接口
     * 
     * @param admin 管理员登录信息，包含管理员ID和密码
     * @return Result 返回登录成功的管理员信息
     * @throws IllegalArgumentException 当管理员ID为空或密码为空时抛出异常
     */
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        // 验证管理员ID不能为空
        if (admin.getAdminId() == null) {
            throw new IllegalArgumentException("管理员 ID 不能为空");
        }
        
        // 验证密码不能为空
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        // 调用服务层进行登录验证
        Admin loginAdmin = adminService.login(admin.getAdminId(), admin.getPassword());
        
        return Result.success(loginAdmin);
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
     * 查询用户信息（支持按用户ID、姓名或查询所有用户）
     * 
     * @param userId 用户ID，可选参数
     * @param name 用户姓名，可选参数
     * @return Result 返回查询到的用户信息或用户列表
     */
    @GetMapping("/users")
    public Result getUsers(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String name) {
        
        // 按用户ID查询
        if (userId != null && !userId.trim().isEmpty()) {
            User user = adminService.findUserByUserId(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            return Result.success(user);
        } 
        // 按用户姓名查询
        else if (name != null && !name.trim().isEmpty()) {
            List<User> users = adminService.findUserByName(name);
            return Result.success(users);
        } 
        // 查询所有用户
        else {
            List<User> users = adminService.findAllUsers();
            return Result.success(users);
        }
    }
    
    /**
     * 查询员工信息（支持按员工ID、姓名或查询所有员工）
     * 
     * @param staffId 员工ID，可选参数
     * @param name 员工姓名，可选参数
     * @return Result 返回查询到的员工信息或员工列表
     */
    @GetMapping("/staffs")
    public Result getStaffs(
            @RequestParam(required = false) Integer staffId,
            @RequestParam(required = false) String name) {
        
        // 按员工ID查询
        if (staffId != null) {
            Staff staff = adminService.findStaffByStaffId(staffId);
            if (staff == null) {
                return Result.error(404, "员工不存在");
            }
            return Result.success(staff);
        } 
        // 按员工姓名查询
        else if (name != null && !name.trim().isEmpty()) {
            List<Staff> staffs = adminService.findStaffByName(name);
            return Result.success(staffs);
        } 
        // 查询所有员工
        else {
            List<Staff> staffs = adminService.findAllStaffs();
            return Result.success(staffs);
        }
    }
    
    /**
     * 添加新员工
     * 
     * @param staff 员工信息对象
     * @return Result 返回添加成功的员工信息，包含员工ID和姓名
     */
    @PostMapping("/staffs")
    public Result addStaff(@RequestBody Staff staff) {
        try {
            adminService.addStaff(staff);
            
            // 构建返回结果，包含员工ID和姓名
            Map<String, Object> result = new HashMap<>();
            result.put("staffId", staff.getStaffId());
            result.put("name", staff.getName());
            
            return Result.success("添加员工成功", result);
        } 
        // 捕获业务异常并返回对应错误信息
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
        // 捕获其他异常并返回通用错误信息
        catch (Exception e) {
            return Result.error(500, "添加员工失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新员工信息
     * 
     * @param staffId 路径参数，要更新的员工ID
     * @param staff 请求体中的员工信息对象
     * @return Result 返回更新结果，包含被更新的员工ID
     */
    @PutMapping("/staffs/{staffId}")
    public Result updateStaff(
            @PathVariable Integer staffId,
            @RequestBody Staff staff) {
        try {
            // 将路径中的员工ID设置到员工对象中
            staff.setStaffId(staffId);
            adminService.updateStaff(staff);
            
            // 构建返回结果，包含员工ID
            Map<String, Object> result = new HashMap<>();
            result.put("staffId", staffId);
            
            return Result.success("更新员工信息成功", result);
        } 
        // 捕获业务异常并返回对应错误信息
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
        // 捕获其他异常并返回通用错误信息
        catch (Exception e) {
            return Result.error(500, "更新员工信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新商品库存
     * 
     * @param id 路径参数，要更新库存的商品ID
     * @param request 请求体，包含库存数量的Map对象
     * @return Result 返回更新结果，包含商品ID和新的库存数量
     */
    @PutMapping("/products/{id}/stock")
    public Result updateProductStock(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {
        try {
            // 从请求体中获取库存数量
            Integer stock = request.get("stock");
            adminService.updateProductStock(id, stock);
            
            // 构建返回结果，包含商品ID和库存数量
            Map<String, Object> result = new HashMap<>();
            result.put("productId", id);
            result.put("stock", stock);
            
            return Result.success("更新库存成功", result);
        } 
        // 捕获业务异常并返回对应错误信息
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
        // 捕获其他异常并返回通用错误信息
        catch (Exception e) {
            return Result.error(500, "更新库存失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新管理员个人信息
     * 
     * @param adminId 路径参数，要更新信息的管理员ID
     * @param admin 请求体中的管理员信息对象
     * @return Result 返回更新结果
     */
    @PutMapping("/{adminId}")
    public Result update(@PathVariable Integer adminId, @RequestBody Admin admin) {
        // 验证管理员ID不能为空
        if (adminId == null) {
            return Result.error(400, "管理员 ID 不能为空");
        }
        
        try {
            // 将路径中的管理员ID设置到管理员对象中
            admin.setAdminId(adminId);
            adminService.update(adminId, admin);
            return Result.success("个人信息更新成功");
        } 
        // 捕获业务异常并返回对应错误信息
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
        // 捕获其他异常并返回通用错误信息
        catch (Exception e) {
            return Result.error(500, "更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询管理员总收益（按时间范围筛选）
     * 
     * @param days 查询参数，时间范围（天数）：
     *             - 1: 1天内
     *             - 7: 7天内
     *             - 30: 一个月内
     *             - null或不传: 全部时间
     * @return Result 返回总收益金额
     */
    @GetMapping("/revenue")
    public Result getRevenue(@RequestParam(required = false) Integer days) {
        try {
            Double revenue = adminService.getTotalRevenue(days);
            
            Map<String, Object> result = new HashMap<>();
            result.put("revenue", revenue);
            result.put("days", days);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "查询收益失败：" + e.getMessage());
        }
    }

}
