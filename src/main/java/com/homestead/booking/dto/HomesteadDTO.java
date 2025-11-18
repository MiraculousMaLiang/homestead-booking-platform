package com.homestead.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * 房源DTO
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
public class HomesteadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房源标题
     */
    @NotBlank(message = "房源标题不能为空")
    private String title;

    /**
     * 房源描述
     */
    private String description;

    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    private String province;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
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
    @NotNull(message = "房型不能为空")
    private Integer roomType;

    /**
     * 卧室数量
     */
    private Integer bedroomCount = 0;

    /**
     * 床位数量
     */
    private Integer bedCount = 0;

    /**
     * 卫生间数量
     */
    private Integer bathroomCount = 0;

    /**
     * 最多入住人数
     */
    @NotNull(message = "最多入住人数不能为空")
    private Integer maxGuests;

    /**
     * 面积(平方米)
     */
    private BigDecimal area;

    /**
     * 日租金
     */
    @NotNull(message = "日租金不能为空")
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
    private BigDecimal deposit = BigDecimal.ZERO;

    /**
     * 最少入住天数
     */
    private Integer minDays = 1;

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

}
