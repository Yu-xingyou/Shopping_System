package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.OrderMapper;
import com.xingyou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        // 验证订单商品列表不能为空
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException(400, "订单商品不能为空");
        }
        
        // 验证用户ID不能为空
        if (order.getUserId() == null) {
            throw new BusinessException(400, "用户 ID 不能为空");
        }
        
        // 计算订单总金额：遍历所有商品，累加（单价 × 数量）
        double totalAmount = 0.0;
        for (OrderItem item : orderItems) {
            double itemAmount = item.getProductPrice() * item.getQuantity();
            totalAmount += itemAmount;
        }
        order.setTotalAmount(totalAmount);
        
        // 验证用户是否存在
        User existingUser = orderMapper.findUserByUserId(order.getUserId());
        if (existingUser == null) {
            throw new BusinessException(404, "用户不存在");
        }
        
        // 验证用户余额是否充足
        if (existingUser.getMoney() == null || existingUser.getMoney() < totalAmount) {
            throw new BusinessException(400, "余额不足，无法创建订单");
        }
        
        // 设置订单的初始属性：订单号、状态（0-待付款）、创建时间
        String orderNum = generateOrderNum();
        order.setOrderNum(orderNum);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        
        // 插入订单主表数据
        orderMapper.insert(order);
        
        // 批量插入订单明细数据，关联订单ID
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderMapper.insertOrderItem(item);
        }
        
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
        // 验证订单是否存在
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        // 当订单状态变更为已发货（status=2）时，执行库存扣减逻辑
        if (status == 2 && order.getStatus() != 2) {
            List<OrderItem> items = orderMapper.findOrderItemsByOrderId(id);
            if (items != null && !items.isEmpty()) {
                List<String> insufficientProducts = new java.util.ArrayList<>();
                
                // 遍历订单中的所有商品，检查库存是否充足
                for (OrderItem item : items) {
                    com.xingyou.entity.shopping.Product product = orderMapper.findProductById(item.getProductId());
                    if (product == null) {
                        throw new BusinessException(404, "商品【" + item.getProductName() + "】不存在");
                    }
                    
                    int currentStock = product.getStock() != null ? product.getStock() : 0;
                    if (currentStock < item.getQuantity()) {
                        insufficientProducts.add("【" + item.getProductName() + "】(当前库存:" + currentStock + ", 需要:" + item.getQuantity() + ")");
                    }
                }
                
                // 如果有商品库存不足，抛出异常并列出所有库存不足的商品
                if (!insufficientProducts.isEmpty()) {
                    String errorMsg = "以下商品库存不足，无法发货：" + String.join("、", insufficientProducts);
                    throw new BusinessException(400, errorMsg);
                }
                
                // 库存充足，批量扣减订单中所有商品的库存
                for (OrderItem item : items) {
                    orderMapper.updateProductStock(item.getProductId(), item.getQuantity());
                }
            }
        }
        
        if (status == 2 && order.getStatus() != 2) {
            orderMapper.updateStatusToDelivered(id, status);
        } else if (status == 3 && order.getStatus() != 3) {
            orderMapper.updateStatusToFinished(id, status);
        } else {
            orderMapper.updateStatus(id, status);
        }
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
        // 验证订单是否存在
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        // 验证订单状态是否允许取消，状态>=3的订单不可取消
        if (order.getStatus() >= 3) {
            throw new BusinessException(400, "该订单不可取消");
        }
        
        // 更新订单状态为已取消（状态4）
        orderMapper.updateStatus(id, 4);
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
        // 验证订单是否存在
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        // 验证用户权限，确保只能取消自己的订单
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权取消该订单");
        }
        
        // 验证订单状态是否允许取消
        if (order.getStatus() >= 3) {
            throw new BusinessException(400, "该订单不可取消");
        }
        
        // 如果订单已发货（状态2），取消时需要扣除5%违约金
        if (order.getStatus() == 2) {
            double penalty = order.getTotalAmount() * 0.05;
            orderMapper.updateUserMoney(userId, -penalty);
        }
        
        // 更新订单状态为已取消（状态4）
        orderMapper.updateStatus(id, 4);
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
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权确认该订单");
        }
        
        if (order.getStatus() != 2) {
            throw new BusinessException(400, "只有已发货的订单才能确认收货");
        }
        
        orderMapper.updateReceiptImage(id, receiptImageUrl);
        
        orderMapper.updateStatusToFinished(id, 3);
        
        orderMapper.updateUserMoney(userId, -order.getTotalAmount());
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
        return orderMapper.getTopSellingProducts(limit);
    }

}
