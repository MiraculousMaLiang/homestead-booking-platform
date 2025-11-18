package com.homestead.booking.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 *
 * @author homestead
 * @since 2025-11-18
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 通用状态码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    // 用户相关
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_PASSWORD_ERROR(1003, "用户名或密码错误"),
    USER_DISABLED(1004, "用户已被禁用"),
    USER_NOT_LOGIN(1005, "用户未登录"),
    USER_LOGIN_EXPIRED(1006, "登录已过期,请重新登录"),
    USER_NO_PERMISSION(1007, "没有权限访问"),

    // 验证码相关
    VERIFY_CODE_ERROR(2001, "验证码错误"),
    VERIFY_CODE_EXPIRED(2002, "验证码已过期"),
    VERIFY_CODE_SEND_FAIL(2003, "验证码发送失败"),
    VERIFY_CODE_SEND_FREQUENTLY(2004, "验证码发送过于频繁"),

    // 房源相关
    HOMESTEAD_NOT_FOUND(3001, "房源不存在"),
    HOMESTEAD_NOT_AVAILABLE(3002, "房源不可预订"),
    HOMESTEAD_ALREADY_BOOKED(3003, "房源已被预订"),

    // 订单相关
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态错误"),
    ORDER_CANCEL_FAIL(4003, "订单取消失败"),
    ORDER_PAY_FAIL(4004, "订单支付失败"),

    // 支付相关
    PAYMENT_FAILED(5001, "支付失败"),
    PAYMENT_TIMEOUT(5002, "支付超时"),
    REFUND_FAILED(5003, "退款失败"),

    // 文件上传相关
    FILE_UPLOAD_ERROR(6001, "文件上传失败"),
    FILE_TYPE_ERROR(6002, "文件类型不支持"),
    FILE_SIZE_ERROR(6003, "文件大小超出限制"),

    // 评价相关
    REVIEW_ALREADY_EXISTS(7001, "已评价过该订单"),
    REVIEW_NOT_ALLOWED(7002, "不允许评价"),

    ;

    private final Integer code;
    private final String message;

}
