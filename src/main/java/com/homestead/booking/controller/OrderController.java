package com.homestead.booking.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.OrderDTO;
import com.homestead.booking.service.OrderService;
import com.homestead.booking.vo.OrderVO;
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
 * 订单Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Tag(name = "订单管理", description = "订单创建、支付、取消等接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "创建订单", description = "用户创建新的预订订单")
    @PostMapping("/create")
    public Result<Long> createOrder(@Valid @RequestBody OrderDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        Long orderId = orderService.createOrder(userId, dto);
        return Result.success(orderId);
    }

    @Operation(summary = "取消订单", description = "用户取消已创建的订单")
    @PutMapping("/cancel/{orderId}")
    public Result<Void> cancelOrder(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId,
            @Parameter(description = "取消原因", required = true)
            @NotBlank(message = "取消原因不能为空")
            @RequestParam String reason) {
        Long userId = StpUtil.getLoginIdAsLong();
        orderService.cancelOrder(userId, orderId, reason);
        return Result.success();
    }

    @Operation(summary = "支付订单", description = "用户支付待支付订单")
    @PostMapping("/pay/{orderId}")
    public Result<Void> payOrder(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        Long userId = StpUtil.getLoginIdAsLong();
        orderService.payOrder(userId, orderId);
        return Result.success();
    }

    @Operation(summary = "确认入住", description = "使用入住码确认入住")
    @PostMapping("/checkIn/{orderId}")
    public Result<Void> confirmCheckIn(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId,
            @Parameter(description = "入住码", required = true)
            @NotBlank(message = "入住码不能为空")
            @RequestParam String checkInCode) {
        orderService.confirmCheckIn(orderId, checkInCode);
        return Result.success();
    }

    @Operation(summary = "确认退房", description = "确认订单退房完成")
    @PostMapping("/checkOut/{orderId}")
    public Result<Void> confirmCheckOut(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        orderService.confirmCheckOut(orderId);
        return Result.success();
    }

    @Operation(summary = "获取订单详情", description = "查看指定订单的详细信息")
    @GetMapping("/detail/{orderId}")
    public Result<OrderVO> getOrderDetail(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        OrderVO orderVO = orderService.getOrderDetail(orderId);
        return Result.success(orderVO);
    }

    @Operation(summary = "获取我的订单列表", description = "获取当前用户的订单列表")
    @GetMapping("/my")
    public Result<PageResult<OrderVO>> getMyOrderList(
            @Parameter(description = "订单状态，不传查全部", example = "1")
            @RequestParam(required = false) Integer orderStatus,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        PageResult<OrderVO> pageResult = orderService.getMyOrderList(userId, orderStatus, pageNum, pageSize);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取房东的订单列表", description = "房东查看收到的订单")
    @GetMapping("/landlord")
    public Result<PageResult<OrderVO>> getLandlordOrderList(
            @Parameter(description = "订单状态，不传查全部", example = "1")
            @RequestParam(required = false) Integer orderStatus,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") Long pageSize) {
        Long landlordId = StpUtil.getLoginIdAsLong();
        PageResult<OrderVO> pageResult = orderService.getLandlordOrderList(landlordId, orderStatus, pageNum, pageSize);
        return Result.success(pageResult);
    }

}
