package com.homestead.booking.service;

import com.homestead.booking.common.PageResult;
import com.homestead.booking.dto.HomesteadDTO;
import com.homestead.booking.dto.HomesteadQueryDTO;
import com.homestead.booking.vo.HomesteadVO;

/**
 * 房源Service接口
 *
 * @author homestead
 * @since 2025-11-18
 */
public interface HomesteadService {

    /**
     * 发布房源
     */
    void publishHomestead(Long userId, HomesteadDTO dto);

    /**
     * 更新房源
     */
    void updateHomestead(Long userId, Long homesteadId, HomesteadDTO dto);

    /**
     * 删除房源
     */
    void deleteHomestead(Long userId, Long homesteadId);

    /**
     * 上架/下架房源
     */
    void updateHomesteadStatus(Long userId, Long homesteadId, Integer status);

    /**
     * 获取房源详情
     */
    HomesteadVO getHomesteadDetail(Long homesteadId);

    /**
     * 查询房源列表
     */
    PageResult<HomesteadVO> queryHomesteadList(HomesteadQueryDTO dto);

    /**
     * 获取我发布的房源列表
     */
    PageResult<HomesteadVO> getMyHomesteadList(Long userId, Long pageNum, Long pageSize);

}
