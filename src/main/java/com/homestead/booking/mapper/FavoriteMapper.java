package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

}
