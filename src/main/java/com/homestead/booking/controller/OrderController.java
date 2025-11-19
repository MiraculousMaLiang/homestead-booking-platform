package com.homestead.booking.controller;

import com.homestead.booking.common.Result;
import com.homestead.booking.dto.OrderDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 订单Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Void> createOrder(HttpServletRequest request,
                                    @Valid @RequestBody OrderDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        // TODO: 实现订单创建逻辑
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderId}")
    public Result<Void> cancelOrder(HttpServletRequest request,
                                    @PathVariable Long orderId,
                                    @RequestParam String reason) {
        Long userId = (Long) request.getAttribute("userId");
        // TODO: 实现订单取消逻辑
        return Result.success();
    }

}
