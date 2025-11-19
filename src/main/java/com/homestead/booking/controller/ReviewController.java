package com.homestead.booking.controller;

import com.homestead.booking.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 评价Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {

    /**
     * 提交评价
     */
    @PostMapping("/submit/{orderId}")
    public Result<Void> submitReview(HttpServletRequest request,
                                     @PathVariable Long orderId) {
        Long userId = (Long) request.getAttribute("userId");
        // TODO: 实现评价提交逻辑
        return Result.success();
    }

    /**
     * 获取房源评价列表
     */
    @GetMapping("/list/{homesteadId}")
    public Result<Void> getReviewList(@PathVariable Long homesteadId,
                                      @RequestParam(defaultValue = "1") Long pageNum,
                                      @RequestParam(defaultValue = "10") Long pageSize) {
        // TODO: 实现评价列表查询逻辑
        return Result.success();
    }

}
