package com.neusoft.yiyang.common;

/**
 * 统一业务状态码。
 *
 * <p>前端约定：{@code code == 200} 表示业务成功。</p>
 */
public interface ResultCode {

    /** 业务成功 */
    Integer SUCCESS = 200;

    /** 参数错误、数据不存在、状态冲突等业务失败（与 HTTP 400 对应） */
    Integer BAD_REQUEST = 400;

    /** 系统异常 */
    Integer ERROR = 500;
}
