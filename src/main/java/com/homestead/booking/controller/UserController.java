package com.homestead.booking.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.UserLoginDTO;
import com.homestead.booking.dto.UserRegisterDTO;
import com.homestead.booking.dto.UserUpdateDTO;
import com.homestead.booking.service.UserService;
import com.homestead.booking.vo.LoginVO;
import com.homestead.booking.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }

    /**
     * 发送验证码
     */
    @PostMapping("/sendVerifyCode")
    public Result<Void> sendVerifyCode(
            @NotBlank(message = "手机号不能为空")
            @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
            @RequestParam String phone) {
        userService.sendVerifyCode(phone);
        return Result.success();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        UserVO userVO = userService.getUserInfo();
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<Void> updateUserInfo(HttpServletRequest request,
                                       @RequestBody UserUpdateDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateUserInfo(userId, dto);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/changePassword")
    public Result<Void> changePassword(
                                       @NotBlank(message = "原密码不能为空") @RequestParam String oldPassword,
                                       @NotBlank(message = "新密码不能为空") @RequestParam String newPassword) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

}
