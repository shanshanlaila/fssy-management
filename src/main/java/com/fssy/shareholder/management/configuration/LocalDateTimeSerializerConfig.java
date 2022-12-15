/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * @Title: LocalDateTimeSerializerConfig.java
 * @Description: LocalDateTime格式化配置类 
 * @author Solomon  
 * @date 2022年12月15日 下午3:22:39 
 */
@Configuration
public class LocalDateTimeSerializerConfig
{
	@Bean
	public LocalDateTimeSerializer localDateTimeSerializer()
	{
		return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer()
	{
		return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
	}
}
