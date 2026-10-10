package com.neusoft.yiyang.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 床位示意图中的一个房间（一个房间卡片 + 房间内所有床位）。
 *
 * <p>GET /bed/map?floor=1 → {code:200, data:[{roomNo:101, beds:[...]}, ...]}</p>
 */
@Data
@Schema(description = "房间及其床位列表")
public class RoomBedsVO {

    @Schema(description = "房间号", example = "101")
    private Integer roomNo;

    @Schema(description = "该房间下的床位列表")
    private List<BedVO> beds = new ArrayList<>();
}
