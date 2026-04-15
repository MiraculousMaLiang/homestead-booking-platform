package com.homestead.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

/**
 * WebSocket 消息传输对象
 * 用于接收前端发送的消息，或推送到前端
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgDTO implements Serializable {

    /**
     * 消息ID (发送时为空，入库后回传给前端用于确认)
     */
    private Long id;

    /**
     * 发送者ID (前端发送时不需要传，后端从 Session 获取)
     */
    private Long fromUserId;

    /**
     * 接收者ID (必填)
     */
    private Long toUserId;

    /**
     * 消息内容 (必填)
     */
    private String content;

    /**
     * 消息类型：1-文本，2-图片... (默认1)
     */
    private Integer messageType = 1;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // 如果需要扩展，比如发送者头像、昵称，可以在这里加字段
    // private String fromUserAvatar;
    // private String fromUserNickname;
}
