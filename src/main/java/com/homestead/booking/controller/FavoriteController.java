package com.homestead.booking.controller;

import com.homestead.booking.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    /**
     * 收藏房源
     */
    @PostMapping("/add/{homesteadId}")
    public Result<Void> addFavorite(HttpServletRequest request,
                                    @PathVariable Long homesteadId) {
        Long userId = (Long) request.getAttribute("userId");
        // TODO: 实现收藏逻辑
        return Result.success();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/remove/{homesteadId}")
    public Result<Void> removeFavorite(HttpServletRequest request,
                                       @PathVariable Long homesteadId) {
        Long userId = (Long) request.getAttribute("userId");
        // TODO: 实现取消收藏逻辑
        return Result.success();
    }

}
