/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-12-01 	      处理日志打印，包含信息有报错类名，方法，行号
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-27 	      获取当前用户时，需要判断空，有可能报错时未登录
 */
package com.fssy.shareholder.management.tools.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fssy.shareholder.management.tools.constant.CommonConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * 统一异常处理类(全局)
 * 
 * @author Solomon
 */
@ControllerAdvice
public class ControllerExceptionHandler
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ControllerExceptionHandler.class);


    @Autowired
    private DepartmentRoleUserMapper departmentRoleUserMapper;
	
	/**
	 * 业务异常处理
	 * 
	 * @param e 业务异常
	 * @return ModelAndView 视图模型
	 */
	@ExceptionHandler(ServiceException.class)
	public ModelAndView handleServiceException(ServiceException e,
			HttpServletRequest request)
	{
		// 从request对象中获取拦截器中设置的标识符
		Object object = request
				.getAttribute(CommonConstant.METHOD_RETURN_IS_VIEW);
		// 判空
		if (object == null)
		{
			throw e;
		}

		e.printStackTrace();
		LOGGER.error(e.toString());
		boolean isView = (Boolean) object;
		
        Integer judgeSystem = 0;
		
		// 查找用户当前的角色是否为供应商
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (!ObjectUtils.isEmpty(user))
		{
			Boolean isSupplier = false;
			QueryWrapper<DepartmentRoleUser> querySupplierWrapper = new QueryWrapper<>();
			querySupplierWrapper.eq("roleId", CommonConstant.ROLE_SUPPLIER)
			.eq("userId",user.getId());
			List<DepartmentRoleUser> supplierList = departmentRoleUserMapper.selectList(querySupplierWrapper);
			if(supplierList.size() > 0){
				isSupplier = true;
				judgeSystem = 3;
			}
			
			if(!isSupplier){
				// 查找用户当前的角色，是否为管理员
				Boolean isAdministrator = false;
				QueryWrapper<DepartmentRoleUser> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("roleId", 1).eq("userId", user.getId());
				List<DepartmentRoleUser> judgeList = departmentRoleUserMapper.selectList(queryWrapper);
				if (judgeList.size() > 0)
				{
					isAdministrator = true;
				}
				
				if (isAdministrator)
				{
					judgeSystem = 1;
				} else
				{
					judgeSystem = 2;
				}
			}
		}
		
		if (isView)
		{
			// 返回错误页面
			ModelAndView modelAndView = new ModelAndView("error");// 配置错误页面
			modelAndView.getModel().put("msg", e.getMessage());
			modelAndView.getModel().put("judgeSystem", judgeSystem);
			return modelAndView;
		}
		else
		{
			// 返回json对象
			ModelAndView modelAndView = new ModelAndView(
					new MappingJackson2JsonView());
			modelAndView.addObject("status", "500");
			modelAndView.addObject("msg", e.getMessage());
			modelAndView.addObject("data", null);
			return modelAndView;
		}
	}
	
	/**
	 * 业务异常处理
	 * 
	 * @param e 验证异常
	 * @return ModelAndView 视图模型
	 */
	@ExceptionHandler(BindException.class)
	public ModelAndView handleBindException(BindException e, HttpServletRequest request)
	{
		// 从request对象中获取拦截器中设置的标识符
		Object object = request
				.getAttribute(CommonConstant.METHOD_RETURN_IS_VIEW);
		// 判空
		if (object == null)
		{
			throw new ServiceException("json标识为空，验证异常无法抛出");
		}

		e.printStackTrace();
		LOGGER.error(e.toString());
		boolean isView = (Boolean) object;
		
        Integer judgeSystem = 0;
		
		// 查找用户当前的角色是否为供应商
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (!ObjectUtils.isEmpty(user))
		{
			Boolean isSupplier = false;
			QueryWrapper<DepartmentRoleUser> querySupplierWrapper = new QueryWrapper<>();
			querySupplierWrapper.eq("roleId", CommonConstant.ROLE_SUPPLIER)
			.eq("userId",user.getId());
			List<DepartmentRoleUser> supplierList = departmentRoleUserMapper.selectList(querySupplierWrapper);
			if(supplierList.size() > 0){
				isSupplier = true;
				judgeSystem = 3;
			}
			
			if(!isSupplier){
				// 查找用户当前的角色，是否为管理员
				Boolean isAdministrator = false;
				QueryWrapper<DepartmentRoleUser> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("roleId", 1).eq("userId", user.getId());
				List<DepartmentRoleUser> judgeList = departmentRoleUserMapper.selectList(queryWrapper);
				if (judgeList.size() > 0)
				{
					isAdministrator = true;
				}
				
				if (isAdministrator)
				{
					judgeSystem = 1;
				} else
				{
					judgeSystem = 2;
				}
			}
		}
		
		if (isView)
		{
			// 返回错误页面
			ModelAndView modelAndView = new ModelAndView("error");// 配置错误页面
			modelAndView.getModel().put("msg", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
			modelAndView.getModel().put("judgeSystem", judgeSystem);
			return modelAndView;
		}
		else
		{
			// 返回json对象
			ModelAndView modelAndView = new ModelAndView(
					new MappingJackson2JsonView());
			modelAndView.addObject("status", "500");
			modelAndView.addObject("msg", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
			modelAndView.addObject("data", null);
			return modelAndView;
		}
	}

	@ExceptionHandler(ShiroException.class)
	public ModelAndView handleShiroException(ShiroException e,
			HttpServletRequest request)
	{
		// 从request对象中获取拦截器中设置的标识符
		Object object = request
				.getAttribute(CommonConstant.METHOD_RETURN_IS_VIEW);
		// 判空
		if (object == null)
		{
			throw e;
		}

		e.printStackTrace();
		LOGGER.error(doMsg(e));
		boolean isView = (Boolean) object;
		
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
			modelAndView.getModel().put("judgeSystem", judgeSystem);
			if (e instanceof UnauthorizedException)
			{
				modelAndView.getModel().put("msg", "没有权限访问");
			}
			else
			{
				modelAndView.getModel().put("msg", e.getMessage());
			}
			return modelAndView;
		}
		else
		{
			// 返回json对象
			ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
			modelAndView.addObject("status", "500");
			modelAndView.addObject("data", null);
			if (e instanceof UnauthorizedException)
			{
				modelAndView.addObject("msg", "没有权限访问");
			}
			else
			{
				modelAndView.addObject("msg", e.getMessage());
			}
			return modelAndView;
		}
	}

	/**
	 * 通用异常处理
	 * 
	 * @param e       通用异常，根据202状态码，转到错误页面
	 * @param model   视图模型
	 * @param request 请求实体
	 * @return ModelAndView 视图模型
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e, Model model,
			HttpServletRequest request)
	{
		// 从request对象中获取拦截器中设置的标识符
		Object object = request
				.getAttribute(CommonConstant.METHOD_RETURN_IS_VIEW);
		// 判空
		if (object == null)
		{
			// 返回json对象
			ModelAndView modelAndView = new ModelAndView(
					new MappingJackson2JsonView());
			modelAndView.addObject("status", "500");
			modelAndView.addObject("msg", "系统错误，请联系管理员");
			modelAndView.addObject("data", null);
			return modelAndView;
		}

		e.printStackTrace();
		// 2021-12-01 这里处理一下500错误的日志打印，包含信息有报错类名，方法，行号
		LOGGER.error(doMsg(e));

		Integer judgeSystem = 0;
		
		// 查找用户当前的角色是否为供应商
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (!ObjectUtils.isEmpty(user))
		{
			Boolean isSupplier = false;
			QueryWrapper<DepartmentRoleUser> querySupplierWrapper = new QueryWrapper<>();
			querySupplierWrapper.eq("roleId", CommonConstant.ROLE_SUPPLIER)
			.eq("userId",user.getId());
			List<DepartmentRoleUser> supplierList = departmentRoleUserMapper.selectList(querySupplierWrapper);
			if(supplierList.size() > 0){
				isSupplier = true;
				judgeSystem = 3;
			}
			
			if(!isSupplier){
				// 查找用户当前的角色，是否为管理员
				Boolean isAdministrator = false;
				QueryWrapper<DepartmentRoleUser> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("roleId", 1).eq("userId", user.getId());
				List<DepartmentRoleUser> judgeList = departmentRoleUserMapper.selectList(queryWrapper);
				if (judgeList.size() > 0)
				{
					isAdministrator = true;
				}
				
				if (isAdministrator)
				{
					judgeSystem = 1;
				} else
				{
					judgeSystem = 2;
				}
			}
		}
		
		boolean isView = (Boolean) object;
		if (isView)
		{
			// 返回错误页面
			ModelAndView modelAndView = new ModelAndView("error");// 配置错误页面
			modelAndView.getModel().put("msg", "系统错误，请联系管理员");
			modelAndView.getModel().put("judgeSystem", judgeSystem);
			return modelAndView;
		}
		else
		{
			// 返回json对象
			ModelAndView modelAndView = new ModelAndView(
					new MappingJackson2JsonView());
			modelAndView.addObject("status", "500");
			modelAndView.addObject("msg", "系统错误，请联系管理员");
			modelAndView.addObject("data", null);
			return modelAndView;
		}
	}
	
	/**
	 * 构建错误日志内容
	 * 
	 * @param e 异常对象
	 * @return String 内容
	 */
	private String doMsg(Exception e)
	{
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		StackTraceElement s = e.getStackTrace()[0];
		String className = s.getClassName();
		String methodName = s.getMethodName();
		int lineNumber = s.getLineNumber();
		String msg = String.format("[%s] L%s 执行方法（%s）时报错，错误信息为: %s，当前操作人为:%s", className,
				lineNumber, methodName, e.getMessage(),
				ObjectUtils.isEmpty(user) ? "" : user.getName());
		return msg;
	}
}
