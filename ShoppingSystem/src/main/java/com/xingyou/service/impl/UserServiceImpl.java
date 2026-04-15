package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.UserMapper;
import com.xingyou.service.UserService;
import com.xingyou.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户登录
     * 
     * 验证用户ID和密码的合法性，校验通过后生成JWT令牌并返回用户信息。
     * 为安全起见，返回前会清除密码字段。
     *
     * @param userId 用户ID，用于标识登录的用户
     * @param password 密码，用于验证用户身份
     * @return Map<String, Object> 登录成功时返回包含用户信息和JWT令牌的Map:
     *         - user: 用户对象(密码已清除)
     *         - token: JWT认证令牌
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 401: 账号不存在
     *         - 401: 密码错误
     */
    @Override
    public Map<String, Object> login(String userId, String password) {
        log.info("用户登录请求 - userId: {}", userId);
        
        User user = userMapper.findByUserId(userId);
        
        if (user == null) {
            log.warn("登录失败 - 账号不存在: {}", userId);
            throw new BusinessException(401, "账号不存在");
        }
        
        if (!password.equals(user.getPassword())) {
            log.warn("登录失败 - 密码错误: {}", userId);
            throw new BusinessException(401, "密码错误");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());
        claims.put("role", 0);
        
        String token = JwtUtils.generateJwt(claims);
        
        user.setPassword(null);
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("token", token);
        
        log.info("用户登录成功 - userId: {}, name: {}", userId, user.getName());
        return result;
    }
    
    /**
     * 用户注册
     * 
     * 生成唯一的用户ID，设置默认属性（性别默认为"未知"，余额默认为0.0），
     * 并将新用户信息保存到数据库中。通过循环检查确保生成的用户ID不重复。
     *
     * @param user 用户对象，包含待注册的用户基本信息（姓名、密码等）
     * @return String 返回生成的唯一用户ID
     * @throws BusinessException 当注册失败时抛出业务异常：
     *         - 500: 注册失败，请稍后重试
     */
    @Override
    public String register(User user) {
        log.info("用户注册请求 - name: {}", user.getName());
        
        String userId = generateUserId();
        
        while (true) {
            User existingUser = userMapper.findByUserId(userId);
            if (existingUser == null) {
                break;
            }
            userId = generateUserId();
        }
        
        user.setUserId(userId);
        
        if (user.getSex() == null) {
            user.setSex(0);
        }
        user.setMoney(java.math.BigDecimal.ZERO);
        
        int rows = userMapper.insert(user);
        if (rows != 1) {
            log.error("用户注册失败 - name: {}", user.getName());
            throw new BusinessException(500, "注册失败，请稍后重试");
        }
        
        log.info("用户注册成功 - userId: {}, name: {}", userId, user.getName());
        return userId;
    }
    
    /**
     * 根据用户ID查询用户信息
     * 
     * 查询指定用户ID的用户信息，为安全起见会清除密码字段后返回。
     *
     * @param userId 用户ID，用于指定需要查询的用户
     * @return User 返回用户对象，如果用户不存在则返回null；密码字段会被设置为null以保证安全
     */
    @Override
    public User findByUserId(String userId) {
        log.debug("查询用户信息 - userId: {}", userId);
        
        User user = userMapper.findByUserId(userId);
        
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
    
    /**
     * 更新用户信息
     * 
     * 验证用户存在性后，选择性更新用户的姓名、性别和密码信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * 更新后会验证受影响的行数，确保操作成功。
     *
     * @param userId 用户ID，用于指定需要更新的用户
     * @param user 用户对象，包含待更新的信息（姓名、性别、密码），只有非空字段会被更新
     * @throws BusinessException 当验证失败或更新异常时抛出业务异常：
     *         - 404: 用户不存在
     *         - 500: 更新失败，请稍后重试
     */
    @Override
    public void update(String userId, User user) {
        log.info("更新用户信息请求 - userId: {}", userId);
        
        User existingUser = userMapper.findByUserId(userId);
        if (existingUser == null) {
            log.warn("更新失败 - 用户不存在: {}", userId);
            throw new BusinessException(404, "用户不存在");
        }
        
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getSex() != null) {
            existingUser.setSex(user.getSex());
        }
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        
        int rows = userMapper.update(existingUser);
        if (rows != 1) {
            log.error("更新用户信息失败 - userId: {}", userId);
            throw new BusinessException(500, "更新失败，请稍后重试");
        }
        
        log.info("更新用户信息成功 - userId: {}", userId);
    }

    /**
     * 生成用户ID
     * 
     * 根据当前时间戳和随机数生成唯一的用户编号。
     * 用户ID格式：U + yyyyMMddHHmmss（14位时间）+ 4位随机数
     * 示例：U202501151430251234
     *
     * @return String 生成的唯一用户ID
     */
    private String generateUserId() {
        // 格式化当前时间为14位时间戳（年月日时分秒）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        // 生成4位随机数（1000-9999），保证用户ID的唯一性
        String random = String.valueOf(new Random().nextInt(9000) + 1000);
        
        return "U" + timestamp + random;
    }

}
