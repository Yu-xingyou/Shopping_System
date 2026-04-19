package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.AdminService;
import com.xingyou.service.UserService;
import com.xingyou.util.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    
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
            @RequestParam(required = false) String staffId,
            @RequestParam(required = false) String name) {
        
        // 按员工ID查询
        if (staffId != null && !staffId.trim().isEmpty()) {
            User staff = adminService.findStaffByStaffId(staffId);
            if (staff == null) {
                return Result.error(404, "员工不存在");
            }
            return Result.success(staff);
        } 
        // 按员工姓名查询
        else if (name != null && !name.trim().isEmpty()) {
            List<User> staffs = adminService.findStaffByName(name);
            return Result.success(staffs);
        } 
        // 查询所有员工
        else {
            List<User> staffs = adminService.findAllStaffs();
            return Result.success(staffs);
        }
    }
    
    /**
     * 添加新员工
     * 
     * @param staff 员工用户对象
     * @return Result 返回添加成功的员工信息，包含员工ID和姓名
     */
    @PostMapping("/staffs")
    public Result addStaff(@RequestBody User staff) {
        try {
            adminService.addStaff(staff);
            
            Map<String, Object> result = new HashMap<>();
            result.put("userId", staff.getUserId());
            result.put("name", staff.getName());
            
            return Result.success("添加员工成功", result);
        } 
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
        catch (Exception e) {
            return Result.error(500, "添加员工失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新员工信息
     * 
     * @param staffId 路径参数，要更新的员工ID
     * @param staff 请求体中的员工用户对象
     * @return Result 返回更新结果，包含被更新的员工ID
     */
    @PutMapping("/staffs/{staffId}")
    public Result updateStaff(
            @PathVariable String staffId,
            @RequestBody User staff) {
        try {
            staff.setUserId(staffId);
            adminService.updateStaff(staff);
            
            Map<String, Object> result = new HashMap<>();
            result.put("userId", staffId);
            
            return Result.success("更新员工信息成功", result);
        } 
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
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
            Integer stock = request.get("stock");
            adminService.updateProductStock(id, stock);
            
            Map<String, Object> result = new HashMap<>();
            result.put("productId", id);
            result.put("stock", stock);
            
            return Result.success("更新库存成功", result);
        } 
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
        catch (Exception e) {
            return Result.error(500, "更新库存失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新管理员个人信息
     * 
     * @param adminId 路径参数，要更新信息的管理员ID
     * @param admin 请求体中的管理员用户对象
     * @return Result 返回更新结果
     */
    @PutMapping("/{adminId}")
    public Result update(@PathVariable String adminId, @RequestBody User admin) {
        if (adminId == null || adminId.trim().isEmpty()) {
            return Result.error(400, "管理员 ID 不能为空");
        }
        
        try {
            adminService.update(adminId, admin);
            return Result.success("个人信息更新成功");
        } 
        catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } 
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
    
    /**
     * 上传管理员头像
     * 接收前端上传的图片文件，通过阿里云OSS存储后返回图片URL
     * 
     * @param file 上传的图片文件（支持jpg、png、jpeg格式，最大5MB）
     * @param adminId 管理员ID
     * @return Result 返回上传结果，成功时包含图片URL
     */
    @PostMapping("/avatar/upload")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("adminId") String adminId) {
        if (file == null || file.isEmpty()) {
            return Result.error(400, "请选择要上传的图片");
        }
        
        if (adminId == null || adminId.trim().isEmpty()) {
            return Result.error(400, "管理员ID不能为空");
        }
        
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif|webp)$")) {
            return Result.error(400, "仅支持 jpg、jpeg、png、gif、webp 格式的图片");
        }
        
        // 验证文件大小（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error(400, "图片大小不能超过 5MB");
        }
        
        try {
            byte[] bytes = file.getBytes();
            String imageUrl = aliyunOSSOperator.upload(bytes, originalFilename);
            
            // 更新数据库中的头像URL
            User updateRequest = new User();
            updateRequest.setAvatar(imageUrl);
            adminService.update(adminId, updateRequest);
            
            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", imageUrl);
            
            return Result.success("头像上传成功", result);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "头像上传失败：" + e.getMessage());
        }
    }
    
    /**
     * 拉黑用户
     * 
     * @param userId 路径参数，要拉黑的用户ID
     * @return Result 返回操作结果
     */
    @PostMapping("/users/{userId}/blacklist")
    public Result blacklistUser(@PathVariable String userId) {
        log.info("管理员发起拉黑用户请求 - userId: {}", userId);
        try {
            userService.blacklistUser(userId);
            log.info("管理员拉黑用户成功 - userId: {}", userId);
            return Result.success("拉黑用户成功");
        } catch (BusinessException e) {
            log.warn("管理员拉黑用户失败 - userId: {}, 原因: {}", userId, e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("管理员拉黑用户异常 - userId: {}, 异常信息: {}", userId, e.getMessage(), e);
            return Result.error(500, "拉黑用户失败：" + e.getMessage());
        }
    }
    
    /**
     * 解除拉黑用户
     * 
     * @param userId 路径参数，要解除拉黑的用户ID
     * @return Result 返回操作结果
     */
    @PostMapping("/users/{userId}/unblacklist")
    public Result unblacklistUser(@PathVariable String userId) {
        log.info("管理员发起解除拉黑用户请求 - userId: {}", userId);
        try {
            userService.unblacklistUser(userId);
            log.info("管理员解除拉黑用户成功 - userId: {}", userId);
            return Result.success("解除拉黑成功");
        } catch (BusinessException e) {
            log.warn("管理员解除拉黑用户失败 - userId: {}, 原因: {}", userId, e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("管理员解除拉黑用户异常 - userId: {}, 异常信息: {}", userId, e.getMessage(), e);
            return Result.error(500, "解除拉黑失败：" + e.getMessage());
        }
    }
}
