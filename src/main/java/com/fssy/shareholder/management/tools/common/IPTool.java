/**
 * 
 */
package com.fssy.shareholder.management.tools.common;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * IP地址相关工具类</br>
 * 
 * @author Solomon
 */
public class IPTool
{
	private static final Logger LOGGER = LoggerFactory.getLogger(IPTool.class);

	/**
	 * 获取IP地址 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr()
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String ip = null;
		try
		{
			ip = request.getHeader("x-forwarded-for");
			if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ObjectUtils.isEmpty(ip) || ip.length() == 0
					|| "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ObjectUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip))
			{
				ip = request.getRemoteAddr();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("IPUtils ERROR ", e);
		}

		return ip;
	}
}
