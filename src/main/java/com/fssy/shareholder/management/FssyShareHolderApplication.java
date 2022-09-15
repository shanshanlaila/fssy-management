package com.fssy.shareholder.management;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import com.fssy.shareholder.management.pojo.properties.FileProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

@SpringBootApplication
@EnableCaching
@MapperScan("com.fssy.shareholder.management.mapper")
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@EnableConfigurationProperties({
	FileProperties.class
})
public class FssyShareHolderApplication {
	
	/**
	 * 设置全局时区
	 */
	@PostConstruct
    void started() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(timeZone);
    }

	public static void main(String[] args) {
		SpringApplication.run(FssyShareHolderApplication.class, args);
	}

}
