package com.fssy.shareholder.management.interceptor;

import com.fssy.shareholder.management.tools.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 判断请求返回类型拦截器
 * 用于错误信息是json还是错误页面
 * @author Solomon
 */
@Component
public class ExceptionPreHandleInterceptor implements HandlerInterceptor
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExceptionPreHandleInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception
	{
		LOGGER.info(handler.toString());
		if (!(handler instanceof HandlerMethod))
		{
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		// 获取拦截器请求方法
		Method method = handlerMethod.getMethod();
		// 请求方法返回值为字段串，可能是页面路径
		boolean flag1 = method.getReturnType().equals(String.class);
		// 请求方法未使用注解@ResponseBody，可能是页面路径
		boolean flag2 = !method.isAnnotationPresent(ResponseBody.class);
		// 请求方法所在的Controller未使用@RestController注解，可能是页面路径
		boolean flag3 = !handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
		// 三者同时满足则代表返回的是页面
		// 设置标识符在request对象
		request.setAttribute(CommonConstant.METHOD_RETURN_IS_VIEW, flag1 && flag2 && flag3);
		return true;
	}

}
