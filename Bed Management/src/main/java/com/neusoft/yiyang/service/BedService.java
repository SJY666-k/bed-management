package com.neusoft.yiyang.service;

import com.neusoft.yiyang.vo.BedStatisticsVO;
import com.neusoft.yiyang.vo.RoomBedsVO;

import java.util.List;

/**
 * 床位示意图业务接口（需求 1：床位示意图）。
 */
public interface BedService {

    /**
     * 床位统计：总床位 / 空闲 / 有人 / 外出。
     *
     * @return 统计结果
     */
    BedStatisticsVO statistics();

    /**
     * 按楼层查询房间与床位，供床位示意图渲染。
     *
     * @param floor 楼层：1 一层、2 二层
     * @return 房间 + 床位列表
     */
    List<RoomBedsVO> listRoomBeds(String floor);
}
