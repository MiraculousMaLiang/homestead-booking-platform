package com.homestead.booking.service;

import com.homestead.booking.common.PageResult;
import com.homestead.booking.dto.OrderDTO;
import com.homestead.booking.vo.OrderVO;

/**
 * 订单Service接口
 *
 * @author homestead
 * @since 2025-12-03
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param userId 用户ID
     * @param dto 订单信息
     * @return 订单ID
     */
    Long createOrder(Long userId, OrderDTO dto);

    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param reason 取消原因
     */
    void cancelOrder(Long userId, Long orderId, String reason);

    /**
     * 支付订单
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void payOrder(Long userId, Long orderId);

    /**
     * 确认入住
     *
     * @param orderId 订单ID
     * @param checkInCode 入住码
     */
    void confirmCheckIn(Long orderId, String checkInCode);

    /**
     * 确认退房
     *
     * @param orderId 订单ID
     */
    void confirmCheckOut(Long orderId);

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderVO getOrderDetail(Long orderId);

    /**
     * 获取我的订单列表
     *
     * @param userId 用户ID
     * @param orderStatus 订单状态(可选)
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 订单列表
     */
    PageResult<OrderVO> getMyOrderList(Long userId, Integer orderStatus, Long pageNum, Long pageSize);

    /**
     * 获取房东的订单列表
     *
     * @param landlordId 房东ID
     * @param orderStatus 订单状态(可选)
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 订单列表
     */
    PageResult<OrderVO> getLandlordOrderList(Long landlordId, Integer orderStatus, Long pageNum, Long pageSize);

}
