package com.neusoft.yiyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 房间实体（对应数据库 {@code room} 表）。
 *
 * <p>数据库设计文档中 room 表的字段：id、room_floor（房间楼层）、room_no（房间编号）。
 * 床位示意图就是「按楼层找房间 → 再按房间找床位」的过程。</p>
 */
@Data
@TableName("room")
@Schema(description = "房间实体")
public class Room {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键 ID")
    private Integer id;

    @Schema(description = "房间楼层：1 一层、2 二层", example = "1")
    private String roomFloor;

    @Schema(description = "房间编号（全楼唯一，如 101、201）", example = "101")
    private Integer roomNo;
}
