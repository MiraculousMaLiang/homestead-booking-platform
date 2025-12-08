package com.homestead.booking.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.ReviewDTO;
import com.homestead.booking.service.ReviewService;
import com.homestead.booking.vo.ReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 评价Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Tag(name = "评价管理", description = "房源评价、回复、点赞等接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Operation(summary = "提交评价", description = "用户对已完成订单提交评价")
    @PostMapping("/submit/{orderId}")
    public Result<Void> submitReview(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId,
            @Valid @RequestBody ReviewDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        reviewService.submitReview(userId, orderId, dto);
        return Result.success();
    }

    @Operation(summary = "房东回复评价", description = "房东对评价进行回复")
    @PostMapping("/reply/{reviewId}")
    public Result<Void> replyReview(
            @Parameter(description = "评价ID", required = true)
            @PathVariable Long reviewId,
            @Parameter(description = "回复内容", required = true)
            @NotBlank(message = "回复内容不能为空")
            @RequestParam String replyContent) {
        Long landlordId = StpUtil.getLoginIdAsLong();
        reviewService.replyReview(landlordId, reviewId, replyContent);
        return Result.success();
    }

    @Operation(summary = "点赞评价", description = "用户点赞评价")
    @PostMapping("/like/{reviewId}")
    public Result<Void> likeReview(
            @Parameter(description = "评价ID", required = true)
            @PathVariable Long reviewId) {
        reviewService.likeReview(reviewId);
        return Result.success();
    }

    @Operation(summary = "获取房源评价列表", description = "分页查询指定房源的评价列表")
    @GetMapping("/list/{homesteadId}")
    public Result<PageResult<ReviewVO>> getHomesteadReviewList(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long homesteadId,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        PageResult<ReviewVO> pageResult = reviewService.getHomesteadReviewList(homesteadId, pageNum, pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取用户评价列表", description = "分页查询指定用户的评价列表")
    @GetMapping("/user/{userId}")
    public Result<PageResult<ReviewVO>> getUserReviewList(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        PageResult<ReviewVO> pageResult = reviewService.getUserReviewList(userId, pageNum, pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取评价详情", description = "查看指定评价的详细信息")
    @GetMapping("/detail/{reviewId}")
    public Result<ReviewVO> getReviewDetail(
            @Parameter(description = "评价ID", required = true)
            @PathVariable Long reviewId) {
        ReviewVO reviewVO = reviewService.getReviewDetail(reviewId);
        return Result.success(reviewVO);
    }

}
