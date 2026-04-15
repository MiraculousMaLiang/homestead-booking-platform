package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天会话Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {

}
