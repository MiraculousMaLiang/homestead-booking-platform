package com.homestead.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homestead.booking.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 用户Mapper接口
 *
 * @author homestead
 * @since 2025-11-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 封禁/恢复用户
     *
     * @param id      用户ID
     * @param status  用户状态
     * @return 封禁/恢复结果
     */
    @Update("update user set status = #{status} where id = #{id}")
    boolean removeUser(Long id, Integer status);
}
