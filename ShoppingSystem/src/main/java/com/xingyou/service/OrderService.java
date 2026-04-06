package com.xingyou.service;

import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    
    Order createOrder(Order order, List<OrderItem> orderItems);
    
    List<Order> findAll();
    
    Order findById(Integer id);
    
    List<Order> findByUserId(String userId);
    
    List<OrderItem> findOrderItemsByOrderId(Integer orderId);
    
    void updateStatus(Integer id, Integer status);
    
    void cancelOrderByStaff(Integer id);
    
    void cancelOrderByUser(Integer id, String userId);
    
    void confirmReceipt(Integer id, String userId);
}
