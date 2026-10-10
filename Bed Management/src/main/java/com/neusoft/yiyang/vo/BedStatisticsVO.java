package com.neusoft.yiyang.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 床位统计结果（对应「床位示意图」页面顶部的四个标签）。
 *
 * <p>GET /bed/statistics → {code:200, data:{total:48, free:40, occupied:6, out:2}}</p>
 */
@Data
@Schema(description = "床位统计结果")
public class BedStatisticsVO {

    @Schema(description = "总床位数", example = "48")
    private Integer total;

    @Schema(description = "空闲床位数量", example = "40")
    private Integer free;

    @Schema(description = "有人（已入住）床位数量", example = "6")
    private Integer occupied;

    @Schema(description = "外出床位数量", example = "2")
    private Integer out;
}
