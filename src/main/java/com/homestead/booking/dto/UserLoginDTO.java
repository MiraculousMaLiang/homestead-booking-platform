package com.homestead.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录DTO
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名/手机号
     */
    @NotBlank(message = "用户名或手机号不能为空")
    private String username;

    /**
     * 密码(密码登录时必填)
     */
    private String password;

    /**
     * 验证码(验证码登录时必填)
     */
    private String verifyCode;

    /**
     * 登录类型: 1-密码登录 2-验证码登录
     */
    private Integer loginType = 1;

}
