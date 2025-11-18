package com.homestead.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 订单DTO
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房源ID
     */
    @NotNull(message = "房源ID不能为空")
    private Long homesteadId;

    /**
     * 入住日期
     */
    @NotNull(message = "入住日期不能为空")
    private LocalDate checkInDate;

    /**
     * 退房日期
     */
    @NotNull(message = "退房日期不能为空")
    private LocalDate checkOutDate;

    /**
     * 入住人数
     */
    @NotNull(message = "入住人数不能为空")
    private Integer guestCount;

    /**
     * 入住人姓名
     */
    private String guestName;

    /**
     * 入住人手机号
     */
    private String guestPhone;

    /**
     * 特殊需求
     */
    private String specialRequest;

}
