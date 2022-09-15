package com.fssy.shareholder.management.configuration;

import com.fssy.shareholder.management.interceptor.ExceptionPreHandleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc需要配置的一些bean对象
 * 
 * @author Solomon
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer
{
	@Autowired
    ExceptionPreHandleInterceptor exceptionPreHandleInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		// 全局异常处理 前置处理拦截器
		registry.addInterceptor(exceptionPreHandleInterceptor).addPathPatterns("/**");
	}
	
}
