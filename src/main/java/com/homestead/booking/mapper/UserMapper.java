package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
