package com.homestead.booking.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息更新DTO
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
public class UserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 性别: 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 邮箱
     */
    private String email;

}
