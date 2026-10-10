package com.neusoft.yiyang.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 床位示意图中的一个小方块。
 *
 * <p>前端 BedMap.vue 使用 bed.id 作为 :key、bed.bedNo 显示床号、
 * bed.bedStatus 决定方块颜色（1 绿、2 蓝、3 橙）。</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "床位示意图小方块")
public class BedVO {

    @Schema(description = "床位 ID", example = "1")
    private Integer id;

    @Schema(description = "床位编号（房间内第几号床）", example = "1")
    private String bedNo;

    @Schema(description = "床位状态：1 空闲、2 有人、3 外出", example = "1")
    private Integer bedStatus;
}
