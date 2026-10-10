package com.neusoft.yiyang.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 床位调换参数（对应「床位管理」页面右下角的「床位调换」按钮弹窗）。
 *
 * <p>需求要求：<b>只能是当天调换当天办理</b>，因此 swapDate 会与服务器当天日期比对。</p>
 */
@Data
@Schema(description = "床位调换参数")
public class BedSwapDTO {

    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户 ID（customer.id）", example = "1")
    private Integer customerId;

    @Schema(description = "新楼层：1 一层、2 二层", example = "1")
    private String newFloor;

    @Schema(description = "新房间号（room.room_no）", example = "102")
    private Integer newRoomNo;

    @NotNull(message = "请选择要调换的空闲床位")
    @Schema(description = "新床位 ID（bed.id）", example = "5")
    private Integer newBedId;

    @Schema(description = "调换日期，格式 yyyy-MM-dd", example = "2025-10-10")
    private String swapDate;
}
