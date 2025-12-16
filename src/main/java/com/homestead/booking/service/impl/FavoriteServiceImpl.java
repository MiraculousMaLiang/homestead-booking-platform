package com.homestead.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.entity.Favorite;
import com.homestead.booking.entity.Homestead;
import com.homestead.booking.exception.BusinessException;
import com.homestead.booking.mapper.FavoriteMapper;
import com.homestead.booking.mapper.HomesteadMapper;
import com.homestead.booking.service.FavoriteService;
import com.homestead.booking.vo.FavoriteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏Service实现类
 *
 * @author homestead
 * @since 2025-12-03
 */
@Slf4j
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private HomesteadMapper homesteadMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFavorite(Long userId, Long homesteadId) {
        // 校验房源是否存在
        Homestead homestead = homesteadMapper.selectById(homesteadId);
        if (homestead == null) {
            throw new BusinessException("房源不存在");
        }

        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getHomesteadId, homesteadId);
        Long count = favoriteMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("已收藏该房源");
        }

        // 添加收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setHomesteadId(homesteadId);

        favoriteMapper.insert(favorite);
        log.info("收藏房源成功, 用户ID: {}, 房源ID: {}", userId, homesteadId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long homesteadId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getHomesteadId, homesteadId);

        int count = favoriteMapper.delete(wrapper);
        if (count == 0) {
            throw new BusinessException("未收藏该房源");
        }

        log.info("取消收藏成功, 用户ID: {}, 房源ID: {}", userId, homesteadId);
    }

    @Override
    public Boolean isFavorite(Long userId, Long homesteadId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getHomesteadId, homesteadId);

        Long count = favoriteMapper.selectCount(wrapper);
        return count > 0;
    }

    @Override
    public PageResult<FavoriteVO> getMyFavoriteList(Long userId, Long pageNum, Long pageSize) {
        Page<Favorite> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);

        IPage<Favorite> favoritePage = favoriteMapper.selectPage(page, wrapper);

        List<FavoriteVO> list = new ArrayList<>();
        for (Favorite favorite : favoritePage.getRecords()) {
            list.add(buildFavoriteVO(favorite));
        }
        PageResult<FavoriteVO> pageResult = PageResult.build(favoritePage.getTotal(), favoritePage.getPages(), favoritePage.getSize(),list);

        return pageResult;
    }

    /**
     * 构建FavoriteVO
     */
    private FavoriteVO buildFavoriteVO(Favorite favorite) {
        FavoriteVO vo = new FavoriteVO();
        BeanUtils.copyProperties(favorite, vo);

        // 查询房源信息
        Homestead homestead = homesteadMapper.selectById(favorite.getHomesteadId());
        if (homestead != null) {
            vo.setHomesteadName(homestead.getTitle());
            vo.setHomesteadCover(homestead.getCoverImage());
            vo.setHomesteadAddress(homestead.getAddress());
            vo.setHomesteadPrice(homestead.getPricePerDay());
            vo.setHomesteadStatus(homestead.getStatus());
        }

        return vo;
    }

}
