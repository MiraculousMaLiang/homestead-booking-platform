package com.homestead.booking.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单VO
 *
 * @author homestead
 * @since 2025-12-03
 */
@Data
@Schema(description = "订单信息")
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "房源ID")
    private Long homesteadId;

    @Schema(description = "房源名称")
    private String homesteadName;

    @Schema(description = "房源封面图")
    private String homesteadCover;

    @Schema(description = "房东ID")
    private Long landlordId;

    @Schema(description = "房东姓名")
    private String landlordName;

    @Schema(description = "入住日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @Schema(description = "退房日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @Schema(description = "入住天数")
    private Integer days;

    @Schema(description = "入住人数")
    private Integer guestCount;

    @Schema(description = "入住人姓名")
    private String guestName;

    @Schema(description = "入住人手机号")
    private String guestPhone;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "押金")
    private BigDecimal deposit;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "实付金额")
    private BigDecimal actualPrice;

    @Schema(description = "特殊需求")
    private String specialRequest;

    @Schema(description = "入住码")
    private String checkInCode;

    @Schema(description = "订单状态: 1-待支付 2-已支付 3-已入住 4-已完成 5-已取消 6-已退款")
    private Integer orderStatus;

    @Schema(description = "订单状态描述")
    private String orderStatusDesc;

    @Schema(description = "取消原因")
    private String cancelReason;

    @Schema(description = "取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelTime;

    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @Schema(description = "实际入住时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInTime;

    @Schema(description = "实际退房时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOutTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
