package com.homestead.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 消息表实体
 * 对应数据库：chat_message
 */
@Data
@Accessors(chain = true)
@TableName("chat_message")
public class ChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送者 ID
     */
    private Long fromUserId;

    /**
     * 接收者 ID
     */
    private Long toUserId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型：1-文本，2-图片，3-语音，4-视频
     */
    private Integer messageType;

    /**
     * 是否已读：0-未读，1-已读
     * (在双写模式的会话表中已有 unread_count，这个字段主要用于历史记录分析或群聊已读逻辑)
     */
    private Integer isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
