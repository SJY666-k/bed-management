package com.neusoft.yiyang.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一响应结果。
 *
 * <p>前端约定：{@code res.data.code === 200} 时取 {@code res.data.data} 作为业务数据。
 * 业务失败时后端会返回 HTTP 400，并把 {@code msg} 放在响应体里，前端
 * {@code error.response.data.msg} 就能直接弹出提示。</p>
 *
 * @param <T> 业务数据类型
 */
@Data
@Schema(description = "统一响应结果")
public class Result<T> {

    @Schema(description = "业务状态码，200 表示成功", example = "200")
    private Integer code;

    @Schema(description = "提示信息", example = "操作成功")
    private String msg;

    @Schema(description = "业务数据")
    private T data;

    public Result() {
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /** 成功，无数据 */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, "操作成功", null);
    }

    /** 成功，带数据 */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, "操作成功", data);
    }

    /** 成功，带消息与数据 */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultCode.SUCCESS, msg, data);
    }

    /** 失败（默认 500） */
    public static <T> Result<T> error(String msg) {
        return new Result<>(ResultCode.ERROR, msg, null);
    }

    /** 失败（自定义状态码） */
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }
}
