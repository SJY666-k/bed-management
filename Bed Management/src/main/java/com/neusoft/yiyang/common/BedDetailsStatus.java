package com.neusoft.yiyang.common;

/**
 * 床位使用记录状态常量（与数据库 {@code beddetails.is_deleted} 字段对应）。
 *
 * <p>需求约定：入住时新增一条 {@code is_deleted = 0} 的记录；退住 / 调换时把旧记录改为
 * {@code is_deleted = 1}（逻辑失效），前端「使用历史」页签展示的就是这些失效记录。</p>
 */
public interface BedDetailsStatus {

    /** beddetails.is_deleted = 0：这条使用记录正在生效 */
    int IN_USE = 0;

    /** beddetails.is_deleted = 1：这条使用记录已结束（使用历史） */
    int HISTORY = 1;

    /**
     * 前端「床位管理」页签取值：1 = 正在使用。
     * 注意它与 {@code is_deleted} 的取值刚好相反（页签 1 对应 is_deleted = 0）。
     */
    int TAB_IN_USE = 1;

    /** 前端「床位管理」页签取值：0 = 使用历史（对应 is_deleted = 1） */
    int TAB_HISTORY = 0;
}
