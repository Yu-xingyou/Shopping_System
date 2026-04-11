package com.xingyou.controller;

import com.xingyou.common.Result;
import com.xingyou.entity.shopping.Notification;
import com.xingyou.service.NotificationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/notification")
@RestController
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping("/unread")
    public Result getUnread(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        List<Notification> notifications = notificationService.findUnreadByUserId(userId);
        return Result.success(notifications);
    }

    @GetMapping("/all")
    public Result getAll(@RequestParam String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error(400, "用户ID不能为空");
        }

        List<Notification> notifications = notificationService.findAllByUserId(userId);
        return Result.success(notifications);
    }

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

