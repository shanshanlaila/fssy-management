/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2021-12-30 	         修改审计日志，参数记录过长，只从请求实体的参数中写入
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-04 		 修改问题，记录的参数是句柄，使用getParameter方法
 */
package com.fssy.shareholder.management.aspect;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.tools.common.IPTool;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.mapper.manage.log.AuditLogMapper;
import com.fssy.shareholder.management.pojo.manage.log.AuditLog;
import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * 日志切面类型（基于此类进行用户行为记录）</br>
 * 
 * @author Solomon
 *
 */
@Service
@Aspect
public class AuditLogAspect
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuditLogAspect.class);

	@Autowired
	private AuditLogMapper auditLogMapper;

	// 定义一个切点，作用于加了RequiredLog注解的方法上
	@Pointcut("@annotation(com.fssy.shareholder.management.annotation.RequiredLog)")
	public void doLogPoint()
	{
	}

	@Around(value = "doLogPoint()")
	@Transactional
	public Object doAround(ProceedingJoinPoint jp) throws Throwable
	{
		// 执行目标方法
		Object result = null;
		// 前置通知
		long t1 = System.currentTimeMillis();
		try
		{
			// 启动切点的目标方法
			result = jp.proceed();
			// 后置通知
			long t2 = System.currentTimeMillis();
			// 将日志信息(用户成功行为日志)写入到数据库
			try
			{
				saveLog(jp, (t2 - t1), 1);
			}
			catch (Exception e1)
			{
				// 记录日志错误不影响业务
				e1.printStackTrace();
				LOGGER.error(e1.toString());
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			// 将日志信息(用户错误行为日志)写入到数据库
			// 错误通知
			long t2 = System.currentTimeMillis();
			try
			{
				// 这里的日志被回滚
				saveLog(jp, (t2 - t1), 0);
			}
			catch (NoSuchMethodException | SecurityException e1)
			{
				// 记录日志错误不影响业务
				e1.printStackTrace();
				LOGGER.error(e1.toString());
			}
			throw e;
		}
		return result;
	}

	// 启用新的事务不能记录
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void saveLog(ProceedingJoinPoint jp, long time, int status)
			throws NoSuchMethodException, SecurityException
	{
		// 1.获取日志信息
		// 1.1获取方法签名信息(记录了目标方法信息)
		MethodSignature s = (MethodSignature) jp.getSignature();
		// 1.2获取目标方法参数类型
		Class<?>[] parameterTypes = s.getParameterTypes();
		// 1.3获取目标方法名
		String methodName = s.getName();
		// 1.4获取目标方法对象
		Class<?> targetClass = jp.getTarget().getClass();
		Method targetMethod = targetClass.getDeclaredMethod(methodName,
				parameterTypes);
		// 1.5获取类方法信息(类名+"."+方法名)
		String method = targetClass.getName() + "." + methodName;
		// 1.6获取方法对象上注解RequiredLog
		RequiredLog requiredLog = targetMethod
				.getDeclaredAnnotation(RequiredLog.class);
		// 1.7获取注解上指定的操作名
		String operation = "";
		if (requiredLog != null)
		{
			operation = requiredLog.value();
		}
		// 1.8获取登录用户名称
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		String name = user.getName();
		// 1.9获取ip地址
		String ip = IPTool.getIpAddr();
		// 1.10 从request取参数
		String params = null;
		// 从requst中取
		for (Object object : jp.getArgs())
		{
			// 获取request
			if (object instanceof HttpServletRequest)
			{
				// 2021-12-30 修改审计日志，参数记录过长，只从请求实体的参数中写入
				HttpServletRequest request = (HttpServletRequest) object;
				Map<String, String[]> maps = request.getParameterMap();
				for (Entry<String, String[]> entry : maps.entrySet())
				{
					String key = entry.getKey();
//					String[] value = entry.getValue();
					if (ObjectUtils.isEmpty(params))
					{
						// 2022-01-04 修改问题，记录的参数是句柄，使用getParameter方法
						params = String.format("%s:%s", key, request.getParameter(key));
					}
					else
					{
						params = params + ";"
								+ String.format("%s:%s", key, request.getParameter(key));
					}
				}
				break;
			}
		}

		// 2.封装日志信息
		AuditLog log = new AuditLog();
		log.setStatus(status);
		log.setName(name);
		log.setIp(ip);
		log.setOperation(operation);
		log.setMethod(method);
		log.setParams(params);
		log.setTime(time);
		// 写入表
		auditLogMapper.insert(log);
	}
}
