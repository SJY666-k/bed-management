package com.neusoft.yiyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 床位详细信息实体（对应数据库 {@code beddetails} 表）。
 *
 * <p>数据库设计文档中 beddetails 表的字段：id、start_date（开始时间）、end_date（结束时间）、
 * bed_details（床位详情）、customer_id（客户 id）、bed_id（床位 id）、is_deleted（是否删除）。</p>
 *
 * <p>注意：这里的 {@code is_deleted} 不是 MyBatis-Plus 的逻辑删除标记，而是业务状态
 * （0 = 正在使用，1 = 已失效的历史记录），因此没有使用 {@code @TableLogic}。</p>
 */
@Data
@TableName("beddetails")
@Schema(description = "床位使用详情实体")
public class BedDetails {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键 ID")
    private Integer id;

    @Schema(description = "开始时间（入住时间）")
    private LocalDate startDate;

    @Schema(description = "结束时间（退住时间），正在使用时为 null")
    private LocalDate endDate;

    @Schema(description = "床位详情描述，如：606栋-1层-101房间-1号床位")
    private String bedDetails;

    @Schema(description = "客户 ID，对应 customer.id")
    private Integer customerId;

    @Schema(description = "床位 ID，对应 bed.id")
    private Integer bedId;

    @Schema(description = "0 正在使用；1 已失效（使用历史）")
    private Integer isDeleted;
}
