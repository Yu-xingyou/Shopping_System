package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {
    
    @Resource
    private OrderService orderService;
    
    @PostMapping("/create")
    public Result create(@RequestBody Map<String, Object> request) {
        Order order = parseOrder(request);
        List<OrderItem> orderItems = parseOrderItems(request);
        
        Order createdOrder = orderService.createOrder(order, orderItems);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", createdOrder.getId());
        result.put("orderNum", createdOrder.getOrderNum());
        result.put("totalAmount", createdOrder.getTotalAmount());
        
        return Result.success(result);
    }
    
    @GetMapping("/list")
    public Result list(@RequestParam String userId) {
        List<Order> orders = orderService.findByUserId(userId);
        return Result.success(orders);
    }
    
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Order order = orderService.findById(id);
        if (order == null) {
            return Result.error(404, "订单不存在");
        }
        return Result.success(order);
    }
    
    @PutMapping("/{id}/status")
    public Result updateStatus(
            @PathVariable Integer id, 
            @RequestParam Integer status,
            @RequestParam Integer staffId) {
        
        if (staffId == null) {
            return Result.error(403, "缺少员工身份信息");
        }
        
        Order order = orderService.findById(id);
        if (order == null) {
            return Result.error(404, "订单不存在");
        }
        
        if (status < 0 || status > 6) {
            return Result.error(400, "无效的订单状态");
        }
        
        try {
            orderService.updateStatus(id, status);
            return Result.success("订单状态更新成功");
        } catch (Exception e) {
            return Result.error(500, "更新订单状态失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/{id}/cancel")
    public Result cancel(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer staffId,
            @RequestParam(required = false) String userId) {
        
        if (staffId != null) {
            try {
                orderService.cancelOrderByStaff(id);
                return Result.success("订单已取消");
            } catch (BusinessException e) {
                return Result.error(e.getCode(), e.getMessage());
            } catch (Exception e) {
                return Result.error(500, "取消订单失败：" + e.getMessage());
            }
        } else if (userId != null) {
            try {
                orderService.cancelOrderByUser(id, userId);
                return Result.success("订单已取消");
            } catch (BusinessException e) {
                return Result.error(e.getCode(), e.getMessage());
            } catch (Exception e) {
                return Result.error(500, "取消订单失败：" + e.getMessage());
            }
        } else {
            return Result.error(400, "缺少取消身份标识");
        }
    }

    @GetMapping("/all")
    public Result all() {
        List<Order> orders = orderService.findAll();
        return Result.success(orders);
    }
    
    @GetMapping("/{id}/items")
    public Result getItems(@PathVariable Integer id) {
        List<OrderItem> items = orderService.findOrderItemsByOrderId(id);
        return Result.success(items);
    }
    
    private Order parseOrder(Map<String, Object> request) {
        Order order = new Order();
        
        if (request.get("userId") != null) {
            order.setUserId((String) request.get("userId"));
        }
        if (request.get("totalAmount") != null) {
            order.setTotalAmount(((Number) request.get("totalAmount")).doubleValue());
        }
        if (request.get("receiverName") != null) {
            order.setReceiverName((String) request.get("receiverName"));
        }
        if (request.get("receiverPhone") != null) {
            order.setReceiverPhone((String) request.get("receiverPhone"));
        }
        if (request.get("receiverAddress") != null) {
            order.setReceiverAddress((String) request.get("receiverAddress"));
        }
        if (request.get("remark") != null) {
            order.setRemark((String) request.get("remark"));
        }
        
        return order;
    }
    
    @SuppressWarnings("unchecked")
    private List<OrderItem> parseOrderItems(Map<String, Object> request) {
        List<Map<String, Object>> itemsMap = (List<Map<String, Object>>) request.get("items");
        
        if (itemsMap == null || itemsMap.isEmpty()) {
            throw new IllegalArgumentException("订单商品不能为空");
        }
        
        List<OrderItem> orderItems = new java.util.ArrayList<>();
        for (Map<String, Object> itemMap : itemsMap) {
            OrderItem item = new OrderItem();
            
            if (itemMap.get("productId") != null) {
                item.setProductId(((Number) itemMap.get("productId")).intValue());
            }
            if (itemMap.get("productName") != null) {
                item.setProductName((String) itemMap.get("productName"));
            }
            if (itemMap.get("productPrice") != null) {
                item.setProductPrice(((Number) itemMap.get("productPrice")).doubleValue());
            }
            if (itemMap.get("quantity") != null) {
                item.setQuantity(((Number) itemMap.get("quantity")).intValue());
            }
            if (itemMap.get("productImage") != null) {
                item.setProductImage((String) itemMap.get("productImage"));
            }
            
            orderItems.add(item);
        }
        
        return orderItems;
    }
}
