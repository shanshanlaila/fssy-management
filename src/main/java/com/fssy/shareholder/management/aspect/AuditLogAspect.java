/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2021-12-30 	         修改审计日志，参数记录过长，只从请求实体的参数中写入
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-04 		 修改问题，记录的参数是句柄，使用getParameter方法
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-12-19 		修改为从上下文获取request，而不从jp中参数获得;修改问题，throw异常会导致事务回滚，错误日志无法记录，此处需要根据request对象中获取拦截器中设置的标识符返回
 */
package com.fssy.shareholder.management.aspect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.log.AuditLogMapper;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.log.AuditLog;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.tools.common.IPTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

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

	@Autowired
	private DepartmentRoleUserMapper departmentRoleUserMapper;

	// 定义一个切点，作用于加了RequiredLog注解的方法上
	@Pointcut("@annotation(com.fssy.shareholder.management.annotation.RequiredLog)")
	public void doLogPoint()
	{
	}

	@Around(value = "doLogPoint()")
	@Transactional(rollbackFor = Exception.class)
	public Object doAround(ProceedingJoinPoint jp) throws Throwable
	{
		// 执行目标方法
		Object result = null;
		// 前置通知
		long t1 = System.currentTimeMillis();
		// 设置回滚点,只回滚以下异常
		Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			// 启动切点的目标方法
			result = jp.proceed();
			// 后置通知
			long t2 = System.currentTimeMillis();
			// 将日志信息(用户成功行为日志)写入到数据库
			try
			{
				saveLog(jp, (t2 - t1), 1, "");
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
			// 手工回滚异常,回滚到savePoint
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
			Map<String, Object> resultMap = new HashMap<>();
			try
			{
				// 这里的日志被回滚
				resultMap = saveLog(jp, (t2 - t1), 0, e.getMessage());
			}
			catch (NoSuchMethodException | SecurityException e1)
			{
				// 记录日志错误不影响业务
				e1.printStackTrace();
				LOGGER.error(e1.toString());
			}
			// 2022-12-19 修改问题，throw异常会导致事务回滚，错误日志无法记录，此处需要根据request对象中获取拦截器中设置的标识符返回
			// 不同情况
			Object viewFlag = resultMap.get("viewFlag");
			// 判空
			if (ObjectUtils.isEmpty(viewFlag))
			{
				throw e;
			}
			boolean isView = (Boolean) viewFlag;

			Integer judgeSystem = 0;
			
			// 查找用户当前的角色是否为供应商
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			if (!ObjectUtils.isEmpty(user))
			{
				Boolean isSupplier = false;
				QueryWrapper<DepartmentRoleUser> querySupplierWrapper = new QueryWrapper<>();
				querySupplierWrapper.eq("roleId", CommonConstant.ROLE_SUPPLIER).eq("userId",
						user.getId());
				List<DepartmentRoleUser> supplierList = departmentRoleUserMapper
						.selectList(querySupplierWrapper);
				if (supplierList.size() > 0)
				{
					isSupplier = true;
					judgeSystem = 3;
				}

				if (!isSupplier)
				{
					// 查找用户当前的角色，是否为管理员
					Boolean isAdministrator = false;
					QueryWrapper<DepartmentRoleUser> queryWrapper = new QueryWrapper<>();
					queryWrapper.eq("roleId", 1).eq("userId", user.getId());
					List<DepartmentRoleUser> judgeList = departmentRoleUserMapper
							.selectList(queryWrapper);
					if (judgeList.size() > 0)
					{
						isAdministrator = true;
					}

					if (isAdministrator)
					{
						judgeSystem = 1;
					}
					else
					{
						judgeSystem = 2;
					}
				}
			}
			
			if (isView)
			{
				// 返回错误页面
				ModelAndView modelAndView = new ModelAndView("error");// 配置错误页面
				if (e instanceof ServiceException)
				{
					modelAndView.getModel().put("msg", e.getMessage());
				}
				else
				{
					modelAndView.getModel().put("msg", "系统错误，请联系管理员");
				}
				modelAndView.getModel().put("judgeSystem", judgeSystem);
				return modelAndView;
			}
			else
			{
				if (e instanceof ServiceException)
				{
					// 返回json对象
					return SysResult.build(500, e.getMessage());
				}
				else
				{
					return SysResult.build(500, "系统错误，请联系管理员");
				}
			}
		}
		return result;
	}

	// 启用新的事务不能记录
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Map<String, Object> saveLog(ProceedingJoinPoint jp, long time, int status, String note)
			throws NoSuchMethodException, SecurityException
	{
		Map<String, Object> result = new HashMap<>();
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
		// 2022-12-19 修改为从上下文获取request，而不从jp中参数获得
		// 1.10 从request取参数
		String params = null;
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder
				.currentRequestAttributes())).getRequest();
		// 从requst中取
		// 2021-12-30 修改审计日志，参数记录过长，只从请求实体的参数中写入
		Object viewFlag = request.getAttribute(CommonConstant.METHOD_RETURN_IS_VIEW);
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
				params = params + ";" + String.format("%s:%s", key, request.getParameter(key));
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
		log.setNote(note);
		// 写入表
		auditLogMapper.insert(log);
		
		result.put("viewFlag", viewFlag);
		return result;
	}
}
