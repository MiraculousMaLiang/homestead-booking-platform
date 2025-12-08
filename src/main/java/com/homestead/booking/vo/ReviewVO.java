package com.homestead.booking.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价VO
 *
 * @author homestead
 * @since 2025-12-03
 */
@Data
@Schema(description = "评价信息")
public class ReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "评价ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "房源ID")
    private Long homesteadId;

    @Schema(description = "房源名称")
    private String homesteadName;

    @Schema(description = "总体评分(1-5)")
    private Integer ratingOverall;

    @Schema(description = "位置评分(1-5)")
    private Integer ratingLocation;

    @Schema(description = "卫生评分(1-5)")
    private Integer ratingCleanliness;

    @Schema(description = "设施评分(1-5)")
    private Integer ratingFacility;

    @Schema(description = "服务评分(1-5)")
    private Integer ratingService;

    @Schema(description = "性价比评分(1-5)")
    private Integer ratingValue;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "评价图片列表")
    private List<String> images;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "是否匿名: 0-否 1-是")
    private Integer isAnonymous;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "房东回复")
    private String replyContent;

    @Schema(description = "回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime replyTime;

    @Schema(description = "状态: 0-待审核 1-已通过 2-已拒绝")
    private Integer status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
