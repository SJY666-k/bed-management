package com.neusoft.yiyang.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 床位使用详情行数据（对应「床位管理」页面的表格一行）。
 *
 * <p>字段名必须与前端 BedManage.vue 的 el-table-column / 弹窗保持一致：
 * customerName、customerSex、bedDetails、startDate、endDate、customerId、id。</p>
 */
@Data
@Schema(description = "床位使用详情")
public class BedDetailsVO {

    @Schema(description = "床位使用详情 ID（beddetails.id），修改结束时间时使用", example = "1")
    private Integer id;

    @Schema(description = "客户 ID（customer.id），床位调换时使用", example = "1")
    private Integer customerId;

    @Schema(description = "客户姓名", example = "张伟")
    private String customerName;

    @Schema(description = "性别：0 男、1 女", example = "0")
    private Integer customerSex;

    @Schema(description = "床位详情", example = "606栋-1层-101房间-2号床位")
    private String bedDetails;

    @Schema(description = "入住日期", example = "2025-03-01")
    private LocalDate startDate;

    @Schema(description = "结束日期（正在使用时为 null）", example = "2025-12-31")
    private LocalDate endDate;
}
