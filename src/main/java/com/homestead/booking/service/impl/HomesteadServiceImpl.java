package com.homestead.booking.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.ResultCode;
import com.homestead.booking.dto.HomesteadDTO;
import com.homestead.booking.dto.HomesteadQueryDTO;
import com.homestead.booking.entity.Homestead;
import com.homestead.booking.exception.BusinessException;
import com.homestead.booking.mapper.HomesteadMapper;
import com.homestead.booking.service.HomesteadService;
import com.homestead.booking.vo.HomesteadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 房源Service实现类
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@Service
public class HomesteadServiceImpl implements HomesteadService {

    @Autowired
    private HomesteadMapper homesteadMapper;

    /**
     * 发布房源
     */
    @Override
    public void publishHomestead(Long userId, HomesteadDTO dto) {
        Homestead homestead = BeanUtil.copyProperties(dto, Homestead.class);
        homestead.setUserId(userId);
        homestead.setStatus(1); // 默认上架
        homestead.setViewCount(0);
        homestead.setFavoriteCount(0);
        homestead.setOrderCount(0);

        homesteadMapper.insert(homestead);

        log.info("发布房源成功, userId: {}, homesteadId: {}", userId, homestead.getId());
    }

    /**
     * 更新房源
     */
    @Override
    public void updateHomestead(Long userId, Long homesteadId, HomesteadDTO dto) {
        Homestead homestead = homesteadMapper.selectById(homesteadId);
        if (homestead == null) {
            throw new BusinessException(ResultCode.HOMESTEAD_NOT_FOUND);
        }

        // 检查权限
        if (!homestead.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.USER_NO_PERMISSION);
        }

        // 更新信息
        BeanUtil.copyProperties(dto, homestead, "id", "userId", "viewCount", "favoriteCount", "orderCount", "ratingScore");

        homesteadMapper.updateById(homestead);

        log.info("更新房源成功, homesteadId: {}", homesteadId);
    }

    /**
     * 删除房源
     */
    @Override
    public void deleteHomestead(Long userId, Long homesteadId) {
        Homestead homestead = homesteadMapper.selectById(homesteadId);
        if (homestead == null) {
            throw new BusinessException(ResultCode.HOMESTEAD_NOT_FOUND);
        }

        // 检查权限
        if (!homestead.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.USER_NO_PERMISSION);
        }

        // 逻辑删除
        homesteadMapper.deleteById(homesteadId);

        log.info("删除房源成功, homesteadId: {}", homesteadId);
    }

    /**
     * 上架/下架房源
     */
    @Override
    public void updateHomesteadStatus(Long userId, Long homesteadId, Integer status) {
        Homestead homestead = homesteadMapper.selectById(homesteadId);
        if (homestead == null) {
            throw new BusinessException(ResultCode.HOMESTEAD_NOT_FOUND);
        }

        // 检查权限
        if (!homestead.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.USER_NO_PERMISSION);
        }

        homestead.setStatus(status);
        homesteadMapper.updateById(homestead);

        log.info("更新房源状态成功, homesteadId: {}, status: {}", homesteadId, status);
    }

    /**
     * 获取房源详情
     */
    @Override
    public HomesteadVO getHomesteadDetail(Long homesteadId) {
        Homestead homestead = homesteadMapper.selectById(homesteadId);
        if (homestead == null) {
            throw new BusinessException(ResultCode.HOMESTEAD_NOT_FOUND);
        }

        // 增加浏览次数
        homestead.setViewCount(homestead.getViewCount() + 1);
        homesteadMapper.updateById(homestead);

        return BeanUtil.copyProperties(homestead, HomesteadVO.class);
    }

    /**
     * 查询房源列表
     */
    @Override
    public PageResult<HomesteadVO> queryHomesteadList(HomesteadQueryDTO dto) {
        LambdaQueryWrapper<Homestead> wrapper = new LambdaQueryWrapper<>();

        // 只查询上架的房源
        wrapper.eq(Homestead::getStatus, 1);

        // 关键词搜索
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.and(w -> w.like(Homestead::getTitle, dto.getKeyword())
                    .or()
                    .like(Homestead::getDescription, dto.getKeyword()));
        }

        // 城市筛选
        if (dto.getCity() != null && !dto.getCity().isEmpty()) {
            wrapper.eq(Homestead::getCity, dto.getCity());
        }

        // 房型筛选
        if (dto.getRoomType() != null) {
            wrapper.eq(Homestead::getRoomType, dto.getRoomType());
        }

        // 价格筛选
        if (dto.getMinPrice() != null) {
            wrapper.ge(Homestead::getPricePerDay, dto.getMinPrice());
        }
        if (dto.getMaxPrice() != null) {
            wrapper.le(Homestead::getPricePerDay, dto.getMaxPrice());
        }

        // 入住人数筛选
        if (dto.getMinGuests() != null) {
            wrapper.ge(Homestead::getMaxGuests, dto.getMinGuests());
        }

        // 评分筛选
        if (dto.getMinRating() != null) {
            wrapper.ge(Homestead::getRatingScore, dto.getMinRating());
        }

        // 排序
        if ("price".equals(dto.getSortField())) {
            wrapper.orderBy(true, "asc".equals(dto.getSortOrder()), Homestead::getPricePerDay);
        } else if ("rating".equals(dto.getSortField())) {
            wrapper.orderBy(true, "asc".equals(dto.getSortOrder()), Homestead::getRatingScore);
        } else if ("view".equals(dto.getSortField())) {
            wrapper.orderBy(true, "asc".equals(dto.getSortOrder()), Homestead::getViewCount);
        } else {
            wrapper.orderByDesc(Homestead::getCreateTime);
        }

        // 分页查询
        Page<Homestead> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<Homestead> resultPage = homesteadMapper.selectPage(page, wrapper);

        // 转换为VO
        List<HomesteadVO> records = resultPage.getRecords().stream()
                .map(h -> BeanUtil.copyProperties(h, HomesteadVO.class))
                .collect(Collectors.toList());

        return PageResult.build(resultPage.getTotal(), resultPage.getCurrent(),
                resultPage.getSize(), records);
    }

    /**
     * 获取我发布的房源列表
     */
    @Override
    public PageResult<HomesteadVO> getMyHomesteadList(Long userId, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<Homestead> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Homestead::getUserId, userId);
        wrapper.orderByDesc(Homestead::getCreateTime);

        Page<Homestead> page = new Page<>(pageNum, pageSize);
        Page<Homestead> resultPage = homesteadMapper.selectPage(page, wrapper);

        List<HomesteadVO> records = resultPage.getRecords().stream()
                .map(h -> BeanUtil.copyProperties(h, HomesteadVO.class))
                .collect(Collectors.toList());

        return PageResult.build(resultPage.getTotal(), resultPage.getCurrent(),
                resultPage.getSize(), records);
    }

}
