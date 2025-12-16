package com.homestead.booking.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.dto.OrderDTO;
import com.homestead.booking.entity.Homestead;
import com.homestead.booking.entity.Order;
import com.homestead.booking.entity.User;
import com.homestead.booking.exception.BusinessException;
import com.homestead.booking.mapper.HomesteadMapper;
import com.homestead.booking.mapper.OrderMapper;
import com.homestead.booking.mapper.UserMapper;
import com.homestead.booking.service.OrderService;
import com.homestead.booking.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单Service实现类
 *
 * @author homestead
 * @since 2025-12-03
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private HomesteadMapper homesteadMapper;

    @Autowired
    private UserMapper userMapper;

    private static final Map<Integer, String> ORDER_STATUS_MAP = new HashMap<>();

    static {
        ORDER_STATUS_MAP.put(1, "待支付");
        ORDER_STATUS_MAP.put(2, "已支付");
        ORDER_STATUS_MAP.put(3, "已入住");
        ORDER_STATUS_MAP.put(4, "已完成");
        ORDER_STATUS_MAP.put(5, "已取消");
        ORDER_STATUS_MAP.put(6, "已退款");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long userId, OrderDTO dto) {
        // 校验房源是否存在且可预订
        Homestead homestead = homesteadMapper.selectById(dto.getHomesteadId());
        if (homestead == null) {
            throw new BusinessException("房源不存在");
        }
        if (homestead.getStatus() != 1) {
            throw new BusinessException("房源已下架，无法预订");
        }

        // 校验日期
        LocalDate now = LocalDate.now();
        if (dto.getCheckInDate().isBefore(now)) {
            throw new BusinessException("入住日期不能早于今天");
        }
        if (dto.getCheckOutDate().isBefore(dto.getCheckInDate()) || dto.getCheckOutDate().isEqual(dto.getCheckInDate())) {
            throw new BusinessException("退房日期必须晚于入住日期");
        }

        // 检查房源在该时间段是否可用
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getHomesteadId, dto.getHomesteadId())
                .in(Order::getOrderStatus, 2, 3) // 已支付或已入住
                .and(w -> w.between(Order::getCheckInDate, dto.getCheckInDate(), dto.getCheckOutDate())
                        .or()
                        .between(Order::getCheckOutDate, dto.getCheckInDate(), dto.getCheckOutDate())
                        .or()
                        .nested(n -> n.le(Order::getCheckInDate, dto.getCheckInDate())
                                .ge(Order::getCheckOutDate, dto.getCheckOutDate())));
        Long count = orderMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("该时间段房源已被预订");
        }

        // 计算入住天数
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());

        // 计算价格
        BigDecimal totalPrice = homestead.getPricePerDay().multiply(BigDecimal.valueOf(days));
        BigDecimal deposit = homestead.getDeposit() != null ? homestead.getDeposit() : BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO; // TODO: 优惠券逻辑
        BigDecimal actualPrice = totalPrice.add(deposit).subtract(discountAmount);

        // 生成订单编号
        String orderNo = generateOrderNo();

        // 生成入住码
        String checkInCode = RandomUtil.randomNumbers(6);

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setHomesteadId(dto.getHomesteadId());
        order.setLandlordId(homestead.getUserId());
        order.setCheckInDate(dto.getCheckInDate());
        order.setCheckOutDate(dto.getCheckOutDate());
        order.setDays((int) days);
        order.setGuestCount(dto.getGuestCount());
        order.setGuestName(dto.getGuestName());
        order.setGuestPhone(dto.getGuestPhone());
        order.setTotalPrice(totalPrice);
        order.setDeposit(deposit);
        order.setDiscountAmount(discountAmount);
        order.setActualPrice(actualPrice);
        order.setSpecialRequest(dto.getSpecialRequest());
        order.setCheckInCode(checkInCode);
        order.setOrderStatus(1); // 待支付

        orderMapper.insert(order);

        log.info("创建订单成功, 订单号: {}, 用户ID: {}", orderNo, userId);
        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long userId, Long orderId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权取消该订单");
        }
        if (order.getOrderStatus() == 5) {
            throw new BusinessException("订单已取消");
        }
        if (order.getOrderStatus() == 4) {
            throw new BusinessException("订单已完成，无法取消");
        }
        if (order.getOrderStatus() == 3) {
            throw new BusinessException("已入住订单无法取消");
        }

        // 更新订单状态
        Order updateOrder = new Order();
        updateOrder.setId(orderId);
        updateOrder.setOrderStatus(5); // 已取消
        updateOrder.setCancelReason(reason);
        updateOrder.setCancelTime(LocalDateTime.now());

        // 如果已支付，更新为已退款
        if (order.getOrderStatus() == 2) {
            updateOrder.setOrderStatus(6); // 已退款
            // TODO: 调用退款接口
        }

        orderMapper.updateById(updateOrder);
        log.info("取消订单成功, 订单ID: {}, 用户ID: {}", orderId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权支付该订单");
        }
        if (order.getOrderStatus() != 1) {
            throw new BusinessException("订单状态异常，无法支付");
        }

        // TODO: 调用支付接口

        // 更新订单状态
        Order updateOrder = new Order();
        updateOrder.setId(orderId);
        updateOrder.setOrderStatus(2); // 已支付
        updateOrder.setPayTime(LocalDateTime.now());

        orderMapper.updateById(updateOrder);
        log.info("支付订单成功, 订单ID: {}, 用户ID: {}", orderId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmCheckIn(Long orderId, String checkInCode) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getOrderStatus() != 2) {
            throw new BusinessException("订单状态异常");
        }
        if (!order.getCheckInCode().equals(checkInCode)) {
            throw new BusinessException("入住码错误");
        }

        // 更新订单状态
        Order updateOrder = new Order();
        updateOrder.setId(orderId);
        updateOrder.setOrderStatus(3); // 已入住
        updateOrder.setCheckInTime(LocalDateTime.now());

        orderMapper.updateById(updateOrder);
        log.info("确认入住成功, 订单ID: {}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmCheckOut(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getOrderStatus() != 3) {
            throw new BusinessException("订单状态异常");
        }

        // 更新订单状态
        Order updateOrder = new Order();
        updateOrder.setId(orderId);
        updateOrder.setOrderStatus(4); // 已完成
        updateOrder.setCheckOutTime(LocalDateTime.now());

        orderMapper.updateById(updateOrder);
        log.info("确认退房成功, 订单ID: {}", orderId);
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        return buildOrderVO(order);
    }

    @Override
    public PageResult<OrderVO> getMyOrderList(Long userId, Integer orderStatus, Long pageNum, Long pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (orderStatus != null) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        IPage<Order> orderPage = orderMapper.selectPage(page, wrapper);

        List<OrderVO> list = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            list.add(buildOrderVO(order));
        }

        return PageResult.build(orderPage.getTotal(),pageNum,pageSize,list);
    }

    @Override
    public PageResult<OrderVO> getLandlordOrderList(Long landlordId, Integer orderStatus, Long pageNum, Long pageSize) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getLandlordId, landlordId);
        if (orderStatus != null) {
            wrapper.eq(Order::getOrderStatus, orderStatus);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        IPage<Order> orderPage = orderMapper.selectPage(page, wrapper);

        List<OrderVO> list = new ArrayList<>();
        for (Order order : orderPage.getRecords()) {
            list.add(buildOrderVO(order));
        }

        return PageResult.build(orderPage.getTotal(),pageNum,pageSize, list);
    }

    /**
     * 构建OrderVO
     */
    private OrderVO buildOrderVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderStatusDesc(ORDER_STATUS_MAP.get(order.getOrderStatus()));

        // 查询房源信息
        Homestead homestead = homesteadMapper.selectById(order.getHomesteadId());
        if (homestead != null) {
            vo.setHomesteadName(homestead.getTitle());
            vo.setHomesteadCover(homestead.getCoverImage());
        }

        // 查询房东信息
        User landlord = userMapper.selectById(order.getLandlordId());
        if (landlord != null) {
            vo.setLandlordName(landlord.getNickname());
        }

        return vo;
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        return "O" + DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);
    }

}
