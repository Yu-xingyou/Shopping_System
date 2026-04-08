package com.xingyou.mapper;

import com.xingyou.entity.people.User;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    
    void insert(Order order);
    
    void insertOrderItem(OrderItem orderItem);
    
    List<Order> findAll();
    
    Order findById(@Param("id") Integer id);
    
    List<Order> findByUserId(@Param("userId") String userId);
    
    List<OrderItem> findOrderItemsByOrderId(@Param("orderId") Integer orderId);
    
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    void updateUserMoney(@Param("userId") String userId, @Param("money") Double money);
    
    User findUserByUserId(@Param("userId") String userId);
    
    void updateProductStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
    
    com.xingyou.entity.shopping.Product findProductById(@Param("id") Integer id);
}
