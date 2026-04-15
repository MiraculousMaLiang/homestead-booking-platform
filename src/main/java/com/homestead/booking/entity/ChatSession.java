package com.homestead.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 会话表实体
 * 对应数据库：chat_session
 * 策略：双写模式 (A和B聊天，数据库会有两行记录，ownerId分别是A和B)
 */
@Data
@Accessors(chain = true) // 支持链式调用 session.setX().setY()
@TableName("chat_session")
public class ChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话唯一标识
     * 生成规则：minId_maxId (例如 "1001_1002")
     * 作用：用于快速关联 A 和 B 的共同会话ID
     */
    private String sessionId;

    /**
     * 会话持有者 ID (当前这条记录属于谁)
     * 核心字段：双写模式新增
     */
    private Long ownerId;

    /**
     * 聊天对象 ID (对方是谁)
     * 核心字段：双写模式新增，替代原有的 relate_id 或 user_ids 解析
     */
    private Long toUserId;

    /**
     * 会话类型：1-单聊，2-群聊
     */
    private Integer sessionType;

    /**
     * 最后一条消息内容 (用于列表展示)
     */
    private String lastMsg;

    /**
     * 最后一条消息时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastMsgTime;

    /**
     * 未读消息数 (只属于 ownerId 的未读数)
     */
    private Integer unreadCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 软删除：0-未删，1-已删
     * (比如 A 删除了会话，isDelete=1，当 B 再发消息来时，重置为 0)
     */
    private Integer isDelete;
}
