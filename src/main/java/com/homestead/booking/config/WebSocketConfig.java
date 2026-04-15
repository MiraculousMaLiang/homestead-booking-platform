package com.homestead.booking.config;

import com.homestead.booking.utils.ChatWebSocketHandler;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig{

//    @Resource
//    private ChatWebSocketHandler myChatWebSocketHandler; // 自定义的消息处理器
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//
//
//        // 注册WebSocket处理器，指定前端连接的路径（如/ws/chat），允许跨域
//        registry.addHandler(myChatWebSocketHandler, "/ws/chat")
//                .setAllowedOrigins("*"); // 生产环境替换为具体前端域名
@Bean
public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
    }
}
