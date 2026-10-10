package com.neusoft.yiyang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.yiyang.entity.Room;
import org.apache.ibatis.annotations.Mapper;

/**
 * 房间 Mapper。床位示意图需要「按楼层查房间号」。
 */
@Mapper
public interface RoomMapper extends BaseMapper<Room> {
}
