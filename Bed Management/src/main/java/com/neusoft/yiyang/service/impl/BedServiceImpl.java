package com.neusoft.yiyang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.yiyang.entity.Bed;
import com.neusoft.yiyang.entity.Room;
import com.neusoft.yiyang.mapper.BedMapper;
import com.neusoft.yiyang.mapper.RoomMapper;
import com.neusoft.yiyang.service.BedService;
import com.neusoft.yiyang.vo.BedStatisticsVO;
import com.neusoft.yiyang.vo.BedVO;
import com.neusoft.yiyang.vo.RoomBedsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 床位示意图业务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedMapper bedMapper;
    private final RoomMapper roomMapper;

    @Override
    public BedStatisticsVO statistics() {
        BedStatisticsVO statistics = bedMapper.selectStatistics();
        // 表为空时聚合结果为 null，兜底成 0，避免前端显示 undefined
        if (statistics == null) {
            statistics = new BedStatisticsVO();
            statistics.setTotal(0);
            statistics.setFree(0);
            statistics.setOccupied(0);
            statistics.setOut(0);
        }
        return statistics;
    }

    @Override
    public List<RoomBedsVO> listRoomBeds(String floor) {
        if (!StringUtils.hasText(floor)) {
            return Collections.emptyList();
        }

        // 按楼层查询房间
        List<Room> rooms = roomMapper.selectList(new LambdaQueryWrapper<Room>()
                .eq(Room::getRoomFloor, floor.trim())
                .orderByAsc(Room::getRoomNo));
        if (rooms.isEmpty()) {
            return Collections.emptyList();
        }

        // 一次查出房间下的床位，避免 N+1 查询
        List<Integer> roomNos = rooms.stream().map(Room::getRoomNo).collect(Collectors.toList());
        List<Bed> beds = bedMapper.selectList(new LambdaQueryWrapper<Bed>()
                .in(Bed::getRoomNo, roomNos)
                .orderByAsc(Bed::getRoomNo)
                .orderByAsc(Bed::getId));

        // 按房间号分组： [{roomNo, beds:[{id, bedNo, bedStatus}]}]
        Map<Integer, List<Bed>> bedGroup = beds.stream().collect(Collectors.groupingBy(Bed::getRoomNo));
        List<RoomBedsVO> result = new ArrayList<>(rooms.size());
        for (Room room : rooms) {
            RoomBedsVO roomVO = new RoomBedsVO();
            roomVO.setRoomNo(room.getRoomNo());
            List<Bed> roomBeds = bedGroup.getOrDefault(room.getRoomNo(), Collections.emptyList());
            roomVO.setBeds(roomBeds.stream()
                    .map(bed -> new BedVO(bed.getId(), bed.getBedNo(), bed.getBedStatus()))
                    .collect(Collectors.toList()));
            result.add(roomVO);
        }
        log.debug("楼层 {} 共查询到 {} 个房间、{} 张床位", floor, rooms.size(), beds.size());
        return result;
    }
}
