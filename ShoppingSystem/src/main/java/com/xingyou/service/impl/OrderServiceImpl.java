package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.OrderMapper;
import com.xingyou.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
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
     * @throws BusinessException 当参数校验或业务规则验证失败时抛出业务异常：
     *         - 400: 订单商品不能为空
     *         - 400: 用户ID不能为空
     *         - 404: 用户不存在
     *         - 400: 余额不足，无法创建订单
     */
    @Override
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        log.info("创建订单请求 - userId: {}, 商品数量: {}", order.getUserId(), orderItems.size());
        
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException(400, "订单商品不能为空");
        }
        
        if (order.getUserId() == null) {
            throw new BusinessException(400, "用户 ID 不能为空");
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemPrice = item.getProductPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal itemAmount = itemPrice.multiply(quantity);
            totalAmount = totalAmount.add(itemAmount);
        }
        order.setTotalAmount(totalAmount);
        
        User existingUser = orderMapper.findUserByUserId(order.getUserId());
        if (existingUser == null) {
            log.warn("创建订单失败 - 用户不存在: {}", order.getUserId());
            throw new BusinessException(404, "用户不存在");
        }
        
        BigDecimal userMoney = existingUser.getMoney();
        if (userMoney == null || userMoney.compareTo(totalAmount) < 0) {
            log.warn("创建订单失败 - 余额不足: userId: {}, 订单金额: {}, 用户余额: {}", 
                    order.getUserId(), totalAmount, userMoney);
            throw new BusinessException(400, "余额不足，无法创建订单");
        }
        
        String orderNum = generateOrderNum();
        order.setOrderNum(orderNum);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        
        orderMapper.insert(order);
        
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderMapper.insertOrderItem(item);
        }
        
        log.info("创建订单成功 - orderId: {}, orderNum: {}, totalAmount: {}", 
                order.getId(), orderNum, totalAmount);
        return order;
    }
    
    /**
     * 查询所有订单
     * 
     * 获取系统中所有的订单列表信息。
     *
     * @return List<Order> 返回所有订单的列表，如果没有订单则返回空列表
     */
    @Override
    public List<Order> findAll() {
        log.debug("查询所有订单");
        return orderMapper.findAll();
    }
    
    /**
     * 根据订单ID查询订单
     * 
     * 根据指定的订单ID查询订单详细信息。
     *
     * @param id 订单ID，用于指定需要查询的订单
     * @return Order 返回订单对象，如果订单不存在则返回null
     */
    @Override
    public Order findById(Integer id) {
        log.debug("根据ID查询订单 - id: {}", id);
        return orderMapper.findById(id);
    }
    
    /**
     * 根据用户ID查询订单列表
     * 
     * 根据指定的用户ID查询该用户的所有订单信息。
     *
     * @param userId 用户ID，用于指定需要查询订单的用户
     * @return List<Order> 返回该用户的订单列表，如果没有订单则返回空列表
     */
    @Override
    public List<Order> findByUserId(String userId) {
        log.debug("根据用户ID查询订单 - userId: {}", userId);
        return orderMapper.findByUserId(userId);
    }
    
    /**
     * 根据订单ID查询订单明细
     * 
     * 根据指定的订单ID查询该订单包含的所有商品明细信息。
     *
     * @param orderId 订单ID，用于指定需要查询明细的订单
     * @return List<OrderItem> 返回订单商品明细列表，如果没有明细则返回空列表
     */
    @Override
    public List<OrderItem> findOrderItemsByOrderId(Integer orderId) {
        log.debug("根据订单ID查询订单明细 - orderId: {}", orderId);
        return orderMapper.findOrderItemsByOrderId(orderId);
    }
    
    /**
     * 更新订单状态
     * 
     * 验证订单存在性后更新订单状态。当状态更新为2（已发货）时，会执行库存扣减逻辑：
     * 检查订单中所有商品的库存是否充足，如果库存不足则抛出异常阻止发货；
     * 如果库存充足则批量扣减相应商品的库存数量。
     *
     * @param id 订单ID，用于指定需要更新状态的订单
     * @param status 新的订单状态值
     * @throws BusinessException 当验证失败或业务规则不满足时抛出业务异常：
     *         - 404: 订单不存在
     *         - 404: 商品不存在
     *         - 400: 商品库存不足，无法发货
     */
    @Override
    public void updateStatus(Integer id, Integer status) {
        log.info("更新订单状态请求 - orderId: {}, newStatus: {}", id, status);
        
        Order order = orderMapper.findById(id);
        if (order == null) {
            log.warn("更新订单状态失败 - 订单不存在: {}", id);
            throw new BusinessException(404, "订单不存在");
        }
        
        if (status == 2 && order.getStatus() != 2) {
            List<OrderItem> items = orderMapper.findOrderItemsByOrderId(id);
            if (items != null && !items.isEmpty()) {
                List<String> insufficientProducts = new java.util.ArrayList<>();
                
                for (OrderItem item : items) {
                    com.xingyou.entity.shopping.Product product = orderMapper.findProductById(item.getProductId());
                    if (product == null) {
                        log.error("更新订单状态失败 - 商品不存在: productId: {}", item.getProductId());
                        throw new BusinessException(404, "商品【" + item.getProductName() + "】不存在");
                    }
                    
                    int currentStock = product.getStock() != null ? product.getStock() : 0;
                    if (currentStock < item.getQuantity()) {
                        insufficientProducts.add("【" + item.getProductName() + "】(当前库存:" + currentStock + ", 需要:" + item.getQuantity() + ")");
                    }
                }
                
                if (!insufficientProducts.isEmpty()) {
                    String errorMsg = "以下商品库存不足，无法发货：" + String.join("、", insufficientProducts);
                    log.warn("更新订单状态失败 - 库存不足: orderId: {}, details: {}", id, errorMsg);
                    throw new BusinessException(400, errorMsg);
                }
                
                for (OrderItem item : items) {
                    orderMapper.updateProductStock(item.getProductId(), item.getQuantity());
                }
                log.info("订单发货 - 库存扣减完成: orderId: {}", id);
            }
        }
        
        if (status == 2 && order.getStatus() != 2) {
            orderMapper.updateStatusToDelivered(id, status);
        } else if (status == 3 && order.getStatus() != 3) {
            orderMapper.updateStatusToFinished(id, status);
        } else {
            orderMapper.updateStatus(id, status);
        }
        
        log.info("更新订单状态成功 - orderId: {}, status: {}", id, status);
    }
    
    /**
     * 员工取消订单
     * 
     * 验证订单存在性和状态后，将订单状态更新为已取消（状态4）。
     * 只有状态小于3的订单才能被取消，已完成或已关闭的订单不可取消。
     *
     * @param id 订单ID，用于指定需要取消的订单
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 404: 订单不存在
     *         - 400: 该订单不可取消（订单状态>=3）
     */
    @Override
    public void cancelOrderByStaff(Integer id) {
        log.info("员工取消订单请求 - orderId: {}", id);
        
        Order order = orderMapper.findById(id);
        if (order == null) {
            log.warn("取消订单失败 - 订单不存在: {}", id);
            throw new BusinessException(404, "订单不存在");
        }
        
        if (order.getStatus() >= 3) {
            log.warn("取消订单失败 - 订单不可取消: orderId: {}, status: {}", id, order.getStatus());
            throw new BusinessException(400, "该订单不可取消");
        }
        
        orderMapper.updateStatus(id, 4);
        log.info("员工取消订单成功 - orderId: {}", id);
    }
    
    /**
     * 用户取消订单
     * 
     * 验证订单存在性、用户权限和订单状态后，允许用户取消自己的订单。
     * 如果订单已发货（状态2），取消时需扣除5%的违约金。
     * 取消成功后将订单状态更新为已取消（状态4）。
     *
     * @param id 订单ID，用于指定需要取消的订单
     * @param userId 用户ID，用于验证操作用户是否为订单所有者
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 404: 订单不存在
     *         - 403: 无权取消该订单（非订单所有者）
     *         - 400: 该订单不可取消（订单状态>=3）
     */
    @Override
    public void cancelOrderByUser(Integer id, String userId) {
        log.info("用户取消订单请求 - orderId: {}, userId: {}", id, userId);
        
        Order order = orderMapper.findById(id);
        if (order == null) {
            log.warn("取消订单失败 - 订单不存在: {}", id);
            throw new BusinessException(404, "订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            log.warn("取消订单失败 - 无权操作: orderId: {}, userId: {}", id, userId);
            throw new BusinessException(403, "无权取消该订单");
        }
        
        if (order.getStatus() >= 3) {
            log.warn("取消订单失败 - 订单不可取消: orderId: {}, status: {}", id, order.getStatus());
            throw new BusinessException(400, "该订单不可取消");
        }
        
        if (order.getStatus() == 2) {
            BigDecimal penalty = order.getTotalAmount().multiply(new BigDecimal("0.05"));
            orderMapper.updateUserMoney(userId, -penalty.doubleValue());
            log.info("订单已发货取消 - 扣除违约金: orderId: {}, penalty: {}", id, penalty);
        }
        
        orderMapper.updateStatus(id, 4);
        log.info("用户取消订单成功 - orderId: {}", id);
    }
    
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
     * @throws BusinessException 当验证失败时抛出业务异常：
     *         - 404: 订单不存在
     *         - 403: 无权确认该订单（非订单所有者）
     *         - 400: 只有已发货的订单才能确认收货
     */
    @Override
    public void confirmReceipt(Integer id, String userId, String receiptImageUrl) {
        log.info("用户确认收货请求 - orderId: {}, userId: {}", id, userId);
        
        Order order = orderMapper.findById(id);
        if (order == null) {
            log.warn("确认收货失败 - 订单不存在: {}", id);
            throw new BusinessException(404, "订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            log.warn("确认收货失败 - 无权操作: orderId: {}, userId: {}", id, userId);
            throw new BusinessException(403, "无权确认该订单");
        }
        
        if (order.getStatus() != 2) {
            log.warn("确认收货失败 - 订单状态不正确: orderId: {}, status: {}", id, order.getStatus());
            throw new BusinessException(400, "只有已发货的订单才能确认收货");
        }
        
        orderMapper.updateReceiptImage(id, receiptImageUrl);
        
        orderMapper.updateStatusToFinished(id, 3);
        
        orderMapper.updateUserMoney(userId, -order.getTotalAmount().doubleValue());
        
        log.info("用户确认收货成功 - orderId: {}, amount: {}", id, order.getTotalAmount());
    }
    
    /**
     * 生成订单号
     * 
     * 根据当前时间戳和随机数生成唯一的订单编号。
     * 订单号格式：O + yyyyMMddHHmmss（14位时间）+ 4位随机数
     * 示例：O202501151430251234
     *
     * @return String 生成的唯一订单号
     */
    private String generateOrderNum() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        
        String random = String.valueOf(new Random().nextInt(9000) + 1000);
        
        return "O" + timestamp + random;
    }

    /**
     * 查询当月热销产品TOP N
     * 
     * 统计当月已完成订单中各商品的销售数量，返回销量最高的前N个商品。
     *
     * @param limit 返回的商品数量限制，默认为10
     * @return List<Map<String, Object>> 热销产品列表，每个元素包含productId、productName和totalQuantity
     */
    @Override
    public List<Map<String, Object>> getTopSellingProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        log.debug("查询热销产品TOP {} - limit: {}", limit, limit);
        return orderMapper.getTopSellingProducts(limit);
    }

}
