/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.tools.common;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @Title: InstandTool.java
 * @Description: 类型转换工具类  
 * @author Solomon  
 * @date 2021年11月5日 上午10:55:29 
 */
@Component
public class InstandTool
{
	public static Long integerToLong(Integer num)
	{
		if (ObjectUtils.isEmpty(num))
		{
			return 0L;
		}
		return Long.valueOf(num);
	}
	
	public static Integer stringToInteger(String num)
	{
		if (ObjectUtils.isEmpty(num) || "null".equals(num))
		{
			return 0;
		}
		return Integer.parseInt(num);
	}
	
	public static Long stringToLong(String num)
	{
		if (ObjectUtils.isEmpty(num) || "null".equals(num))
		{
			return 0L;
		}
		return Long.parseLong(num);
	}

	public static Double stringToDouble(String num)
	{
		if (ObjectUtils.isEmpty(num) || "null".equals(num))
		{
			return 0D;
		}
		return Double.parseDouble(num);
	}
	
	public static Long doubleToLong(Double num)
	{
		if (ObjectUtils.isEmpty(num))
		{
			return 0L;
		}
		Long result = (long) Math.ceil(num);
		return result;
	}
	
	public static String objectToString(Object obj)
	{
		if (ObjectUtils.isEmpty(obj))
		{
			return null;
		}
		return String.valueOf(obj);
	}
	
	public static Integer longToInteger(Long num)
	{
		if (ObjectUtils.isEmpty(num))
		{
			return 0;
		}
		return num.intValue();
	}
}
