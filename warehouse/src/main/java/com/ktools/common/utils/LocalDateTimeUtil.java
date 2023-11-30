package com.ktools.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lsl
 * @version 1.0
 * @date 2023年06月19日 19:04
 */
public class LocalDateTimeUtil {

    /**
     * 时间格式
     */
    private static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    private static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YYYY_MM = "yyyy-MM";
    private static final String YYYY = "yyyy";

    /**
     * String 类型转成 LocalDateTime ,必须为完整时间,如：2020-01-20 00:00:00
     *
     * @param timeStr
     * @return {@link LocalDateTime}
     * @author lsl
     * @date 2023/6/19 19:05
     */
    public static LocalDateTime parse(String timeStr) {
        return parse(timeStr, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * LocalDateTime 转完整 String 类型的时间 如：2020-01-20 00:00:00
     *
     * @param time
     * @return {@link String}
     * @author lsl
     * @date 2023/6/19 21:16
     */
    public static String parse(LocalDateTime time) {
        return parse(time, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * LocalDateTime 转指定类型的字符串
     *
     * @param time
     * @param pattern
     * @return {@link String}
     * @author lsl
     * @date 2023/6/19 21:16
     */
    public static String parse(LocalDateTime time, String pattern) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return df.format(time);
    }

    /**
     * String (2020-01-20 00:00:00)类型转成 LocalDateTime
     *
     * @param timeStr
     * @param pattern
     * @return {@link LocalDateTime}
     * @author lsl
     * @date 2023/6/19 19:05
     */
    public static LocalDateTime parse(String timeStr, String pattern) {
        if (pattern.equals(YYYY)) {
            timeStr += "-01-01 00:00:00";
        } else if (pattern.equals(YYYY_MM)) {
            timeStr += "-01 00:00:00";
        } else if (pattern.equals(YYYY_MM_DD)) {
            timeStr += " 00:00:00";
        } else if (pattern.equals(YYYY_MM_DD_HH)) {
            timeStr += ":00:00";
        } else if (pattern.equals(YYYY_MM_DD_HH_MM)) {
            timeStr += ":00";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return LocalDateTime.parse(timeStr, dtf);
    }

}
