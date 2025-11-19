package com.homestead.booking.controller;

import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.HomesteadDTO;
import com.homestead.booking.dto.HomesteadQueryDTO;
import com.homestead.booking.service.HomesteadService;
import com.homestead.booking.vo.HomesteadVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 房源Controller
 *
 * @author homestead
 * @since 2025-11-18
 */
@Slf4j
@RestController
@RequestMapping("/homestead")
public class HomesteadController {

    @Autowired
    private HomesteadService homesteadService;

    /**
     * 发布房源
     */
    @PostMapping("/publish")
    public Result<Void> publishHomestead(HttpServletRequest request,
                                         @Valid @RequestBody HomesteadDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        homesteadService.publishHomestead(userId, dto);
        return Result.success();
    }

    /**
     * 更新房源
     */
    @PutMapping("/update/{id}")
    public Result<Void> updateHomestead(HttpServletRequest request,
                                        @PathVariable Long id,
                                        @Valid @RequestBody HomesteadDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        homesteadService.updateHomestead(userId, id, dto);
        return Result.success();
    }

    /**
     * 删除房源
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteHomestead(HttpServletRequest request,
                                        @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        homesteadService.deleteHomestead(userId, id);
        return Result.success();
    }

    /**
     * 上架/下架房源
     */
    @PutMapping("/status/{id}")
    public Result<Void> updateHomesteadStatus(HttpServletRequest request,
                                              @PathVariable Long id,
                                              @RequestParam Integer status) {
        Long userId = (Long) request.getAttribute("userId");
        homesteadService.updateHomesteadStatus(userId, id, status);
        return Result.success();
    }

    /**
     * 获取房源详情
     */
    @GetMapping("/detail/{id}")
    public Result<HomesteadVO> getHomesteadDetail(@PathVariable Long id) {
        HomesteadVO homesteadVO = homesteadService.getHomesteadDetail(id);
        return Result.success(homesteadVO);
    }

    /**
     * 查询房源列表
     */
    @PostMapping("/list")
    public Result<PageResult<HomesteadVO>> queryHomesteadList(@RequestBody HomesteadQueryDTO dto) {
        PageResult<HomesteadVO> pageResult = homesteadService.queryHomesteadList(dto);
        return Result.success(pageResult);
    }

    /**
     * 获取我发布的房源列表
     */
    @GetMapping("/my")
    public Result<PageResult<HomesteadVO>> getMyHomesteadList(HttpServletRequest request,
                                                               @RequestParam(defaultValue = "1") Long pageNum,
                                                               @RequestParam(defaultValue = "10") Long pageSize) {
        Long userId = (Long) request.getAttribute("userId");
        PageResult<HomesteadVO> pageResult = homesteadService.getMyHomesteadList(userId, pageNum, pageSize);
        return Result.success(pageResult);
    }

}
