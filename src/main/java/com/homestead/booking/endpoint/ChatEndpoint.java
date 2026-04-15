package com.homestead.booking.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homestead.booking.dto.MsgDTO;
import com.homestead.booking.service.ChatService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

// 路径中的 {userId} 用于区分是谁连上来了
@ServerEndpoint("/ws/chat/{userId}")
@Component
public class ChatEndpoint {

    // 用来存储所有在线用户的容器：Key=UserId, Value=Session
    private static final ConcurrentHashMap<Long, Session> onlineUsers = new ConcurrentHashMap<>();

    // JSON转换工具
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 这里需要手动注入你的 Service，因为 @ServerEndpoint 是多例的
     private static ChatService chatService;
     @Autowired
     public void setChatService(ChatService chatService) {
         ChatEndpoint.chatService = chatService;
     }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        onlineUsers.put(userId, session);
        System.out.println("用户 " + userId + " 已上线");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") Long userId) {
        onlineUsers.remove(userId);
        System.out.println("用户 " + userId + " 已下线");
    }

    /**
     * 收到客户端消息后调用的方法
     * message 应该是前端发来的 JSON 字符串
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // 1. 解析消息 (假设前端传的是 JSON)
            // 你需要定义一个 MsgDTO 类来接收参数，包含 content, toUserId, type 等
            MsgDTO msgDto = objectMapper.readValue(message, MsgDTO.class);

            // 补全发送者ID
            Long fromUserId = Long.parseLong(session.getPathParameters().get("userId"));
            msgDto.setFromUserId(fromUserId);
            msgDto.setCreateTime(new Date());

            // -------------------------------------------------------
            // 2. 数据库业务处理 (核心) - 这里调用你的 Service 方法
            // -------------------------------------------------------
            chatService.sendMsg(msgDto);

            // 伪代码逻辑：
            // A. 保存到 chat_message 表
            // B. 查询或创建 chat_session 表数据
            //    session_id 生成规则建议：如果是单聊，将两个ID排序拼接，例如 "1001_1002"，保证唯一性
            // C. 更新 chat_session 的 last_msg, update_time 等字段

            // -------------------------------------------------------
            // 3. 消息转发 (Push)
            // -------------------------------------------------------
            Session receiverSession = onlineUsers.get(msgDto.getToUserId());

            if (receiverSession != null && receiverSession.isOpen()) {
                // 如果接收者在线，直接发送 JSON 字符串
                String jsonStr = objectMapper.writeValueAsString(msgDto);
                receiverSession.getBasicRemote().sendText(jsonStr);

                // 可选：标记消息为已读（如果即时送达算已读的话）
            } else {
                // 接收者不在线
                // 数据库中该消息的 is_read 默认为 0 (未读)
                // 更新 chat_session 中的 unread_count + 1
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
