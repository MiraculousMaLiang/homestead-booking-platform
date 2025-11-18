package com.homestead.booking.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 评价实体类
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
@TableName("review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 房源ID
     */
    private Long homesteadId;

    /**
     * 总体评分(1-5)
     */
    private Integer ratingOverall;

    /**
     * 位置评分(1-5)
     */
    private Integer ratingLocation;

    /**
     * 卫生评分(1-5)
     */
    private Integer ratingCleanliness;

    /**
     * 设施评分(1-5)
     */
    private Integer ratingFacility;

    /**
     * 服务评分(1-5)
     */
    private Integer ratingService;

    /**
     * 性价比评分(1-5)
     */
    private Integer ratingValue;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片(多张用逗号分隔)
     */
    private String images;

    /**
     * 标签(多个用逗号分隔)
     */
    private String tags;

    /**
     * 是否匿名: 0-否 1-是
     */
    private Integer isAnonymous;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 房东回复
     */
    private String replyContent;

    /**
     * 回复时间
     */
    private LocalDateTime replyTime;

    /**
     * 状态: 0-待审核 1-已通过 2-已拒绝
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除: 0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;

}
