package com.homestead.booking.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.homestead.booking.dto.MsgDTO;
import com.homestead.booking.entity.ChatMessage;
import com.homestead.booking.entity.ChatSession;
import com.homestead.booking.mapper.ChatMessageMapper;
import com.homestead.booking.mapper.ChatSessionMapper;
import com.homestead.booking.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    // 注入你的 Mapper/Dao
    @Autowired
     private ChatSessionMapper sessionMapper;
    @Autowired
     private ChatMessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(MsgDTO msgDto) {
        Date now = new Date();

        // -------------------------------------------------------
        // 1. 保存消息主体 (只存一份)
        // -------------------------------------------------------
        ChatMessage message = new ChatMessage();
        message.setFromUserId(msgDto.getFromUserId());
        message.setToUserId(msgDto.getToUserId());
        message.setContent(msgDto.getContent());
        message.setCreateTime(now);
        messageMapper.insert(message);


        // -------------------------------------------------------
        // 2. 双写会话 (核心逻辑)
        // -------------------------------------------------------

        // 生成统一的 session_id (小ID_大ID)，用于关联这两条数据
        long minId = Math.min(msgDto.getFromUserId(), msgDto.getToUserId());
        long maxId = Math.max(msgDto.getFromUserId(), msgDto.getToUserId());
        String sessionId = minId + "_" + maxId;

        // A. 更新【发送者】的会话 (Owner = 我, Target = 对方)
        // 发送者看这条会话：未读数应该是 0 (或者保持不变)，消息内容更新
        upsertSession(msgDto.getFromUserId(), msgDto.getToUserId(), sessionId, msgDto.getContent(), now, false,1);

        // B. 更新【接收者】的会话 (Owner = 对方, Target = 我)
        // 接收者看这条会话：未读数 + 1，消息内容更新
        upsertSession(msgDto.getToUserId(), msgDto.getFromUserId(), sessionId, msgDto.getContent(), now, true,1);
    }

    @Override
    public List<ChatSession> getSessionList() {
        Integer loginId = StpUtil.getLoginIdAsInt();

        // 查询属于该用户的会话，按时间倒序排列
        LambdaQueryWrapper<ChatSession> query = new LambdaQueryWrapper<>();
        query.eq(ChatSession::getOwnerId, loginId)
                .orderByDesc(ChatSession::getLastMsgTime); // 这里的排序很重要，最新的在上面

        return sessionMapper.selectList(query);
    }

    @Override
    public List<ChatMessage> getHistory(Long userId, Long targetId) {
        // 查询条件：(from = 我 AND to = 他) OR (from = 他 AND to = 我)
        LambdaQueryWrapper<ChatMessage> query = new LambdaQueryWrapper<>();
        query.and(wrapper ->
                wrapper.eq(ChatMessage::getFromUserId, userId).eq(ChatMessage::getToUserId, targetId)
        ).or(wrapper ->
                wrapper.eq(ChatMessage::getFromUserId, targetId).eq(ChatMessage::getToUserId, userId)
        );

        query.orderByAsc(ChatMessage::getCreateTime); // 按时间正序，早说的话在上面

        // 实际生产中通常需要分页 (Page)，这里先查全部
        return messageMapper.selectList(query);
    }

    @Override
    public void clearUnread(Long currentUserId, Long targetUserId) {
        LambdaUpdateWrapper<ChatSession> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ChatSession::getOwnerId, currentUserId)
                .eq(ChatSession::getToUserId, targetUserId)
                .set(ChatSession::getUnreadCount, 0);
        sessionMapper.update(updateWrapper);

    }

    /**
     * 更新或插入会话 (Upsert)
     * @param ownerId 会话持有者
     * @param targetId 聊天对象
     * @param sessionId 会话唯一标识
     * @param content 最新消息
     * @param time 时间
     * @param incrementUnread 是否增加未读数 (接收者为 true, 发送者为 false)
     */
    private void upsertSession(Long ownerId, Long targetId, String sessionId, String content, Date time, boolean incrementUnread, int sessionType) {
        // 1. 查询该用户是否存在该会话
        // select * from chat_session where owner_id = ? and to_user_id = ?
        LambdaQueryWrapper<ChatSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatSession::getOwnerId, ownerId).eq(ChatSession::getToUserId, targetId);
        ChatSession session = sessionMapper.selectOne(queryWrapper);

        if (session == null) {
            // --- 插入新会话 ---
            session = new ChatSession();
            session.setSessionType(sessionType);
            session.setOwnerId(ownerId);
            session.setToUserId(targetId); // 对方ID
            session.setSessionId(sessionId);
            session.setLastMsg(content);
            session.setLastMsgTime(time);
            session.setCreateTime(time);
            // 如果是新会话，且我是接收者，那就是 1 条未读
            session.setUnreadCount(incrementUnread ? 1 : 0);

            sessionMapper.insert(session);
        } else {
            // --- 更新旧会话 ---
            session.setLastMsg(content);
            session.setLastMsgTime(time);

            if (incrementUnread) {
                // 接收者：未读数 + 1
                session.setUnreadCount(session.getUnreadCount() + 1);
            } else {
                // 发送者：未读数不变，或者重置为0？
                // 通常发送消息时，意味着我已经进入了聊天框，理论上可以将未读数归零，
                // 但为了严谨，这里通常不做处理，或者由前端调用 read 接口来清零。
                // 这里暂且保持不变。
            }

            // 确保如果用户删除了会话(软删除 is_delete=1)，新消息会让它重新浮现
            session.setIsDelete(0);

            sessionMapper.updateById(session);
        }
    }
}
