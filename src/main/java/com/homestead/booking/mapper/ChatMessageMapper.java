package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.ChatMessage;
import com.homestead.booking.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天内容Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

}
