package com.homestead.booking.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfomationVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户类型: 1-普通用户 2-房东 3-管理员
     */
    private Integer userType;

    /**
     * 房源数量
     */
    private Long homesteadCount;

    /**
     * 评价数量
     */
    private Long reviewCount;

    /**
     * 收藏数量
     */
    private Long favoriteCount;
}
