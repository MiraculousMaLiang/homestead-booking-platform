package com.homestead.booking.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.UserLoginDTO;
import com.homestead.booking.dto.UserRegisterDTO;
import com.homestead.booking.dto.UserUpdateDTO;
import com.homestead.booking.service.*;
import com.homestead.booking.vo.HomesteadVO;
import com.homestead.booking.vo.InfomationVO;
import com.homestead.booking.vo.LoginVO;
import com.homestead.booking.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileUpdateService fileService;
    @Autowired
    private HomesteadService homesteadService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "用户注册", description = "通过手机号和密码注册新用户")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @Operation(summary = "用户登录", description = "支持密码登录和手机验证码登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }

    @Operation(summary = "发送验证码", description = "发送手机验证码，用于登录或注册")
    @PostMapping("/sendVerifyCode")
    public Result<Void> sendVerifyCode(
            @Parameter(description = "手机号", required = true, example = "13800138000")
            @NotBlank(message = "手机号不能为空")
            @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
            @RequestParam String phone) {
        userService.sendVerifyCode(phone);
        return Result.success();
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        UserVO userVO = userService.getUserInfo();
        return Result.success(userVO);
    }

    @Operation(summary = "更新用户信息", description = "更新当前登录用户的个人信息")
    @PutMapping("/update")
    public Result<Void> updateUserInfo(HttpServletRequest request,
                                       @RequestBody UserUpdateDTO dto) {
//        Long userId = (Long) request.getAttribute("userId");
        Long userId = StpUtil.getLoginIdAsLong();
        userService.updateUserInfo(userId, dto);
        return Result.success();
    }

    @Operation(summary = "修改密码", description = "修改当前登录用户的登录密码")
    @PutMapping("/changePassword")
    public Result<Void> changePassword(
            @Parameter(description = "原密码", required = true)
            @NotBlank(message = "原密码不能为空") @RequestParam String oldPassword,
            @Parameter(description = "新密码", required = true)
            @NotBlank(message = "新密码不能为空") @RequestParam String newPassword) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success();
    }

    @Operation(summary = "修改头像")
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(MultipartFile file) throws Exception {
        @URL String avatarUrl=fileService.upload(file);
        userService.updateAvatar(avatarUrl);
        return Result.success("修改成功");
    }

    @Operation(summary = "获取用户个人中心信息", description = "获取当前登录用户个人中心的详细信息")
    @GetMapping("/infomation")
    public Result<InfomationVO> getUserInfomation() {
        InfomationVO infomationVO = new InfomationVO();
        UserVO userVO = userService.getUserInfo();
        BeanUtils.copyProperties(userVO, infomationVO);

        // 获取用户发布的房源数量
        infomationVO.setHomesteadCount(homesteadService.getMyHomesteadList(userVO.getId(), 1L, 100L).getTotal());

        // 获取用户收到的评论数量
        infomationVO.setReviewCount(reviewService.getUserReviewList(userVO.getId(), 1L, 100L).getTotal());

        // 获取用户收藏的房源数量
        infomationVO.setFavoriteCount(favoriteService.getMyFavoriteList(userVO.getId(), 1L, 100L).getTotal());
        return Result.success(infomationVO);
    }

}
