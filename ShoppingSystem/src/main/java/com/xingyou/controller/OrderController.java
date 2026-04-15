package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.shopping.Order;
import com.xingyou.entity.shopping.OrderItem;
import com.xingyou.exception.BusinessException;
import com.xingyou.service.OrderService;
import com.xingyou.util.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    
    /**
     * 创建新订单
     * 
     * @param request 请求体，包含订单信息和订单明细的Map对象
     * @return Result 返回创建成功的订单信息，包含订单ID、订单号和总金额
     */
    @PostMapping("/create")
    public Result create(@RequestBody Map<String, Object> request) {
        // 解析订单基本信息
        Order order = parseOrder(request);
        // 解析订单明细列表
        List<OrderItem> orderItems = parseOrderItems(request);
        
        // 调用服务层创建订单
        Order createdOrder = orderService.createOrder(order, orderItems);
        
        // 构建返回结果，包含订单ID、订单号和总金额
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", createdOrder.getId());
        result.put("orderNum", createdOrder.getOrderNum());
        result.put("totalAmount", createdOrder.getTotalAmount());
        
        return Result.success(result);
    }
    
    /**
     * 根据用户ID查询订单列表
     * 
     * @param userId 用户ID
     * @return Result 返回该用户的订单列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam String userId) {
        List<Order> orders = orderService.findByUserId(userId);
        return Result.success(orders);
    }
    
    /**
     * 根据订单ID查询订单详情
     * 
     * @param id 订单ID
     * @return Result 返回订单详情信息，如果订单不存在则返回404错误
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Order order = orderService.findById(id);
        if (order == null) {
            return Result.error(404, "订单不存在");
        }
        return Result.success(order);
    }
    
    /**
     * 更新订单状态(仅员工可操作)
     * 
     * @param id 订单ID
     * @param status 新的订单状态(0-6)
     * @param staffId 员工ID
     * @return Result 返回更新结果,成功返回"订单状态更新成功",失败返回错误信息
     */
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
    
    /**
     * 取消订单(支持员工和用户两种角色)
     * 
     * @param id 订单ID
     * @param staffId 员工ID(可选,员工取消时传入)
     * @param userId 用户ID(可选,用户取消时传入)
     * @return Result 返回取消结果,成功返回"订单已取消",失败返回错误信息
     */
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

    /**
     * 确认收货接口（需上传小票照片）
     * 
     * 处理用户确认收货的业务逻辑，验证用户ID后调用订单服务完成收货操作。
     * 成功确认后订单状态将更新为已完成。
     *
     * @param id 订单ID，用于指定需要确认收货的订单
     * @param userId 用户ID，用于验证操作用户的合法性，不能为空或空字符串
     * @param receiptImage 小票图片文件，必填参数
     * @return Result 返回操作结果，包含以下情况：
     *         - 参数校验失败时返回400错误码和提示信息
     *         - 业务异常时返回对应的错误码和异常信息
     *         - 系统异常时返回500错误码和异常信息
     *         - 成功时返回成功标识和"确认收货成功，订单已完成"提示
     */
    @PostMapping("/{id}/confirm")
    public Result confirmReceipt(
            @PathVariable Integer id,
            @RequestParam String userId,
            @RequestParam("receiptImage") MultipartFile receiptImage) {
        
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户 ID 不能为空");
        }
        
        if (receiptImage == null || receiptImage.isEmpty()) {
            return Result.error(400, "请上传小票照片");
        }
        
        try {
            String imageUrl = aliyunOSSOperator.upload(receiptImage.getBytes(), receiptImage.getOriginalFilename());
            
            orderService.confirmReceipt(id, userId, imageUrl);
            return Result.success("确认收货成功，订单已完成");
        } catch (BusinessException e) {
            return Result.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "确认收货失败：" + e.getMessage());
        }
    }

    /**
     * 查询所有订单接口
     * 
     * 获取系统中所有的订单列表信息。
     *
     * @return Result 返回操作结果，包含以下情况：
     *         - 成功时返回所有订单的列表数据
     */
    @GetMapping("/all")
    public Result all() {
        List<Order> orders = orderService.findAll();
        return Result.success(orders);
    }
    
    /**
     * 查询订单明细接口
     * 
     * 根据订单ID获取该订单包含的所有商品明细信息。
     *
     * @param id 订单ID，用于指定需要查询明细的订单
     * @return Result 返回操作结果，包含以下情况：
     *         - 成功时返回订单明细列表数据
     */
    @GetMapping("/{id}/items")
    public Result getItems(@PathVariable Integer id) {
        List<OrderItem> items = orderService.findOrderItemsByOrderId(id);
        return Result.success(items);
    }
    
    /**
     * 解析订单数据
     * 
     * 从请求Map中提取订单相关字段并构建Order对象，只处理非空字段。
     *
     * @param request 包含订单信息的请求参数Map，可能包含以下键：
     *                - userId: 用户ID
     *                - totalAmount: 订单总金额
     *                - receiverName: 收货人姓名
     *                - receiverPhone: 收货人电话
     *                - receiverAddress: 收货地址
     *                - remark: 订单备注
     * @return Order 解析后的订单对象，包含所有提供的非空字段
     */
    private Order parseOrder(Map<String, Object> request) {
        Order order = new Order();
        
        // 提取并设置订单的各项属性，仅处理非空值
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

    /**
     * 解析订单商品明细列表
     * 
     * 从请求Map中提取订单商品列表信息，验证非空后遍历构建OrderItem对象集合。
     * 每个商品项包含产品ID、名称、价格、数量和图片等信息。
     *
     * @param request 包含订单商品信息的请求参数Map，其中"items"键对应的值为商品列表
     * @return List<OrderItem> 解析后的订单商品明细列表
     * @throws IllegalArgumentException 当商品列表为空或null时抛出异常
     */
    @SuppressWarnings("unchecked")
    private List<OrderItem> parseOrderItems(Map<String, Object> request) {
        List<Map<String, Object>> itemsMap = (List<Map<String, Object>>) request.get("items");
        
        // 验证订单商品列表不能为空
        if (itemsMap == null || itemsMap.isEmpty()) {
            throw new IllegalArgumentException("订单商品不能为空");
        }
        
        List<OrderItem> orderItems = new java.util.ArrayList<>();
        
        // 遍历商品列表，逐个解析并构建OrderItem对象
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

    /**
     * 查询当月热销产品TOP N
     * 
     * 统计当月已完成订单中各商品的销售数量，按销量降序排列。
     *
     * @param limit 可选参数，返回的商品数量限制，默认为10
     * @return Result 返回热销产品列表，每个产品包含productId、productName和totalQuantity
     */
    @GetMapping("/top-selling")
    public Result getTopSellingProducts(@RequestParam(required = false, defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> products = orderService.getTopSellingProducts(limit);
            return Result.success(products);
        } catch (Exception e) {
            return Result.error(500, "查询热销产品失败：" + e.getMessage());
        }
    }

}
