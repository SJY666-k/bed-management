package com.neusoft.yiyang.controller;

import com.neusoft.yiyang.common.Result;
import com.neusoft.yiyang.dto.BedDetailsQueryDTO;
import com.neusoft.yiyang.dto.BedSwapDTO;
import com.neusoft.yiyang.dto.UpdateEndTimeDTO;
import com.neusoft.yiyang.service.BedManageService;
import com.neusoft.yiyang.service.BedService;
import com.neusoft.yiyang.vo.BedDetailsVO;
import com.neusoft.yiyang.vo.BedStatisticsVO;
import com.neusoft.yiyang.vo.RoomBedsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 床位管理模块 Controller。
 *
 * <p>由于 application.yml 配置了 {@code server.servlet.context-path: /api}，
 * 所以下面这些接口的真实访问地址都是 {@code http://localhost:8080/api/bed/**}，
 * 与前端 Vite 代理（/api → localhost:8080）以及 axios 的 baseURL（/api）完全对应。</p>
 */
@Tag(name = "床位管理", description = "床位示意图 + 床位管理")
@RestController
@RequestMapping("/bed")
@RequiredArgsConstructor
public class BedController {

    private final BedService bedService;
    private final BedManageService bedManageService;

    @Operation(summary = "床位统计", description = "统计总床位以及空闲 / 有人 / 外出的床位数量")
    @GetMapping("/statistics")
    public Result<BedStatisticsVO> statistics() {
        return Result.success(bedService.statistics());
    }

    @Operation(summary = "床位示意图数据", description = "按楼层查询房间及其床位，床位调换弹窗的楼层/房间下拉也复用该接口")
    @GetMapping("/map")
    public Result<List<RoomBedsVO>> bedMap(@RequestParam(value = "floor", defaultValue = "1") String floor) {
        return Result.success(bedService.listRoomBeds(floor));
    }

    @Operation(summary = "床位使用详情列表", description = "按客户姓名（模糊）、入住日期、使用状态多条件组合查询")
    @GetMapping("/manage/details")
    public Result<List<BedDetailsVO>> details(BedDetailsQueryDTO query) {
        return Result.success(bedManageService.listDetails(query));
    }

    @Operation(summary = "修改床位使用结束时间", description = "只能修改结束时间")
    @PutMapping("/manage/updateEndTime")
    public Result<Void> updateEndTime(@Valid @RequestBody UpdateEndTimeDTO dto) {
        bedManageService.updateEndTime(dto);
        return Result.success("修改成功", null);
    }

    @Operation(summary = "床位调换", description = "只能当天调换当天办理，且只能调换到空闲床位")
    @PostMapping("/manage/swap")
    public Result<Void> swap(@Valid @RequestBody BedSwapDTO dto) {
        bedManageService.swapBed(dto);
        return Result.success("床位调换成功", null);
    }
}
