package com.neusoft.yiyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * 客户（老人）实体（对应数据库 {@code customer} 表）。
 *
 * <p>床位管理模块只关心其中一部分字段：customer_name、customer_sex、room_no、
 * building_no、bed_id、is_deleted；其余字段按数据库设计文档一并保留，方便后续
 * 「客户管理 / 入住登记」模块直接复用。</p>
 */
@Data
@TableName("customer")
@Schema(description = "客户（老人）实体")
public class Customer {

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键 ID")
    private Integer id;

    @Schema(description = "是否删除：0 未删除、1 已删除")
    private Integer isDeleted;

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "年龄")
    private Integer customerAge;

    @Schema(description = "性别：0 男、1 女")
    private Integer customerSex;

    @Schema(description = "身份证号")
    private String idcard;

    @Schema(description = "房间号")
    private String roomNo;

    @Schema(description = "所属楼房")
    private String buildingNo;

    @Schema(description = "入住时间")
    private LocalDate checkinDate;

    @Schema(description = "合同到期时间")
    private LocalDate expirationDate;

    @Schema(description = "联系电话")
    private String contactTel;

    @Schema(description = "床号，对应 bed.id")
    private Integer bedId;

    @Schema(description = "身心状况")
    private String psychosomaticState;

    @Schema(description = "注意事项")
    private String attention;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "身高")
    private String height;

    @Schema(description = "体重")
    private String weight;

    @Schema(description = "血型")
    private String bloodType;

    @Schema(description = "档案文件路径")
    private String filepath;

    @Schema(description = "所属用户 ID")
    private Integer userId;

    @Schema(description = "护理等级 ID")
    private Integer levelId;

    @Schema(description = "家属信息")
    private String familyMember;
}
