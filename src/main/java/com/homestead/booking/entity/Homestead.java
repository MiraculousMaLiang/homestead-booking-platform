package com.homestead.booking.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 房源实体类
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
@TableName("homestead")
public class Homestead implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 房东用户ID
     */
    private Long userId;

    /**
     * 房源标题
     */
    private String title;

    /**
     * 房源描述
     */
    private String description;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 房型: 1-整套房 2-独立房间 3-合住房间
     */
    private Integer roomType;

    /**
     * 卧室数量
     */
    private Integer bedroomCount;

    /**
     * 床位数量
     */
    private Integer bedCount;

    /**
     * 卫生间数量
     */
    private Integer bathroomCount;

    /**
     * 最多入住人数
     */
    private Integer maxGuests;

    /**
     * 面积(平方米)
     */
    private BigDecimal area;

    /**
     * 日租金
     */
    private BigDecimal pricePerDay;

    /**
     * 周租金
     */
    private BigDecimal pricePerWeek;

    /**
     * 月租金
     */
    private BigDecimal pricePerMonth;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 最少入住天数
     */
    private Integer minDays;

    /**
     * 封面图
     */
    private String coverImage;

    /**
     * 入住时间
     */
    private LocalTime checkInTime;

    /**
     * 退房时间
     */
    private LocalTime checkOutTime;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 收藏次数
     */
    private Integer favoriteCount;

    /**
     * 订单数量
     */
    private Integer orderCount;

    /**
     * 评分(0-5)
     */
    private BigDecimal ratingScore;

    /**
     * 状态: 0-下架 1-上架 2-维护中
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除: 0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;

}
