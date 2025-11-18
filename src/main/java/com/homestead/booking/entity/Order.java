package com.homestead.booking.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单实体类
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 房源ID
     */
    private Long homesteadId;

    /**
     * 房东ID
     */
    private Long landlordId;

    /**
     * 入住日期
     */
    private LocalDate checkInDate;

    /**
     * 退房日期
     */
    private LocalDate checkOutDate;

    /**
     * 入住天数
     */
    private Integer days;

    /**
     * 入住人数
     */
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
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    private BigDecimal actualPrice;

    /**
     * 特殊需求
     */
    private String specialRequest;

    /**
     * 入住码
     */
    private String checkInCode;

    /**
     * 订单状态: 1-待支付 2-已支付 3-已入住 4-已完成 5-已取消 6-已退款
     */
    private Integer orderStatus;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 实际入住时间
     */
    private LocalDateTime checkInTime;

    /**
     * 实际退房时间
     */
    private LocalDateTime checkOutTime;

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
