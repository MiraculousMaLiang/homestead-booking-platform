package com.homestead.booking.service;

import com.homestead.booking.dto.MsgDTO;
import com.homestead.booking.entity.ChatMessage;
import com.homestead.booking.entity.ChatSession;

import java.util.List;

public interface ChatService {
    void sendMsg(MsgDTO msgDto);

    List<ChatSession> getSessionList();

    List<ChatMessage> getHistory(Long currentUserId, Long targetUserId);

    void clearUnread(Long currentUserId, Long targetUserId);
}
