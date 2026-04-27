package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Favorite;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.Product;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.AdminMapper;
import com.xingyou.service.AdminService;
import com.xingyou.service.FavoriteService;
import com.xingyou.service.NotificationService;
import com.xingyou.service.ProductService;
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
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    /**
     * 查询所有用户
     * 
     * 获取系统中所有的用户列表信息。
     *
     * @return List<User> 返回所有用户的列表，如果没有用户则返回空列表
     */
    @Override
    public List<User> findAllUsers() {
        log.debug("管理员查询所有用户");
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
        log.debug("管理员查询用户信息 - userId: {}", userId);
        User user = adminMapper.findUserByUserId(userId);
        
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
        log.debug("管理员模糊查询用户 - name: {}", name);
        return adminMapper.findUserByName(name);
    }

    /**
     * 查询所有员工
     * 
     * 获取系统中所有的员工列表信息。
     *
     * @return List<User> 返回所有员工的列表，如果没有员工则返回空列表
     */
    @Override
    public List<User> findAllStaffs() {
        log.debug("管理员查询所有员工");
        return adminMapper.findAllStaffs();
    }

    /**
     * 根据员工ID查询员工信息
     * 
     * 查询指定员工ID的员工信息，为安全起见会清除密码字段后返回。
     *
     * @param staffId 员工ID，用于指定需要查询的员工
     * @return User 返回员工用户对象，如果员工不存在则返回null；密码字段会被设置为null以保证安全
     */
    @Override
    public User findStaffByStaffId(String staffId) {
        log.debug("管理员查询员工信息 - staffId: {}", staffId);
        User staff = adminMapper.findStaffByStaffId(staffId);
        
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
     * @return List<User> 返回匹配的员工列表，如果没有匹配结果则返回空列表
     */
    @Override
    public List<User> findStaffByName(String name) {
        log.debug("管理员模糊查询员工 - name: {}", name);
        return adminMapper.findStaffByName(name);
    }

    /**
     * 新增员工
     * 
     * 验证员工信息的合法性后，将新员工添加到系统中。
     * 默认状态设置为启用（status=1），角色设置为员工（role=1）。
     *
     * @param staff 员工用户对象，包含待添加的员工信息，必须包含姓名和密码
     * @throws BusinessException 当参数校验失败时抛出业务异常：
     *         - 400: 员工姓名不能为空
     *         - 400: 密码不能为空
     *         - 400: 密码长度不能少于6位
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStaff(User staff) {
        log.info("管理员新增员工请求 - name: {}", staff.getName());
        
        if (staff.getName() == null || staff.getName().trim().isEmpty()) {
            throw new BusinessException(400, "员工姓名不能为空");
        }
        
        if (staff.getPassword() == null || staff.getPassword().trim().isEmpty()) {
            throw new BusinessException(400, "密码不能为空");
        }
        
        if (staff.getPassword().length() < 6) {
            throw new BusinessException(400, "密码长度不能少于 6 位");
        }
        
        if (staff.getStatus() == null) {
            staff.setStatus(1);
        }
        staff.setRole(1);
        
        staff.setPassword(passwordEncoderUtil.encode(staff.getPassword()));
        
        adminMapper.addStaff(staff);
        log.info("管理员新增员工成功 - staffId: {}, name: {}", staff.getUserId(), staff.getName());
    }

    /**
     * 更新员工信息
     * 
     * 验证员工ID和状态信息的合法性后，更新系统中已有员工的信息。
     * 员工状态必须在有效范围内（1-正常、2-冻结、3-离职）。
     *
     * @param staff 员工用户对象，包含待更新的员工信息，必须包含有效的用户ID
     * @throws BusinessException 当参数校验失败时抛出业务异常：
     *         - 400: 员工ID不能为空
     *         - 404: 员工不存在
     *         - 400: 员工状态必须是1（正常）、2（冻结）或3（离职）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStaff(User staff) {
        log.info("管理员更新员工信息请求 - staffId: {}", staff.getUserId());
        
        if (staff.getUserId() == null) {
            throw new BusinessException(400, "员工 ID 不能为空");
        }
        
        User existingStaff = adminMapper.findStaffByStaffId(staff.getUserId());
        if (existingStaff == null) {
            log.warn("更新员工失败 - 员工不存在: {}", staff.getUserId());
            throw new BusinessException(404, "员工不存在");
        }
        
        if (staff.getStatus() != null && (staff.getStatus() < 1 || staff.getStatus() > 3)) {
            throw new BusinessException(400, "员工状态必须是 1（正常）、2（冻结）或 3（离职）");
        }
        
        adminMapper.updateStaff(staff);
        log.info("管理员更新员工信息成功 - staffId: {}", staff.getUserId());
    }

    /**
     * 更新管理员信息
     * 验证管理员是否存在后，选择性更新管理员的姓名、密码、邮箱、手机和头像信息。
     * 只更新提供的非空字段，未提供的字段保持原值不变。
     * @param adminId 管理员ID，用于指定需要更新的管理员
     * @param admin 管理员用户对象，包含待更新的信息（姓名、密码、邮箱、手机、头像），只有非空字段会被更新
     * @throws BusinessException 当管理员不存在时抛出404异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(String adminId, User admin) {
        log.info("更新管理员信息请求 - adminId: {}", adminId);
        
        User existingAdmin = adminMapper.findByAdminId(adminId);
        if (existingAdmin == null) {
            log.warn("更新管理员失败 - 管理员不存在: {}", adminId);
            throw new BusinessException(404, "管理员不存在");
        }
        
        if (admin.getName() != null) {
            existingAdmin.setName(admin.getName());
        }
        if (admin.getPassword() != null && !admin.getPassword().trim().isEmpty()) {
            existingAdmin.setPassword(passwordEncoderUtil.encode(admin.getPassword()));
        }
        if (admin.getEmail() != null) {
            existingAdmin.setEmail(admin.getEmail());
        }
        if (admin.getPhone() != null) {
            existingAdmin.setPhone(admin.getPhone());
        }
        if (admin.getAvatar() != null) {
            existingAdmin.setAvatar(admin.getAvatar());
        }
        
        adminMapper.update(existingAdmin);
        log.info("更新管理员信息成功 - adminId: {}", adminId);
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
    @Transactional(rollbackFor = Exception.class)
    public void updateProductStock(Integer id, Integer stock) {
        log.info("管理员更新商品库存请求 - productId: {}, oldStock: ?, newStock: {}", id, stock);
        
        if (id == null) {
            throw new BusinessException(400, "商品 ID 不能为空");
        }
        
        if (stock == null || stock < 0) {
            throw new BusinessException(400, "库存数量不能为负数");
        }
        
        Product product = productService.findById(id);
        if (product == null) {
            log.warn("更新库存失败 - 商品不存在: {}", id);
            throw new BusinessException(404, "商品不存在");
        }
        
        Integer oldStock = product.getStock();
        log.info("更新商品库存 - productId: {}, oldStock: {}, newStock: {}", id, oldStock, stock);
        
        adminMapper.updateProductStock(id, stock);
        
        if (oldStock != null && oldStock == 0 && stock > 0) {
            List<Favorite> favorites = favoriteService.findByProductId(id);
            if (favorites != null && !favorites.isEmpty()) {
                String productName = product.getName() != null ? product.getName() : "未知商品";
                log.info("商品补货通知 - productId: {}, productName: {}, 通知用户数: {}", 
                        id, productName, favorites.size());
                for (Favorite favorite : favorites) {
                    notificationService.createRestockNotification(favorite.getUserId(), id, productName);
                }
            }
        }
    }

    /**
     * 管理员登录
     * 
     * 验证管理员ID和密码的合法性,校验通过后生成JWT令牌并返回管理员信息。
     * 包括参数校验、用户存在性验证和密码匹配验证。
     *
     * @param adminId 管理员ID,用于标识登录的管理员
     * @param password 密码,用于验证管理员身份
     * @return Map<String, Object> 登录成功时返回包含管理员信息和JWT令牌的Map:
     *         - user: 管理员用户对象(密码已清除)
     *         - token: JWT认证令牌
     * @throws BusinessException 当验证失败时抛出业务异常:
     *         - 400: 管理员ID不能为空
     *         - 400: 密码不能为空
     *         - 404: 管理员不存在
     *         - 401: 密码错误
     */
    @Override
    public Map<String, Object> login(String adminId, String password) {
        log.info("管理员登录请求 - adminId: {}", adminId);
        
        if (adminId == null || adminId.trim().isEmpty()) {
            throw new BusinessException(400, "管理员 ID 不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(400, "密码不能为空");
        }
        
        User admin = adminMapper.findByAdminId(adminId);
        if (admin == null) {
            log.warn("管理员登录失败 - 管理员不存在: {}", adminId);
            throw new BusinessException(404, "管理员不存在");
        }
        
        log.debug("密码验证 - adminId: {}, 输入密码长度: {}, 数据库密码前20位: {}", 
                adminId, password != null ? password.length() : 0, 
                admin.getPassword() != null ? admin.getPassword().substring(0, 20) : "null");
        
        boolean matches = passwordEncoderUtil.matches(password, admin.getPassword());
        log.debug("密码匹配结果 - adminId: {}, 是否匹配: {}", adminId, matches);
        
        if (!matches) {
            log.warn("管理员登录失败 - 密码错误: {}", adminId);
            throw new BusinessException(401, "密码错误");
        }
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admin.getUserId());
        claims.put("name", admin.getName());
        claims.put("role", 2);
        
        String token = JwtUtils.generateJwt(claims);
        
        admin.setPassword(null);
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", admin);
        result.put("token", token);
        
        log.info("管理员登录成功 - adminId: {}, name: {}", adminId, admin.getName());
        return result;
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
        log.debug("管理员查询所有订单");
        return adminMapper.findAllOrders();
    }
    
    /**
     * 查询管理员总收益（按时间范围筛选）
     * 
     * 只统计状态为3（已完成/已收货）的订单总金额。
     *
     * @param days 时间范围（天数），可选参数：
     *             - null: 查询全部时间
     *             - 1: 1天内
     *             - 7: 7天内
     *             - 30: 一个月内
     * @return Double 返回总收益金额，如果没有订单则返回0.0
     */
    @Override
    public Double getTotalRevenue(Integer days) {
        log.debug("查询管理员总收益 - days: {}", days);
        Double revenue = adminMapper.getTotalRevenue(days);
        return revenue != null ? revenue : 0.0;
    }
}

