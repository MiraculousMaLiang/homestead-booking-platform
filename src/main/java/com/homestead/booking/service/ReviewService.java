package com.homestead.booking.service;

import com.homestead.booking.common.PageResult;
import com.homestead.booking.dto.ReviewDTO;
import com.homestead.booking.vo.ReviewVO;

/**
 * 评价Service接口
 *
 * @author homestead
 * @since 2025-12-03
 */
public interface ReviewService {

    /**
     * 提交评价
     *
     * @param userId 用户ID
     * @param orderId 订单ID
     * @param dto 评价信息
     */
    void submitReview(Long userId, Long orderId, ReviewDTO dto);

    /**
     * 房东回复评价
     *
     * @param landlordId 房东ID
     * @param reviewId 评价ID
     * @param replyContent 回复内容
     */
    void replyReview(Long landlordId, Long reviewId, String replyContent);

    /**
     * 点赞评价
     *
     * @param reviewId 评价ID
     */
    void likeReview(Long reviewId);

    /**
     * 获取房源评价列表
     *
     * @param homesteadId 房源ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评价列表
     */
    PageResult<ReviewVO> getHomesteadReviewList(Long homesteadId, Long pageNum, Long pageSize);

    /**
     * 获取用户评价列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 评价列表
     */
    PageResult<ReviewVO> getUserReviewList(Long userId, Long pageNum, Long pageSize);

    /**
     * 获取评价详情
     *
     * @param reviewId 评价ID
     * @return 评价详情
     */
    ReviewVO getReviewDetail(Long reviewId);

}
