package com.neusoft.yiyang.util;

import com.neusoft.yiyang.common.BusinessException;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类。
 *
 * <p>前端可能传来 {@code 2025-10-10}，也可能传来带时区的
 * {@code 2025-10-10T16:00:00.000Z}，这里统一截取前 10 位再解析。</p>
 */
public final class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtils() {
    }

    /**
     * 把前端传来的日期字符串标准化成 {@code yyyy-MM-dd}；空值返回 {@code null}。
     */
    public static String normalize(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return null;
        }
        String trimmed = dateStr.trim();
        return trimmed.length() > 10 ? trimmed.substring(0, 10) : trimmed;
    }

    /**
     * 把前端传来的日期字符串解析成 {@link LocalDate}；空值返回 {@code null}。
     *
     * @throws BusinessException 格式不合法时抛出
     */
    public static LocalDate parse(String dateStr) {
        String normalized = normalize(dateStr);
        if (normalized == null) {
            return null;
        }
        try {
            return LocalDate.parse(normalized, FORMATTER);
        } catch (Exception e) {
            throw new BusinessException("日期格式不正确，正确格式为 yyyy-MM-dd：" + dateStr);
        }
    }

    /**
     * 解析日期并校验非空。
     */
    public static LocalDate parseRequired(String dateStr, String fieldName) {
        LocalDate date = parse(dateStr);
        if (date == null) {
            throw new BusinessException(fieldName + "不能为空");
        }
        return date;
    }
}
