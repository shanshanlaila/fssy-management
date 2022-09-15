package com.fssy.shareholder.management.tools.shiro;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义session获取方式</br>
 * 这里采用ajax请求头authToken携带sessionId的方式
 * 
 * @author Solomon
 */
public class ShiroSessionManager extends DefaultWebSessionManager
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultWebSessionManager.class);

	private static final String AUTHORIZATION = "authToken";

	private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

	public ShiroSessionManager()
	{
		super();
	}

	@Override
	protected Serializable getSessionId(ServletRequest request,
			ServletResponse response)
	{
		String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
		// 如果是微信小程序请求的，直接把请求头包含的cookie值即sessionid返回，不用做后面的操作
		if ("true".equals(WebUtils.toHttp(request).getHeader("isWx")) && !ObjectUtils.isEmpty(WebUtils.toHttp(request).getHeader("cookie"))) {
			try {
				Serializable id1 = WebUtils.toHttp(request).getHeader("cookie").split("JSESSIONID=")[1];
				return id1;
			} catch (Exception e) {

			}
		}
		LOGGER.info("使用api时的id：" + id);
		if (ObjectUtils.isEmpty(id))
		{
			// 如果没有携带id参数则按照父类的方式在cookie进行获取
			LOGGER.info("super：" + super.getSessionId(request, response));
			return super.getSessionId(request, response);
		}
		else
		{
			// 如果请求头中有 authToken 则其值为sessionId
			request.setAttribute(
					ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
					REFERENCED_SESSION_ID_SOURCE);
			request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID,
					id);
			request.setAttribute(
					ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID,
					Boolean.TRUE);
			return id;
		}

	}
}
