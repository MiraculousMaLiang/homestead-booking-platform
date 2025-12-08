package com.homestead.booking.service;

import com.homestead.booking.common.PageResult;
import com.homestead.booking.vo.FavoriteVO;

/**
 * 收藏Service接口
 *
 * @author homestead
 * @since 2025-12-03
 */
public interface FavoriteService {

    /**
     * 收藏房源
     *
     * @param userId 用户ID
     * @param homesteadId 房源ID
     */
    void addFavorite(Long userId, Long homesteadId);

    /**
     * 取消收藏
     *
     * @param userId 用户ID
     * @param homesteadId 房源ID
     */
    void removeFavorite(Long userId, Long homesteadId);

    /**
     * 检查是否已收藏
     *
     * @param userId 用户ID
     * @param homesteadId 房源ID
     * @return 是否已收藏
     */
    Boolean isFavorite(Long userId, Long homesteadId);

    /**
     * 获取我的收藏列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 收藏列表
     */
    PageResult<FavoriteVO> getMyFavoriteList(Long userId, Long pageNum, Long pageSize);

}
