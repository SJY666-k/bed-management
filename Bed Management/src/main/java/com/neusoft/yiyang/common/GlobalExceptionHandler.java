package com.neusoft.yiyang.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 *
 * <p>为了让前端可以用 {@code try/catch} 区分成功与失败，这里统一返回非 2xx 的 HTTP 状态码：
 * <ul>
 *     <li>业务异常 → HTTP 400 + msg</li>
 *     <li>其它异常 → HTTP 500</li>
 * </ul>
 * 前端的 {@code catch (error) { ElMessage.error(error.response.data.msg) }} 即可直接使用。</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常：如「只能调换到空闲的床位」 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return ResponseEntity.badRequest().body(Result.error(ResultCode.BAD_REQUEST, e.getMessage()));
    }

    /** 参数校验异常：如 DTO 上的 @NotNull 不通过 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("参数校验失败");
        log.warn("参数校验失败：{}", msg);
        return ResponseEntity.badRequest().body(Result.error(ResultCode.BAD_REQUEST, msg));
    }

    /** 兜底异常 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("系统异常，请稍后重试"));
    }
}
