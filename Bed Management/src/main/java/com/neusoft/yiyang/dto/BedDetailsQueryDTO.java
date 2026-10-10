package com.neusoft.yiyang.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 床位使用详情查询条件（对应「床位管理」页面的查询表单）。
 *
 * <p>GET /bed/manage/details?status=1&amp;customerName=张&amp;startDate=2025-01-01</p>
 */
@Data
@Schema(description = "床位使用详情查询条件")
public class BedDetailsQueryDTO {

    @Schema(description = "使用状态：1 正在使用、0 使用历史", example = "1")
    private Integer status;

    @Schema(description = "客户姓名（模糊查询）", example = "张")
    private String customerName;

    @Schema(description = "入住日期（查询该日期及之后入住的记录）", example = "2025-01-01")
    private String startDate;
}
