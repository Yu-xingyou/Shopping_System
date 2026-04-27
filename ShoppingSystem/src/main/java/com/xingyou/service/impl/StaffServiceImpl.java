package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.StaffMapper;
import com.xingyou.service.StaffService;
import com.xingyou.util.JwtUtils;
import com.xingyou.util.PasswordEncoderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StaffServiceImpl implements StaffService {
    
    @Autowired
    private StaffMapper staffMapper;
    
    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;
    
    /**
     * 员工登录
     * 
     * 验证员工ID、密码和账号状态，校验通过后生成JWT令牌并返回员工信息。
     * 只有状态为1（正常）的员工才能登录系统。
     *
     * @param staffId 员工ID，用于标识登录的员工
     * @param password 密码，用于验证员工身份
     * @return Map<String, Object> 登录成功时返回包含员工信息和JWT令牌的Map:
     *         - user: 员工用户对象
     *         - token: JWT认证令牌
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 401: 员工ID不存在
     *         - 401: 密码错误
     *         - 403: 登录失败（账号被冻结、离职或状态异常）
     */
    @Override
    public Map<String, Object> login(String staffId, String password) {
        log.info("员工登录请求 - staffId: {}", staffId);
        
        User staff = staffMapper.findById(staffId);
        
        if (staff == null) {
            log.warn("员工登录失败 - 员工ID不存在: {}", staffId);
            throw new BusinessException(401, "员工 ID 不存在");
        }
        
        log.debug("密码验证 - staffId: {}, 输入密码长度: {}, 数据库密码前20位: {}", 
                staffId, password != null ? password.length() : 0, 
                staff.getPassword() != null ? staff.getPassword().substring(0, 20) : "null");
        
        boolean matches = passwordEncoderUtil.matches(password, staff.getPassword());
        log.debug("密码匹配结果 - staffId: {}, 是否匹配: {}", staffId, matches);
        
        if (!matches) {
            log.warn("员工登录失败 - 密码错误: {}", staffId);
            throw new BusinessException(401, "密码错误");
        }
        
        if (staff.getStatus() == null || staff.getStatus() != 1) {
            String statusMsg = getStatusMessage(staff.getStatus());
            log.warn("员工登录失败 - 账号状态异常: staffId: {}, status: {}", staffId, staff.getStatus());
            throw new BusinessException(403, "登录失败：" + statusMsg);
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", staff.getUserId());
        claims.put("name", staff.getName());
        claims.put("role", 1);
        
        String token = JwtUtils.generateJwt(claims);
        
        staff.setPassword(null);
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", staff);
        result.put("token", token);
        
        log.info("员工登录成功 - staffId: {}, name: {}", staffId, staff.getName());
        return result;
    }

    /**
     * 获取员工状态提示信息
     * 
     * 根据员工状态码返回对应的中文提示信息，用于登录失败时的错误提示。
     *
     * @param status 员工状态码：2-冻结，3-离职，null或其他值-状态异常
     * @return String 返回对应的状态提示消息
     */
    private String getStatusMessage(Integer status) {
        if (status == null) {
            return "账号状态异常";
        }
        
        // 根据状态码返回具体的提示信息
        switch (status) {
            case 2:
                return "账号已冻结，无法登录";
            case 3:
                return "账号已离职，无法登录";
            default:
                return "账号状态异常";
        }
    }

    /**
     * 更新员工信息
     * 
     * 验证员工存在性后，选择性更新员工的姓名、密码、邮箱、手机和头像信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * 更新后会验证受影响的行数，确保操作成功。
     *
     * @param staffId 员工ID，用于指定需要更新的员工
     * @param staff 员工用户对象，包含待更新的信息（姓名、密码、邮箱、手机、头像），只有非空字段会被更新
     * @throws BusinessException 当验证失败或更新异常时抛出业务异常：
     *         - 404: 员工不存在
     *         - 500: 更新失败，请稍后重试
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String staffId, User staff) {
        log.info("更新员工信息请求 - staffId: {}", staffId);
        
        User existingStaff = staffMapper.findByStaffId(staffId);
        if (existingStaff == null) {
            log.warn("更新员工失败 - 员工不存在: {}", staffId);
            throw new BusinessException(404, "员工不存在");
        }
        
        if (staff.getName() != null) {
            existingStaff.setName(staff.getName());
        }
        if (staff.getPassword() != null && !staff.getPassword().trim().isEmpty()) {
            existingStaff.setPassword(passwordEncoderUtil.encode(staff.getPassword()));
        }
        if (staff.getEmail() != null) {
            existingStaff.setEmail(staff.getEmail());
        }
        if (staff.getPhone() != null) {
            existingStaff.setPhone(staff.getPhone());
        }
        if (staff.getAvatar() != null) {
            existingStaff.setAvatar(staff.getAvatar());
        }
        
        int rows = staffMapper.update(existingStaff);
        if (rows != 1) {
            log.error("更新员工信息失败 - staffId: {}", staffId);
            throw new BusinessException(500, "更新失败，请稍后重试");
        }
        
        log.info("更新员工信息成功 - staffId: {}", staffId);
    }
    
    /**
     * 查询所有用户
     * 
     * 获取系统中所有的用户列表信息。
     *
     * @return List<User> 返回所有用户的列表，如果没有用户则返回空列表
     */
    @Override
    public List<User> findAllUsers() {
        log.debug("员工查询所有用户");
        return staffMapper.findAllUsers();
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
    public User findUserByUserId(String userId) {
        log.debug("员工查询用户信息 - userId: {}", userId);
        User user = staffMapper.findUserByUserId(userId);
        
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
    
    /**
     * 根据用户名模糊查询用户
     * 
     * 根据提供的用户名进行模糊匹配查询，返回所有匹配的用户列表。
     *
     * @param name 用户名（支持模糊匹配），用于搜索包含该名称的用户
     * @return List<User> 返回匹配的用户列表，如果没有匹配结果则返回空列表
     */
    @Override
    public List<User> findUserByName(String name) {
        log.debug("员工模糊查询用户 - name: {}", name);
        return staffMapper.findUserByName(name);
    }
}
