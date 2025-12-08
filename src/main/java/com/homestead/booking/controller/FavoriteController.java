package com.homestead.booking.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.service.FavoriteService;
import com.homestead.booking.vo.FavoriteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Tag(name = "收藏管理", description = "房源收藏、取消收藏等接口")
@Slf4j
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "收藏房源", description = "用户收藏喜欢的房源")
    @PostMapping("/add/{homesteadId}")
    public Result<Void> addFavorite(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long homesteadId) {
        Long userId = StpUtil.getLoginIdAsLong();
        favoriteService.addFavorite(userId, homesteadId);
        return Result.success();
    }

    @Operation(summary = "取消收藏", description = "用户取消收藏的房源")
    @DeleteMapping("/remove/{homesteadId}")
    public Result<Void> removeFavorite(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long homesteadId) {
        Long userId = StpUtil.getLoginIdAsLong();
        favoriteService.removeFavorite(userId, homesteadId);
        return Result.success();
    }

    @Operation(summary = "检查是否已收藏", description = "检查指定房源是否已被当前用户收藏")
    @GetMapping("/check/{homesteadId}")
    public Result<Boolean> isFavorite(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long homesteadId) {
        Long userId = StpUtil.getLoginIdAsLong();
        Boolean isFavorite = favoriteService.isFavorite(userId, homesteadId);
        return Result.success(isFavorite);
    }

    @Operation(summary = "获取我的收藏列表", description = "分页查询当前用户的收藏列表")
    @GetMapping("/my")
    public Result<PageResult<FavoriteVO>> getMyFavoriteList(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        PageResult<FavoriteVO> pageResult = favoriteService.getMyFavoriteList(userId, pageNum, pageSize);
        return Result.success(pageResult);
    }

}
