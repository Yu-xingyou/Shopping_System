package com.xingyou.service.impl;

import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.StaffMapper;
import com.xingyou.service.StaffService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {
    
    @Resource
    private StaffMapper staffMapper;
    
    /**
     * 员工登录
     * 
     * 验证员工ID、密码和账号状态，校验通过后返回员工信息。
     * 只有状态为1（正常）的员工才能登录系统。
     *
     * @param staffId 员工ID，用于标识登录的员工
     * @param password 密码，用于验证员工身份
     * @return Staff 登录成功时返回员工对象
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 401: 员工ID不存在
     *         - 401: 密码错误
     *         - 403: 登录失败（账号被冻结、离职或状态异常）
     */
    @Override
    public Staff login(Integer staffId, String password) {
        // 查询员工信息并验证是否存在
        Staff staff = staffMapper.findById(staffId);
        
        if (staff == null) {
            throw new BusinessException(401, "员工 ID 不存在");
        }
        
        // 验证密码是否正确
        if (!password.equals(staff.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }
        
        // 验证员工状态，只有状态为1（正常）的员工才允许登录
        if (staff.getStatus() == null || staff.getStatus() != 1) {
            String statusMsg = getStatusMessage(staff.getStatus());
            throw new BusinessException(403, "登录失败：" + statusMsg);
        }
        
        return staff;
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
     * 验证员工存在性后，选择性更新员工的姓名和密码信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * 更新后会验证受影响的行数，确保操作成功。
     *
     * @param staffId 员工ID，用于指定需要更新的员工
     * @param staff 员工对象，包含待更新的信息（姓名、密码），只有非空字段会被更新
     * @throws BusinessException 当验证失败或更新异常时抛出业务异常：
     *         - 404: 员工不存在
     *         - 500: 更新失败，请稍后重试
     */
    @Override
    public void update(Integer staffId, Staff staff) {
        // 验证员工是否存在
        Staff existingStaff = staffMapper.findByStaffId(staffId);
        if (existingStaff == null) {
            throw new BusinessException(404, "员工不存在");
        }
        
        // 选择性更新员工信息，只处理非空字段
        if (staff.getName() != null) {
            existingStaff.setName(staff.getName());
        }
        if (staff.getPassword() != null && !staff.getPassword().trim().isEmpty()) {
            existingStaff.setPassword(staff.getPassword());
        }
        
        // 执行更新并验证结果
        int rows = staffMapper.update(existingStaff);
        if (rows != 1) {
            throw new BusinessException(500, "更新失败，请稍后重试");
        }
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
        User user = staffMapper.findUserByUserId(userId);
        
        // 为保护用户隐私和系统安全，清除敏感密码信息
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
        return staffMapper.findUserByName(name);
    }
}
