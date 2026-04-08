package com.xingyou.service.impl;

import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import com.xingyou.exception.BusinessException;
import com.xingyou.mapper.OrderMapper;
import com.xingyou.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private OrderMapper orderMapper;
    
    @Override
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException(400, "订单商品不能为空");
        }
        
        if (order.getUserId() == null) {
            throw new BusinessException(400, "用户 ID 不能为空");
        }
        
        double totalAmount = 0.0;
        for (OrderItem item : orderItems) {
            double itemAmount = item.getProductPrice() * item.getQuantity();
            totalAmount += itemAmount;
        }
        order.setTotalAmount(totalAmount);
        
        User existingUser = orderMapper.findUserByUserId(order.getUserId());
        if (existingUser == null) {
            throw new BusinessException(404, "用户不存在");
        }
        
        if (existingUser.getMoney() == null || existingUser.getMoney() < totalAmount) {
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
        
        return order;
    }
    
    @Override
    public List<Order> findAll() {
        return orderMapper.findAll();
    }
    
    @Override
    public Order findById(Integer id) {
        return orderMapper.findById(id);
    }
    
    @Override
    public List<Order> findByUserId(String userId) {
        return orderMapper.findByUserId(userId);
    }
    
    @Override
    public List<OrderItem> findOrderItemsByOrderId(Integer orderId) {
        return orderMapper.findOrderItemsByOrderId(orderId);
    }
    
    @Override
    public void updateStatus(Integer id, Integer status) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        if (status == 2 && order.getStatus() != 2) {
            List<OrderItem> items = orderMapper.findOrderItemsByOrderId(id);
            if (items != null && !items.isEmpty()) {
                List<String> insufficientProducts = new java.util.ArrayList<>();
                
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
                
                if (!insufficientProducts.isEmpty()) {
                    String errorMsg = "以下商品库存不足，无法发货：" + String.join("、", insufficientProducts);
                    throw new BusinessException(400, errorMsg);
                }
                
                for (OrderItem item : items) {
                    orderMapper.updateProductStock(item.getProductId(), item.getQuantity());
                }
            }
        }
        
        orderMapper.updateStatus(id, status);
    }
    
    @Override
    public void cancelOrderByStaff(Integer id) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        if (order.getStatus() >= 3) {
            throw new BusinessException(400, "该订单不可取消");
        }
        
        orderMapper.updateStatus(id, 4);
    }
    
    @Override
    public void cancelOrderByUser(Integer id, String userId) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }
        
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权取消该订单");
        }
        
        if (order.getStatus() >= 3) {
            throw new BusinessException(400, "该订单不可取消");
        }
        
        if (order.getStatus() == 2) {
            double penalty = order.getTotalAmount() * 0.05;
            orderMapper.updateUserMoney(userId, -penalty);
        }
        
        orderMapper.updateStatus(id, 4);
    }
    
    @Override
    public void confirmReceipt(Integer id, String userId) {
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
        
        orderMapper.updateStatus(id, 3);
        
        orderMapper.updateUserMoney(userId, -order.getTotalAmount());
    }
    
    private String generateOrderNum() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf(new Random().nextInt(9000) + 1000);
        return "O" + timestamp + random;
    }
}
