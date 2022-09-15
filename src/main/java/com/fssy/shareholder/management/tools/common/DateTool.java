/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.tools.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.springframework.stereotype.Component;

/**
 * @Title: DateTool.java
 * @Description: 日期工具类  
 * @author Solomon  
 * @date 2021年11月12日 下午6:12:46 
 */
@Component
public class DateTool
{
	/**
	 * 获取上个自然月日期字符串
	 * 
	 * @return
	 */
	public static String getLastMonthString()
	{
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
	public static String getTodayString()
	{
		Calendar calendar = Calendar.getInstance(Locale.CHINA); // 当天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}

	/**
	 * 获取当月第一天日期字符串
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonthString()
	{
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
	public static String getLastDayOfTodayString()
	{
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
	public static String getLastWeekOfTodayString()
	{
		Calendar calendar = Calendar.getInstance(Locale.CHINA); // 当天
		calendar.add(Calendar.DATE, -7); // 上周
//		calendar.set(Calendar.DAY_OF_MONTH, 0); // 减一天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(calendar.getTime());
	}
}
