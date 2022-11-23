/**
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.tools.common;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;


/**
 * @author Solomon
 * @Title: DateTool.java
 * @Description: 日期工具类
 * @date 2021年11月12日 下午6:12:46
 */
@Component
public class DateTool {
    /**
     * 获取上个自然月日期字符串
     *
     * @return
     */
    public static String getLastMonthString() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.MONTH, -1); // 上个月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当天日期字符串
     *
     * @return
     */
    public static String getTodayString() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA); // 当天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当月第一天日期字符串
     *
     * @return
     */
    public static String getFirstDayOfMonthString() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.MONTH, 0); // 当月
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当月最后一天日期字符串
     *
     * @return
     */
    public static String getLastDayOfTodayString() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA); // 当天
        calendar.add(Calendar.MONTH, 1); // 下月
        calendar.set(Calendar.DAY_OF_MONTH, 0); // 减一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取上周日期字符串
     *
     * @return
     */
    public static String getLastWeekOfTodayString() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA); // 当天
        calendar.add(Calendar.DATE, -7); // 上周
//		calendar.set(Calendar.DAY_OF_MONTH, 0); // 减一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime());
    }

    /**
     * 转换字符串为日期（格式为yyyy-MM-dd)
     *
     * @param dateString 日期字符串
     * @return
     */
    public static Date transferToDate(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 转换字符串为日期（格式为yyyy-MM-dd HH:mm:ss)
     *
     * @param dateString 日期字符串
     * @return
     */
    public static Date transferTimeToDate(String timeString) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate localDate = LocalDate.parse(timeString, dtf);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 将 Date 转为 LocalDate
     *
     * @param date
     * @return java.time.LocalDate;
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 获取两个日期的天数的差别：后-前
     *
     * @param dateStart 前
     * @param dateStop  后
     * @return
     */
    public static Integer computeDiffDays(Date dateStart, Date dateStop) {
        try {
            long diff = dateStop.getTime() - dateStart.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            return InstandTool.longToInteger(diffDays);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当月最后一天日期字符串
     *
     * @return
     */
    public static String getLastDayOfTodayString(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA); // 当天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 设置日期为传入年和月的第一天
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, 1); // 下月
        calendar.set(Calendar.DAY_OF_MONTH, 0); // 减一天
        return sdf.format(calendar.getTime());
    }
}
