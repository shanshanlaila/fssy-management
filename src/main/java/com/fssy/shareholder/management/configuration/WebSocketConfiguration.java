/**
 * 
 */
package com.fssy.shareholder.management.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类
 * 
 * @author Solomon
 *
 */
@Configuration
public class WebSocketConfiguration
{
	/**
	 * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket
	 * endpoint
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter()
	{
		return new ServerEndpointExporter();
	}
}
