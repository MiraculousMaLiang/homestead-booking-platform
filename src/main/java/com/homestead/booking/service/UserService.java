package com.homestead.booking.service;

import com.homestead.booking.dto.UserLoginDTO;
import com.homestead.booking.dto.UserRegisterDTO;
import com.homestead.booking.dto.UserUpdateDTO;
import com.homestead.booking.vo.LoginVO;
import com.homestead.booking.vo.UserVO;

/**
 * 用户Service接口
 *
 * @author homestead
 * @since 2025-11-18
 */
public interface UserService {

    /**
     * 用户注册
     */
    void register(UserRegisterDTO dto);

    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO dto);

    /**
     * 发送验证码
     */
    void sendVerifyCode(String phone);

    /**
     * 获取用户信息
     */
    UserVO getUserInfo(Long userId);

    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, UserUpdateDTO dto);

    /**
     * 修改密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

}
