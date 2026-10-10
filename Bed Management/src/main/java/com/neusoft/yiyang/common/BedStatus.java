package com.neusoft.yiyang.common;

/**
 * 床位状态常量（与数据库 {@code bed.bed_status} 字段对应）。
 *
 * <p>需求约定：1 = 空闲，2 = 有人，3 = 外出。</p>
 */
public interface BedStatus {

    /** 空闲（床位示意图显示绿色） */
    int FREE = 1;

    /** 有人（床位示意图显示蓝色） */
    int OCCUPIED = 2;

    /** 外出（床位示意图显示橙色） */
    int OUT = 3;
}
