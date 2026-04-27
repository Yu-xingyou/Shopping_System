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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            
            log.info("✓ 验证码生成成功 - token: {}, code长度: {}", token, captchaResult.getCode().length());
            log.debug("验证码详细信息 - token前8位: {}, 过期时间: 5分钟后", token.substring(0, 8));
            
            Map<String, Object> result = new HashMap<>();
            result.put("image", "data:image/jpeg;base64," + captchaResult.getImageBase64());
            result.put("token", token);
            
            log.info("========== 验证码生成完成 ==========");
            return Result.success(result);
        } catch (Exception e) {
            log.error("✗ 验证码生成失败 - 异常信息: {}", e.getMessage(), e);
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
        
        log.info("========== 开始处理登录请求 ==========");
        log.info("登录请求 - userId: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("✗ 登录失败 - 账号为空");
            throw new IllegalArgumentException("账号不能为空");
        }
        
        if (password == null || password.trim().isEmpty()) {
            log.warn("✗ 登录失败 - 密码为空 - userId: {}", userId);
            throw new IllegalArgumentException("密码不能为空");
        }
        
        if (captchaToken == null || captcha == null) {
            log.warn("✗ 登录失败 - 验证码缺失 - userId: {}", userId);
            return Result.error(400, "请输入验证码");
        }
        
        log.debug("开始验证验证码 - userId: {}, captchaToken长度: {}", userId, captchaToken.length());
        
        if (!CaptchaStorage.verifyCaptcha(captchaToken, captcha)) {
            log.warn("✗ 登录失败 - 验证码错误或已过期 - userId: {}, 输入验证码: {}", userId, captcha);
            return Result.error(400, "验证码错误或已过期");
        }
        
        log.info("✓ 验证码验证通过 - userId: {}", userId);
        
        try {
            Map<String, Object> loginResult;
            
            if (userId.startsWith("U")) {
                log.info("识别为普通用户登录 - userId: {}", userId);
                loginResult = userService.login(userId, password);
                User user = (User) loginResult.get("user");
                log.info("✓ 普通用户登录成功 - userId: {}, name: {}, role: {}", userId, user.getName(), user.getRole());
            } else {
                log.info("识别为员工/管理员登录 - userId: {}", userId);
                try {
                    log.info("尝试管理员登录 - userId: {}", userId);
                    loginResult = adminService.login(userId, password);
                    User admin = (User) loginResult.get("user");
                    log.info("✓ 管理员登录成功 - userId: {}, name: {}, role: {}", userId, admin.getName(), admin.getRole());
                } catch (BusinessException e) {
                    if (e.getCode() == 401 || e.getCode() == 404) {
                        log.info("管理员登录失败，尝试员工登录 - userId: {}, 原因: {}", userId, e.getMessage());
                        loginResult = staffService.login(userId, password);
                        User staff = (User) loginResult.get("user");
                        log.info("✓ 员工登录成功 - userId: {}, name: {}, role: {}", userId, staff.getName(), staff.getRole());
                    } else {
                        log.warn("✗ 登录失败 - userId: {}, 错误码: {}, 原因: {}", userId, e.getCode(), e.getMessage());
                        throw e;
                    }
                }
            }
            
            log.info("========== 登录请求处理完成 ==========");
            return Result.success(loginResult);
        } catch (BusinessException e) {
            log.warn("✗ 登录业务异常 - userId: {}, 错误码: {}, 原因: {}", userId, e.getCode(), e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("✗ 登录系统异常 - userId: {}, 异常信息: {}", userId, e.getMessage(), e);
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
        log.info("========== 开始处理注册请求 ==========");
        log.info("注册请求 - name: {}, email: {}, phone: {}", user.getName(), user.getEmail(), user.getPhone());
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            log.warn("✗ 注册失败 - 密码为空 - name: {}", user.getName());
            throw new IllegalArgumentException("密码不能为空");
        }
        
        if (user.getPassword().length() < 6) {
            log.warn("✗ 注册失败 - 密码长度不足 - name: {}, 密码长度: {}", user.getName(), user.getPassword().length());
            throw new IllegalArgumentException("密码长度不能少于 6 位");
        }
        
        if (captchaToken == null || captcha == null) {
            log.warn("✗ 注册失败 - 验证码缺失 - name: {}", user.getName());
            return Result.error(400, "请输入验证码");
        }
        
        log.debug("开始验证验证码 - name: {}, captchaToken长度: {}", user.getName(), captchaToken.length());
        
        if (!CaptchaStorage.verifyCaptcha(captchaToken, captcha)) {
            log.warn("✗ 注册失败 - 验证码错误或已过期 - name: {}, 输入验证码: {}", user.getName(), captcha);
            return Result.error(400, "验证码错误或已过期");
        }
        
        log.info("✓ 验证码验证通过 - name: {}", user.getName());
        
        try {
            String userId = userService.register(user);
            
            log.info("✓ 用户注册成功 - userId: {}, name: {}", userId, user.getName());
            log.info("========== 注册请求处理完成 ==========");
            
            return Result.success("注册成功，您的账号为：" + userId, null);
        } catch (Exception e) {
            log.error("✗ 用户注册失败 - name: {}, 异常信息: {}", user.getName(), e.getMessage(), e);
            return Result.error(500, "注册失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询用户订单接口
     * 
     * @param userId 用户ID，必填参数
     * @return Result 返回查询结果，成功时包含该用户的订单列表
     */
    @GetMapping("/orders")
    public Result getOrders(@RequestParam String userId) {
        log.info("========== 开始查询用户订单 ==========");
        log.info("查询订单请求 - userId: {}", userId);
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("✗ 查询订单失败 - 用户ID为空");
            return Result.error(400, "用户 ID 不能为空");
        }
        
        try {
            User user = userService.findByUserId(userId);
            if (user == null) {
                log.warn("✗ 查询订单失败 - 用户不存在 - userId: {}", userId);
                return Result.error(404, "用户不存在");
            }
            
            log.debug("用户存在 - userId: {}, name: {}", userId, user.getName());
            
            List<Order> orders = orderService.findByUserId(userId);
            
            log.info("✓ 查询订单成功 - userId: {}, 订单数量: {}", userId, orders.size());
            log.info("========== 查询订单完成 ==========");
            
            return Result.success(orders);
        } catch (Exception e) {
            log.error("✗ 查询订单异常 - userId: {}, 异常信息: {}", userId, e.getMessage(), e);
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
        log.info("========== 开始更新用户信息 ==========");
        log.info("更新用户信息请求 - userId: {}", userId);
        log.debug("待更新字段 - name: {}, email: {}, avatar: {}", user.getName(), user.getEmail(), user.getAvatar());
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("✗ 更新用户信息失败 - 用户ID为空");
            return Result.error(400, "用户 ID 不能为空");
        }
        
        try {
            userService.update(userId, user);
            
            log.info("✓ 用户信息更新成功 - userId: {}", userId);
            log.info("========== 更新用户信息完成 ==========");
            
            return Result.success("个人信息更新成功");
        } catch (BusinessException e) {
            log.warn("✗ 更新用户信息业务异常 - userId: {}, 错误码: {}, 原因: {}", userId, e.getCode(), e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("✗ 更新用户信息系统异常 - userId: {}, 异常信息: {}", userId, e.getMessage(), e);
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
        log.info("========== 开始上传头像 ==========");
        log.info("上传头像请求 - userId: {}", userId);
        
        if (file == null || file.isEmpty()) {
            log.warn("✗ 上传头像失败 - 文件为空 - userId: {}", userId);
            return Result.error(400, "请选择要上传的图片");
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("✗ 上传头像失败 - 用户ID为空");
            return Result.error(400, "用户ID不能为空");
        }
        
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        log.debug("文件信息 - 文件名: {}, 文件大小: {} bytes, 文件类型: {}", 
                originalFilename, file.getSize(), file.getContentType());
        
        if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png|gif|webp)$")) {
            log.warn("✗ 上传头像失败 - 文件格式不支持 - userId: {}, 文件名: {}", userId, originalFilename);
            return Result.error(400, "仅支持 jpg、jpeg、png、gif、webp 格式的图片");
        }
        
        // 验证文件大小（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            log.warn("✗ 上传头像失败 - 文件过大 - userId: {}, 文件大小: {} MB", 
                    userId, file.getSize() / 1024.0 / 1024.0);
            return Result.error(400, "图片大小不能超过 5MB");
        }
        
        log.info("文件验证通过 - userId: {}, 文件名: {}, 大小: {} KB", 
                userId, originalFilename, file.getSize() / 1024.0);
        
        try {
            log.debug("开始上传到阿里云OSS - userId: {}", userId);
            byte[] bytes = file.getBytes();
            String imageUrl = aliyunOSSOperator.upload(bytes, originalFilename);
            
            log.info("✓ 图片上传到OSS成功 - userId: {}, URL: {}", userId, imageUrl);
            
            // 更新数据库中的头像URL
            User updateRequest = new User();
            updateRequest.setAvatar(imageUrl);
            userService.update(userId, updateRequest);
            
            log.info("✓ 数据库头像URL更新成功 - userId: {}", userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", imageUrl);
            
            log.info("✓ 头像上传完成 - userId: {}", userId);
            log.info("========== 上传头像完成 ==========");
            
            return Result.success("头像上传成功", result);
        } catch (BusinessException e) {
            log.warn("✗ 上传头像业务异常 - userId: {}, 错误码: {}, 原因: {}", userId, e.getCode(), e.getMessage());
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("✗ 上传头像系统异常 - userId: {}, 异常信息: {}", userId, e.getMessage(), e);
            return Result.error(500, "头像上传失败：" + e.getMessage());
        }
    }




}

