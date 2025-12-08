package com.homestead.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评价DTO
 *
 * @author homestead
 * @since 2025-12-03
 */
@Data
@Schema(description = "评价信息")
public class ReviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总体评分(1-5)", required = true)
    @NotNull(message = "总体评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer ratingOverall;

    @Schema(description = "位置评分(1-5)", required = true)
    @NotNull(message = "位置评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer ratingLocation;

    @Schema(description = "卫生评分(1-5)", required = true)
    @NotNull(message = "卫生评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer ratingCleanliness;

    @Schema(description = "设施评分(1-5)", required = true)
    @NotNull(message = "设施评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer ratingFacility;

    @Schema(description = "服务评分(1-5)", required = true)
    @NotNull(message = "服务评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer ratingService;

    @Schema(description = "性价比评分(1-5)", required = true)
    @NotNull(message = "性价比评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer ratingValue;

    @Schema(description = "评价内容", required = true)
    @NotBlank(message = "评价内容不能为空")
    private String content;

    @Schema(description = "评价图片列表")
    private List<String> images;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "是否匿名: 0-否 1-是", example = "0")
    private Integer isAnonymous;

}
