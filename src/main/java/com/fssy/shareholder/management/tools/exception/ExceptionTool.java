/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.tools.exception;

import org.apache.shiro.SecurityUtils;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * @Title: ExceptionTool.java
 * @Description: 异常操作工具类 
 * @author Solomon  
 * @date 2021年12月4日 下午3:26:34 
 */
public class ExceptionTool
{
	/**
	 * 构建错误日志内容
	 * 
	 * @param e 异常对象
	 * @return String 内容
	 */
	public static String doMsg(Exception e)
	{
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		StackTraceElement s = e.getStackTrace()[0];
		String className = s.getClassName();
		String methodName = s.getMethodName();
		int lineNumber = s.getLineNumber();
		String msg = String.format("[%s] L%s 执行方法（%s）时报错，错误信息为: %s，当前操作人为:%s", className,
				lineNumber, methodName, e.toString(),
				ObjectUtils.isEmpty(user) ? "" : user.getName());
		return msg;
	}
}
