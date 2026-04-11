package com.xingyou.mapper;

import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单数据访问层接口
 * 
 * 提供订单的创建、查询、状态更新等核心功能，
 * 以及订单明细、用户余额、商品库存的相关操作。
 */
@Mapper
public interface OrderMapper {
    
    /**
     * 插入订单主表数据
     * 
     * 使用 useGeneratedKeys 自动获取生成的订单ID，
     * 用于后续关联订单明细。
     *
     * @param order 订单对象，包含待插入的订单信息
     */
    void insert(Order order);
    
    /**
     * 插入订单明细项
     *
     * @param orderItem 订单明细对象，包含商品信息、价格、数量等
     */
    void insertOrderItem(OrderItem orderItem);
    
    /**
     * 查询所有订单，按创建时间降序排列
     *
     * @return 系统中所有订单的列表
     */
    List<Order> findAll();
    
    /**
     * 根据订单ID查询订单
     *
     * @param id 订单ID
     * @return 订单对象，不存在则返回null
     */
    Order findById(@Param("id") Integer id);
    
    /**
     * 根据用户ID查询该用户的所有订单，按创建时间降序排列
     *
     * @param userId 用户ID
     * @return 该用户的订单列表
     */
    List<Order> findByUserId(@Param("userId") String userId);
    
    /**
     * 根据订单ID查询订单明细列表
     *
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    List<OrderItem> findOrderItemsByOrderId(@Param("orderId") Integer orderId);
    
    /**
     * 更新订单状态
     *
     * @param id 订单ID
     * @param status 新的订单状态
     */
    void updateStatus(Integer id, Integer status);
    
    void updateStatusToDelivered(Integer id, Integer status);
    
    void updateStatusToFinished(Integer id, Integer status);

    /**
     * 更新用户余额
     * 
     * 通过正负数控制充值或扣款：
     * 正数表示增加余额，负数表示扣除余额。
     *
     * @param userId 用户ID
     * @param money 金额变化量（正数充值，负数扣款）
     */
    void updateUserMoney(@Param("userId") String userId, @Param("money") Double money);
    
    /**
     * 根据用户ID查询用户信息（用于余额校验）
     *
     * @param userId 用户ID
     * @return 用户对象，不存在则返回null
     */
    User findUserByUserId(@Param("userId") String userId);
    
    /**
     * 扣减商品库存
     * 
     * 使用 WHERE 条件确保库存充足才执行更新，
     * 防止超卖现象发生。
     *
     * @param productId 商品ID
     * @param quantity 扣减数量
     */
    void updateProductStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
    
    /**
     * 根据商品ID查询商品信息（用于库存检查）
     *
     * @param id 商品ID
     * @return 商品对象，不存在则返回null
     */
    com.xingyou.entity.shopping.Product findProductById(@Param("id") Integer id);
    
    /**
     * 查询已完成订单的总收益（按时间范围筛选）
     * 
     * 只统计状态为3（已完成/已收货）的订单总金额。
     *
     * @param days 时间范围（天数），null表示查询全部
     * @return 总收益金额，如果没有订单则返回0
     */
    Double getTotalRevenue(@Param("days") Integer days);
    
    /**
     * 更新订单的小票图片URL
     *
     * @param id 订单ID
     * @param receiptImage 小票图片URL地址
     */
    void updateReceiptImage(@Param("id") Integer id, @Param("receiptImage") String receiptImage);
    
    /**
     * 查询当月热销产品TOP N（按销售数量排序）
     * 
     * 统计当月已完成订单中各商品的销售数量，返回销量最高的前N个商品。
     *
     * @param limit 返回的商品数量限制
     * @return 热销产品列表，包含商品ID、名称和销售数量
     */
    List<Map<String, Object>> getTopSellingProducts(@Param("limit") Integer limit);
}
