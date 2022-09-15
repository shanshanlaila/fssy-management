package com.fssy.shareholder.management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fssy.shareholder.management.tools.mybatisplus.injector.MySqlInjector;

/**
 * mybatisplus配置类
 * 
 * @author Solomon
 */
@Configuration
public class MybatisPlusConfiguration
{
	/**
	 * 分页拦截器配置类
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
	}
	
	/**
	 * 自定义sql注入器
	 * @return
	 */
	@Bean
	public MySqlInjector mySqlInjector()
	{
		return new MySqlInjector();
	}
}
