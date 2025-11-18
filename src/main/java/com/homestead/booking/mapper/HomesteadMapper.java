package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.Homestead;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房源Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface HomesteadMapper extends BaseMapper<Homestead> {

}
