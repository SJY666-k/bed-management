package com.neusoft.yiyang.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 修改床位使用结束时间（对应「床位管理」页面右下角的「修改」按钮弹窗）。
 *
 * <p>PUT /bed/manage/updateEndTime，例如：{"id": 1, "endDate": "2025-12-31"}</p>
 */
@Data
@Schema(description = "修改床位使用结束时间")
public class UpdateEndTimeDTO {

    @NotNull(message = "床位使用详情ID不能为空")
    @Schema(description = "床位使用详情 ID（beddetails.id）", example = "1")
    private Integer id;

    @Schema(description = "结束时间，格式 yyyy-MM-dd", example = "2025-12-31")
    private String endDate;
}
