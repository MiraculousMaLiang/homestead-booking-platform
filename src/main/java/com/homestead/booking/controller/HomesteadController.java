package com.homestead.booking.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.homestead.booking.common.PageResult;
import com.homestead.booking.common.Result;
import com.homestead.booking.dto.HomesteadDTO;
import com.homestead.booking.dto.HomesteadQueryDTO;
import com.homestead.booking.service.HomesteadService;
import com.homestead.booking.vo.HomesteadVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "房源管理", description = "房源发布、更新、查询等接口")
@Slf4j
@RestController
@RequestMapping("/homestead")
public class HomesteadController {

    @Autowired
    private HomesteadService homesteadService;

    @Operation(summary = "发布房源", description = "房东发布新的房源信息")
    @PostMapping("/publish")
    public Result<Void> publishHomestead(@Valid @RequestBody HomesteadDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        homesteadService.publishHomestead(userId, dto);
        return Result.success();
    }

    @Operation(summary = "更新房源", description = "房东更新已发布的房源信息")
    @PutMapping("/update/{id}")
    public Result<Void> updateHomestead(
                                        @Parameter(description = "房源ID", required = true)
                                        @PathVariable Long id,
                                        @Valid @RequestBody HomesteadDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        homesteadService.updateHomestead(userId, id, dto);
        return Result.success();
    }

    @Operation(summary = "删除房源", description = "房东删除已发布的房源(逻辑删除)")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteHomestead(
                                        @Parameter(description = "房源ID", required = true)
                                        @PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        homesteadService.deleteHomestead(userId, id);
        return Result.success();
    }

    @Operation(summary = "上架/下架房源", description = "房东修改房源上架状态，0-下架 1-上架")
    @PutMapping("/status/{id}")
    public Result<Void> updateHomesteadStatus(
                                              @Parameter(description = "房源ID", required = true)
                                              @PathVariable Long id,
                                              @Parameter(description = "状态：0-下架 1-上架", required = true)
                                              @RequestParam Integer status) {
        Long userId = StpUtil.getLoginIdAsLong();
        homesteadService.updateHomesteadStatus(userId, id, status);
        return Result.success();
    }

    @Operation(summary = "获取房源详情", description = "查看指定房源的详细信息")
    @GetMapping("/detail/{id}")
    public Result<HomesteadVO> getHomesteadDetail(
            @Parameter(description = "房源ID", required = true)
            @PathVariable Long id) {
        HomesteadVO homesteadVO = homesteadService.getHomesteadDetail(id);
        return Result.success(homesteadVO);
    }

    @Operation(summary = "查询房源列表", description = "根据条件分页查询房源列表")
    @PostMapping("/list")
    public Result<PageResult<HomesteadVO>> queryHomesteadList(@RequestBody HomesteadQueryDTO dto) {
        PageResult<HomesteadVO> pageResult = homesteadService.queryHomesteadList(dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取我发布的房源列表", description = "房东查看自己发布的所有房源")
    @GetMapping("/my")
    public Result<PageResult<HomesteadVO>> getMyHomesteadList(
                                                               @Parameter(description = "页码", example = "1")
                                                               @RequestParam(defaultValue = "1") Long pageNum,
                                                               @Parameter(description = "每页数量", example = "10")
                                                               @RequestParam(defaultValue = "10") Long pageSize) {
        Long userId = StpUtil.getLoginIdAsLong();
        PageResult<HomesteadVO> pageResult = homesteadService.getMyHomesteadList(userId, pageNum, pageSize);
        return Result.success(pageResult);
    }

}
