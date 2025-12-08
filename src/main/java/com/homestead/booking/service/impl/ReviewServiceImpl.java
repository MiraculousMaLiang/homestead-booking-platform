package com.homestead.booking.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.dto.ReviewDTO;
import com.homestead.booking.entity.Homestead;
import com.homestead.booking.entity.Order;
import com.homestead.booking.entity.Review;
import com.homestead.booking.entity.User;
import com.homestead.booking.exception.BusinessException;
import com.homestead.booking.mapper.HomesteadMapper;
import com.homestead.booking.mapper.OrderMapper;
import com.homestead.booking.mapper.ReviewMapper;
import com.homestead.booking.mapper.UserMapper;
import com.homestead.booking.service.ReviewService;
import com.homestead.booking.vo.ReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评价Service实现类
 *
 * @author homestead
 * @since 2025-12-03
 */
@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HomesteadMapper homesteadMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitReview(Long userId, Long orderId, ReviewDTO dto) {
        // 校验订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权评价该订单");
        }
        if (order.getOrderStatus() != 4) {
            throw new BusinessException("只有已完成的订单才能评价");
        }

        // 检查是否已评价
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getOrderId, orderId);
        Long count = reviewMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("该订单已评价");
        }

        // 创建评价
        Review review = new Review();
        review.setOrderId(orderId);
        review.setUserId(userId);
        review.setHomesteadId(order.getHomesteadId());
        review.setRatingOverall(dto.getRatingOverall());
        review.setRatingLocation(dto.getRatingLocation());
        review.setRatingCleanliness(dto.getRatingCleanliness());
        review.setRatingFacility(dto.getRatingFacility());
        review.setRatingService(dto.getRatingService());
        review.setRatingValue(dto.getRatingValue());
        review.setContent(dto.getContent());

        if (CollUtil.isNotEmpty(dto.getImages())) {
            review.setImages(String.join(",", dto.getImages()));
        }
        if (CollUtil.isNotEmpty(dto.getTags())) {
            review.setTags(String.join(",", dto.getTags()));
        }

        review.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : 0);
        review.setLikeCount(0);
        review.setStatus(1); // 默认已通过

        reviewMapper.insert(review);
        log.info("提交评价成功, 评价ID: {}, 订单ID: {}", review.getId(), orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyReview(Long landlordId, Long reviewId, String replyContent) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }

        // 查询房源，验证是否是房东
        Homestead homestead = homesteadMapper.selectById(review.getHomesteadId());
        if (homestead == null || !homestead.getUserId().equals(landlordId)) {
            throw new BusinessException("无权回复该评价");
        }

        // 更新回复
        Review updateReview = new Review();
        updateReview.setId(reviewId);
        updateReview.setReplyContent(replyContent);
        updateReview.setReplyTime(LocalDateTime.now());

        reviewMapper.updateById(updateReview);
        log.info("回复评价成功, 评价ID: {}, 房东ID: {}", reviewId, landlordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeReview(Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }

        // 点赞数+1
        Review updateReview = new Review();
        updateReview.setId(reviewId);
        updateReview.setLikeCount(review.getLikeCount() + 1);

        reviewMapper.updateById(updateReview);
        log.info("点赞评价成功, 评价ID: {}", reviewId);
    }

    @Override
    public PageResult<ReviewVO> getHomesteadReviewList(Long homesteadId, Long pageNum, Long pageSize) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getHomesteadId, homesteadId)
                .eq(Review::getStatus, 1) // 只查已通过的评价
                .orderByDesc(Review::getCreateTime);

        IPage<Review> reviewPage = reviewMapper.selectPage(page, wrapper);

        List<ReviewVO> list = new ArrayList<>();
        for (Review review : reviewPage.getRecords()) {
            list.add(buildReviewVO(review));
        }

        return PageResult.success(reviewPage.getTotal(), list);
    }

    @Override
    public PageResult<ReviewVO> getUserReviewList(Long userId, Long pageNum, Long pageSize) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getUserId, userId)
                .orderByDesc(Review::getCreateTime);

        IPage<Review> reviewPage = reviewMapper.selectPage(page, wrapper);

        List<ReviewVO> list = new ArrayList<>();
        for (Review review : reviewPage.getRecords()) {
            list.add(buildReviewVO(review));
        }

        return PageResult.success(reviewPage.getTotal(), list);
    }

    @Override
    public ReviewVO getReviewDetail(Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException("评价不存在");
        }

        return buildReviewVO(review);
    }

    /**
     * 构建ReviewVO
     */
    private ReviewVO buildReviewVO(Review review) {
        ReviewVO vo = new ReviewVO();
        BeanUtils.copyProperties(review, vo);

        // 处理图片列表
        if (review.getImages() != null && !review.getImages().isEmpty()) {
            vo.setImages(Arrays.asList(review.getImages().split(",")));
        }

        // 处理标签列表
        if (review.getTags() != null && !review.getTags().isEmpty()) {
            vo.setTags(Arrays.asList(review.getTags().split(",")));
        }

        // 查询用户信息(如果不匿名)
        if (review.getIsAnonymous() == 0) {
            User user = userMapper.selectById(review.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            }
        } else {
            vo.setUserNickname("匿名用户");
        }

        // 查询房源信息
        Homestead homestead = homesteadMapper.selectById(review.getHomesteadId());
        if (homestead != null) {
            vo.setHomesteadName(homestead.getName());
        }

        return vo;
    }

}
