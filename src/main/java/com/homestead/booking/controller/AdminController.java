package com.homestead.booking.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.AdminUserDTO;
import com.homestead.booking.dto.OrderDTO;
import com.homestead.booking.entity.Order;
import com.homestead.booking.entity.User;
import com.homestead.booking.service.OrderService;
import com.homestead.booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Tag(name = "管理员管理", description = "用户列表、查询、管理等接口")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;
    /**
     * 获取用户列表
     */
    @Operation(summary = "获取用户列表")
    @PostMapping("/getUserList")
    public Result<PageResult<User>>getUserList(@RequestBody AdminUserDTO adminUserDTO) {
        PageResult<User> pageResult = userService.getUserList(adminUserDTO);
        return Result.success(pageResult);
    }
    /**
     * 封禁/恢复用户
     */
    @Operation(summary = "封禁/恢复用户")
    @PostMapping("/deleteUser")
    public Result<String> deleteUser(@RequestBody User user) {
        boolean result = userService.removeUser(user);
        if (result) {
            return Result.success("操作成功");
        } else {
            return Result.error("操作失败");
        }
    }


}
