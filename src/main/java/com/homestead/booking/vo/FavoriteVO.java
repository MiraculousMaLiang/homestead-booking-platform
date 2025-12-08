package com.homestead.booking.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收藏VO
 *
 * @author homestead
 * @since 2025-12-03
 */
@Data
@Schema(description = "收藏信息")
public class FavoriteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "收藏ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "房源ID")
    private Long homesteadId;

    @Schema(description = "房源名称")
    private String homesteadName;

    @Schema(description = "房源封面图")
    private String homesteadCover;

    @Schema(description = "房源地址")
    private String homesteadAddress;

    @Schema(description = "房源价格")
    private BigDecimal homesteadPrice;

    @Schema(description = "房源状态: 0-下架 1-上架")
    private Integer homesteadStatus;

    @Schema(description = "收藏时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
