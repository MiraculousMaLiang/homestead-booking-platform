package com.homestead.booking.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 房源查询DTO
 *
 * @author homestead
 * @since 2025-11-18
 */
@Data
public class HomesteadQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 城市
     */
    private String city;

    /**
     * 房型
     */
    private Integer roomType;

    /**
     * 最低价格
     */
    private BigDecimal minPrice;

    /**
     * 最高价格
     */
    private BigDecimal maxPrice;

    /**
     * 最少入住人数
     */
    private Integer minGuests;

    /**
     * 最低评分
     */
    private BigDecimal minRating;

    /**
     * 排序字段: price-价格 rating-评分 view-浏览量
     */
    private String sortField = "create_time";

    /**
     * 排序方式: asc-升序 desc-降序
     */
    private String sortOrder = "desc";

    /**
     * 当前页码
     */
    private Long pageNum = 1L;

    /**
     * 每页大小
     */
    private Long pageSize = 10L;

}
