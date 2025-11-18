package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
