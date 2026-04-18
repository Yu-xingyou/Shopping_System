package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.AdminService;
import com.xingyou.service.OrderService;
import com.xingyou.service.StaffService;
import com.xingyou.service.UserService;
import com.xingyou.util.AliyunOSSOperator;
import com.xingyou.util.CaptchaStorage;
import com.xingyou.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    
    /**
     * 获取图形验证码
     * 生成4位随机字符验证码，存储在内存中（5分钟有效期）
     * 
     * @return Result 返回Base64编码的验证码图片和token
     */
    @GetMapping("/captcha")
    public Result getCaptcha() {
        try {
            CaptchaUtil.CaptchaResult captchaResult = CaptchaUtil.generateCaptcha();
            
            String token = CaptchaStorage.storeCaptcha(captchaResult.getCode());
            
            Map<String, Object> result = new HashMap<>();
            result.put("image", "data:image/jpeg;base64," + captchaResult.getImageBase64());
            result.put("token", token);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "验证码生成失败：" + e.getMessage());
        }
    }
    
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
     * @param loginRequest 登录请求，包含 userId、password、captchaToken、captcha
     * @return Result 返回登录结果，包含用户信息和JWT令牌
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, Object> loginRequest) {
        String userId = (String) loginRequest.get("userId");
        String password = (String) loginRequest.get("password");
        String captchaToken = (String) loginRequest.get("captchaToken");
        String captcha = (String) loginRequest.get("captcha");
        
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("账号不能为空");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        if (captchaToken == null || captcha == null) {
            return Result.error(400, "请输入验证码");
        }
        
        if (!CaptchaStorage.verifyCaptcha(captchaToken, captcha)) {
            return Result.error(400, "验证码错误或已过期");
        }
        
        try {
            Map<String, Object> loginResult;
            
            if (userId.startsWith("U")) {
                loginResult = userService.login(userId, password);
            } else {
                try {
                    loginResult = adminService.login(userId, password);
                } catch (BusinessException e) {
                    if (e.getCode() == 401 || e.getCode() == 404) {
                        loginResult = staffService.login(userId, password);
                    } else {
                        throw e;
                    }
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
     * @param captchaToken 验证码token
     * @param captcha 验证码文本
     * @return Result 返回注册结果，成功时包含生成的用户ID
     * @throws IllegalArgumentException 当密码为空或密码长度少于6位时抛出异常
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user, 
                          @RequestParam String captchaToken,
                          @RequestParam String captcha) {
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("密码长度不能少于 6 位");
        }
        
        if (captchaToken == null || captcha == null) {
            return Result.error(400, "请输入验证码");
        }
        
        if (!CaptchaStorage.verifyCaptcha(captchaToken, captcha)) {
            return Result.error(400, "验证码错误或已过期");
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
    
    /**
     * 上传头像接口
     * 接收前端上传的图片文件，通过阿里云OSS存储后返回图片URL
     * 
     * @param file 上传的图片文件（支持jpg、png、jpeg格式，最大5MB）
     * @param userId 用户ID
     * @return Result 返回上传结果，成功时包含图片URL
     */
    @PostMapping("/avatar/upload")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) {
        if (file == null || file.isEmpty()) {
            return Result.error(400, "请选择要上传的图片");
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
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
            userService.update(userId, updateRequest);
            
            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", imageUrl);
            
            return Result.success("头像上传成功", result);
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "头像上传失败：" + e.getMessage());
        }
    }




}

