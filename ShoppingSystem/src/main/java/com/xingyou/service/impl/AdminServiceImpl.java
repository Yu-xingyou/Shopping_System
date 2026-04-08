package com.xingyou.service.impl;

import com.xingyou.entity.people.Admin;
import com.xingyou.entity.people.Staff;
import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.AdminMapper;
import com.xingyou.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    /**
     * 查询所有用户
     * 
     * 获取系统中所有的用户列表信息。
     *
     * @return List<User> 返回所有用户的列表，如果没有用户则返回空列表
     */
    @Override
    public List<User> findAllUsers() {
        return adminMapper.findAllUsers();
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
        User user = adminMapper.findUserByUserId(userId);
        
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
        return adminMapper.findUserByName(name);
    }

    /**
     * 查询所有员工
     * 
     * 获取系统中所有的员工列表信息。
     *
     * @return List<Staff> 返回所有员工的列表，如果没有员工则返回空列表
     */
    @Override
    public List<Staff> findAllStaffs() {
        return adminMapper.findAllStaffs();
    }

    /**
     * 根据员工ID查询员工信息
     * 
     * 查询指定员工ID的员工信息，为安全起见会清除密码字段后返回。
     *
     * @param staffId 员工ID，用于指定需要查询的员工
     * @return Staff 返回员工对象，如果员工不存在则返回null；密码字段会被设置为null以保证安全
     */
    @Override
    public Staff findStaffByStaffId(Integer staffId) {
        Staff staff = adminMapper.findStaffByStaffId(staffId);
        
        // 为保护员工隐私和系统安全，清除敏感密码信息
        if (staff != null) {
            staff.setPassword(null);
        }
        return staff;
    }

    /**
     * 根据员工姓名模糊查询员工
     * 
     * 根据提供的员工姓名进行模糊匹配查询，返回所有匹配的员工列表。
     *
     * @param name 员工姓名（支持模糊匹配），用于搜索包含该姓名的员工
     * @return List<Staff> 返回匹配的员工列表，如果没有匹配结果则返回空列表
     */
    @Override
    public List<Staff> findStaffByName(String name) {
        return adminMapper.findStaffByName(name);
    }

    /**
     * 新增员工
     * 
     * 验证员工信息的合法性后，将新员工添加到系统中。
     * 默认状态设置为启用（status=1）。
     *
     * @param staff 员工对象，包含待添加的员工信息，必须包含姓名和密码
     * @throws BusinessException 当参数校验失败时抛出业务异常：
     *         - 400: 员工姓名不能为空
     *         - 400: 密码不能为空
     *         - 400: 密码长度不能少于6位
     */
    @Override
    public void addStaff(Staff staff) {
        // 验证员工姓名不能为空
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            throw new BusinessException(400, "员工姓名不能为空");
        }
        
        // 验证密码不能为空且长度不少于6位
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new BusinessException(400, "密码不能为空");
        }
        
        if (staff.getPassword().length() < 6) {
            throw new BusinessException(400, "密码长度不能少于 6 位");
        }
        
        // 设置默认状态为启用
        if (staff.getStatus() == null) {
            staff.setStatus(1);
        }
        
        adminMapper.addStaff(staff);
    }

    /**
     * 更新员工信息
     * 
     * 验证员工ID和状态信息的合法性后，更新系统中已有员工的信息。
     * 员工状态必须在有效范围内（1-正常、2-冻结、3-离职）。
     *
     * @param staff 员工对象，包含待更新的员工信息，必须包含有效的员工ID
     * @throws BusinessException 当参数校验失败时抛出业务异常：
     *         - 400: 员工ID不能为空
     *         - 404: 员工不存在
     *         - 400: 员工状态必须是1（正常）、2（冻结）或3（离职）
     */
    @Override
    public void updateStaff(Staff staff) {
        // 验证员工ID不能为空
        if (staff.getStaffId() == null) {
            throw new BusinessException(400, "员工 ID 不能为空");
        }
        
        // 验证员工是否存在
        Staff existingStaff = adminMapper.findStaffByStaffId(staff.getStaffId());
        if (existingStaff == null) {
            throw new BusinessException(404, "员工不存在");
        }
        
        // 验证员工状态的合法性，状态值必须在1-3范围内
        if (staff.getStatus() != null && (staff.getStatus() < 1 || staff.getStatus() > 3)) {
            throw new BusinessException(400, "员工状态必须是 1（正常）、2（冻结）或 3（离职）");
        }
        
        adminMapper.updateStaff(staff);
    }

    /**
     * 更新管理员信息
     * 
     * 验证管理员是否存在后，选择性更新管理员的姓名和密码信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     *
     * @param adminId 管理员ID，用于指定需要更新的管理员
     * @param admin 管理员对象，包含待更新的信息（姓名、密码），只有非空字段会被更新
     * @throws BusinessException 当管理员不存在时抛出404异常
     */
    @Override
    public void update(Integer adminId, Admin admin) {
        // 验证管理员是否存在
        Admin existingAdmin = adminMapper.findByAdminId(adminId);
        if (existingAdmin == null) {
            throw new BusinessException(404, "管理员不存在");
        }
        
        // 选择性更新管理员信息，只处理非空字段
        if (admin.getName() != null) {
            existingAdmin.setName(admin.getName());
        }
        if (admin.getPassword() != null && !admin.getPassword().trim().isEmpty()) {
            existingAdmin.setPassword(admin.getPassword());
        }
        
        adminMapper.update(existingAdmin);
    }

    /**
     * 更新商品库存
     * 
     * 验证商品ID和库存数量的合法性后，更新指定商品的库存信息。
     * 库存数量必须为非负数。
     *
     * @param id 商品ID，用于指定需要更新库存的商品
     * @param stock 新的库存数量，必须大于等于0
     * @throws BusinessException 当参数校验失败时抛出业务异常：
     *         - 400: 商品ID不能为空
     *         - 400: 库存数量不能为负数
     */
    @Override
    public void updateProductStock(Integer id, Integer stock) {
        // 验证商品ID不能为空
        if (id == null) {
            throw new BusinessException(400, "商品 ID 不能为空");
        }
        
        // 验证库存数量的合法性，不能为null且不能为负数
        if (stock == null || stock < 0) {
            throw new BusinessException(400, "库存数量不能为负数");
        }
        
        adminMapper.updateProductStock(id, stock);
    }

    /**
     * 管理员登录
     * 
     * 验证管理员ID和密码的合法性，校验通过后返回管理员信息。
     * 包括参数校验、用户存在性验证和密码匹配验证。
     *
     * @param adminId 管理员ID，用于标识登录的管理员
     * @param password 密码，用于验证管理员身份
     * @return Admin 登录成功时返回管理员对象
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 400: 管理员ID不能为空
     *         - 400: 密码不能为空
     *         - 404: 管理员不存在
     *         - 401: 密码错误
     */
    @Override
    public Admin login(Integer adminId, String password) {
        // 验证管理员ID和密码参数不能为空
        if (adminId == null) {
            throw new BusinessException(400, "管理员 ID 不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(400, "密码不能为空");
        }
        
        // 查询管理员并验证是否存在
        Admin admin = adminMapper.findByAdminId(adminId);
        if (admin == null) {
            throw new BusinessException(404, "管理员不存在");
        }
        
        // 验证密码是否正确
        if (!admin.getPassword().equals(password)) {
            throw new BusinessException(401, "密码错误");
        }
        
        return admin;
    }

    /**
     * 查询所有订单
     * 
     * 获取系统中所有的订单列表信息。
     *
     * @return List<Order> 返回所有订单的列表，如果没有订单则返回空列表
     */
    @Override
    public List<Order> findAllOrders() {
        return adminMapper.findAllOrders();
    }
}

