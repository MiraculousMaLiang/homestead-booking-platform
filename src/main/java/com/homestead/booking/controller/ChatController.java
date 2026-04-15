package com.homestead.booking.controller;

import com.homestead.booking.common.Result;
import com.homestead.booking.entity.ChatMessage;
import com.homestead.booking.entity.ChatSession;
import com.homestead.booking.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    /**
     * 获取会话列表
     * 按时间倒序排列
     */
    @GetMapping("/list")
    public Result<List<ChatSession>> getSessionList() {
        // 实际项目中 userId 应该从 Token/Session 中获取，不要前端传
        return Result.success(chatService.getSessionList());
    }

    /**
     * 获取聊天记录
     */
    @GetMapping("/history")
    public Result<List<ChatMessage>> getHistory(@RequestParam Long currentUserId, @RequestParam Long targetUserId) {
        return Result.success(chatService.getHistory(currentUserId, targetUserId));
    }

    /**
     * 标记为已读
     */
    @PostMapping("/read")
    public Result<String> markAsRead(@RequestParam Long currentUserId, @RequestParam Long targetUserId) {
        chatService.clearUnread(currentUserId, targetUserId);
        return Result.success();
    }
}
