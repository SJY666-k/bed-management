package com.neusoft.yiyang.service;

import com.neusoft.yiyang.dto.BedDetailsQueryDTO;
import com.neusoft.yiyang.dto.BedSwapDTO;
import com.neusoft.yiyang.dto.UpdateEndTimeDTO;
import com.neusoft.yiyang.vo.BedDetailsVO;

import java.util.List;

/**
 * 床位管理业务接口（需求 2：床位管理）。
 */
public interface BedManageService {

    /**
     * 多条件组合查询床位使用详情。
     *
     * @param query 客户姓名（模糊）、入住日期、使用状态
     * @return 床位使用详情列表
     */
    List<BedDetailsVO> listDetails(BedDetailsQueryDTO query);

    /**
     * 修改床位使用结束时间（只能改结束时间，其它字段不允许改动）。
     *
     * @param dto 详情 ID + 新的结束时间
     */
    void updateEndTime(UpdateEndTimeDTO dto);

    /**
     * 床位调换：旧床位记录失效、旧床位变空闲、新床位变有人、新增一条使用记录，
     * 并同步客户信息（房间号、床号、楼号）。只能当天调换当天办理。
     *
     * @param dto 客户 ID + 新楼层 + 新房间号 + 新床位 ID
     */
    void swapBed(BedSwapDTO dto);
}
