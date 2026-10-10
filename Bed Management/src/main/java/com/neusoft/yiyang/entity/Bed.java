package com.neusoft.yiyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 床位实体（对应数据库 {@code bed} 表）。
 *
 * <p>数据库设计文档中 bed 表的字段：id、room_no（房间编号）、bed_status（床位状态）、
 * remarks（备注）、bed_no（床位编号）。</p>
 */
@Data
@TableName("bed")
@Schema(description = "床位实体")
public class Bed {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键 ID")
    private Integer id;

    @Schema(description = "房间编号，对应 room.room_no", example = "101")
    private Integer roomNo;

    @Schema(description = "床位状态：1 空闲、2 有人、3 外出", example = "1")
    private Integer bedStatus;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "床位编号（房间内第几号床）", example = "1")
    private String bedNo;
}
