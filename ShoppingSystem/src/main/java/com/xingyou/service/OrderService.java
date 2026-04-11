package com.xingyou.service;

import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 订单服务层接口
 * 
 * 提供订单的创建、查询、状态管理等核心业务逻辑，
 * 包括订单取消、确认收货等功能。
 */
@Service
public interface OrderService {
    
    /**
     * 创建订单
     * 
     * 验证用户信息和商品列表后，计算订单总金额并生成订单编号，
     * 校验用户余额是否充足，然后保存订单及订单明细到数据库。
     * 订单初始状态设置为0（待付款）。
     *
     * @param order 订单对象，包含用户ID等基本信息
     * @param orderItems 订单商品明细列表，必须包含至少一个商品
     * @return Order 创建成功后的订单对象，包含生成的订单ID和订单号
     */
    Order createOrder(Order order, List<OrderItem> orderItems);
    
    /**
     * 查询所有订单
     * 
     * 获取系统中所有的订单列表信息。
     *
     * @return List<Order> 返回所有订单的列表，如果没有订单则返回空列表
     */
    List<Order> findAll();
    
    /**
     * 根据订单ID查询订单
     * 
     * 根据指定的订单ID查询订单详细信息。
     *
     * @param id 订单ID，用于指定需要查询的订单
     * @return Order 返回订单对象，如果订单不存在则返回null
     */
    Order findById(Integer id);
    
    /**
     * 根据用户ID查询订单列表
     * 
     * 根据指定的用户ID查询该用户的所有订单信息。
     *
     * @param userId 用户ID，用于指定需要查询订单的用户
     * @return List<Order> 返回该用户的订单列表，如果没有订单则返回空列表
     */
    List<Order> findByUserId(String userId);
    
    /**
     * 根据订单ID查询订单明细
     * 
     * 根据指定的订单ID查询该订单包含的所有商品明细信息。
     *
     * @param orderId 订单ID，用于指定需要查询明细的订单
     * @return List<OrderItem> 返回订单商品明细列表，如果没有明细则返回空列表
     */
    List<OrderItem> findOrderItemsByOrderId(Integer orderId);
    
    /**
     * 更新订单状态
     * 
     * 验证订单存在性后更新订单状态。当状态更新为2（已发货）时，会执行库存扣减逻辑：
     * 检查订单中所有商品的库存是否充足，如果库存不足则抛出异常阻止发货；
     * 如果库存充足则批量扣减相应商品的库存数量。
     *
     * @param id 订单ID，用于指定需要更新状态的订单
     * @param status 新的订单状态值
     */
    void updateStatus(Integer id, Integer status);
    
    /**
     * 员工取消订单
     * 
     * 验证订单存在性和状态后，将订单状态更新为已取消（状态4）。
     * 只有状态小于3的订单才能被取消，已完成或已关闭的订单不可取消。
     *
     * @param id 订单ID，用于指定需要取消的订单
     */
    void cancelOrderByStaff(Integer id);
    
    /**
     * 用户取消订单
     * 
     * 验证订单存在性、用户权限和订单状态后，允许用户取消自己的订单。
     * 如果订单已发货（状态2），取消时需扣除5%的违约金。
     * 取消成功后将订单状态更新为已取消（状态4）。
     *
     * @param id 订单ID，用于指定需要取消的订单
     * @param userId 用户ID，用于验证操作用户是否为订单所有者
     */
    void cancelOrderByUser(Integer id, String userId);
    
    /**
     * 用户确认收货
     * 
     * 验证订单存在性、用户权限和订单状态后，允许用户确认收货。
     * 只有已发货（状态2）的订单才能确认收货，确认成功后将订单状态更新为已完成（状态3），
     * 并从用户余额中扣除订单总金额完成结算。
     *
     * @param id 订单ID，用于指定需要确认收货的订单
     * @param userId 用户ID，用于验证操作用户是否为订单所有者
     * @param receiptImageUrl 小票图片URL地址
     */
    void confirmReceipt(Integer id, String userId, String receiptImageUrl);
    
    /**
     * 查询当月热销产品TOP N
     * 
     * 统计当月已完成订单中各商品的销售数量，返回销量最高的前N个商品。
     *
     * @param limit 返回的商品数量限制，默认为10
     * @return 热销产品列表，每个元素包含productId、productName和totalQuantity
     */
    List<Map<String, Object>> getTopSellingProducts(Integer limit);
}
