package com.neusoft.yiyang.common;

/**
 * 业务异常。
 *
 * <p>Service 层校验不通过时抛出，由 {@link GlobalExceptionHandler} 统一转换成
 * HTTP 400 + {@code {code:400, msg:"...", data:null}} 返回给前端。</p>
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
