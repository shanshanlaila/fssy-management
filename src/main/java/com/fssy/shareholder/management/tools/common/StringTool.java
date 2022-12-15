/**
 * 
 */
package com.fssy.shareholder.management.tools.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 字符串操作工具类
 * 
 * @author Solomon
 */
@Component
public class StringTool
{
	
	/**
	 * 返回查找字符串中指定字符串出现的次数
	 * @param srcText 查询字符串
	 * @param findText 指定字符串
	 * @return
	 */
	public static int appearNumber(String srcText, String findText) {
		int count = 0;
	    int index = 0;
	    while ((index = srcText.indexOf(findText, index)) != -1) {
	        index = index + findText.length();
	        count++;
	    }
	    return count;
	}
	
	/**
	 * 文件名不允许字符转换方法
	 * 
	 * @param fileStr 文件名字符串
	 * @param destStr 替换字符串
	 * @return
	 */
	public static String replaceFilenameWrongCharToDestination(String fileStr, String destStr)
	{
		return fileStr.replaceAll("[/\\\\:*?|]", destStr);
	}

	/**
	 * 拼接信息
	 * 
	 * @param sb      字符串
	 * @param content 拼接字符串
	 * @return 字符串
	 */
	public static StringBuffer setMsg(StringBuffer sb, String content)
	{
		if (ObjectUtils.isEmpty(sb))
		{
			sb.append(content);
		}
		else
		{
			sb.append(";" + content);
		}
		return sb;
	}

	/**
	 * 取消右边空白
	 *
	 * @param str
	 * @return
	 */
	public static String rightTrim(String str)
	{
		String regex = "(.*\\S+)(\\s+$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.matches())
		{
			str = m.group(1);
		}
		return str;
	}
}
