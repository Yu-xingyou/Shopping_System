package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.shopping.Notification;
import com.xingyou.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知控制器
 * 提供通知相关的REST API接口，包括查询通知、标记已读等功能
 */
@RequestMapping("/notification")
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 查询用户未读通知列表
     *
     * @param userId 用户ID
     * @return Result 返回未读通知列表
     */
    @GetMapping("/unread")
    public Result getUnread(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        List<Notification> notifications = notificationService.findUnreadByUserId(userId);
        return Result.success(notifications);
    }

    /**
     * 查询用户所有通知列表
     *
     * @param userId 用户ID
     * @return Result 返回所有通知列表
     */
    @GetMapping("/all")
    public Result getAll(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        List<Notification> notifications = notificationService.findAllByUserId(userId);
        return Result.success(notifications);
    }

    /**
     * 标记单条通知为已读
     *
     * @param id 通知ID
     * @param userId 用户ID
     * @return Result 返回操作结果
     */
    @PostMapping("/{id}/read")
    public Result markAsRead(@PathVariable Integer id, @RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        try {
            notificationService.markAsRead(id, userId);
            return Result.success("标记已读成功");
        } catch (Exception e) {
            return Result.error(500, "标记已读失败：" + e.getMessage());
        }
    }

    /**
     * 标记用户所有未读通知为已读
     *
     * @param userId 用户ID
     * @return Result 返回操作结果
     */
    @PostMapping("/read-all")
    public Result markAllAsRead(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        try {
            notificationService.markAllAsRead(userId);
            return Result.success("全部标记已读成功");
        } catch (Exception e) {
            return Result.error(500, "标记已读失败：" + e.getMessage());
        }
    }

    /**
     * 查询用户未读通知数量
     *
     * @param userId 用户ID
     * @return Result 返回未读通知数量
     */
    @GetMapping("/unread-count")
    public Result getUnreadCount(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        int count = notificationService.countUnread(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("count", count);

        return Result.success(result);
    }
}

