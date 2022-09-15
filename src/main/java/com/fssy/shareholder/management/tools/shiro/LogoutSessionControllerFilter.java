/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.tools.shiro;

import java.io.Serializable;
import java.util.Deque;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * @Title: LogoutSessionControllerFilter.java
 * @Description: 退出登录时需要对重复登录的缓存清除
 * @author Solomon
 * @date 2021年11月30日 下午5:02:26
 */
public class LogoutSessionControllerFilter extends LogoutFilter
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LogoutSessionControllerFilter.class);

	private Cache<User, Deque<Session>> cache;

	public LogoutSessionControllerFilter(CacheManager cacheManager)
	{
		super();
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	public void setCacheManager(CacheManager cacheManager)
	{
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
	{
		// 需要删除shiro-kickout-session缓存中对应的session
		Subject subject = getSubject(request, response);

		// 获取当前session
		Session session = subject.getSession();
		// 获取当前登录用户
		User user = (User) subject.getPrincipal();
		// 获取当前sessionId
		Serializable sessionId = session.getId();
		LOGGER.debug("logout session id = " + sessionId);

		synchronized (this.cache)
		{
			Deque<Session> deque = cache.get(user);
			Session removeSession = null;
			if (!ObjectUtils.isEmpty(deque))
			{
				for (Session sessionInqueue : deque)
				{
					LOGGER.debug("deque sessionId = " + sessionInqueue.getId());
					if (sessionId.equals(sessionInqueue.getId()))
					{
						removeSession = sessionInqueue;
						break;
					}
				}
				if (!ObjectUtils.isEmpty(removeSession))
				{
					deque.remove(removeSession);
				}
			}
		}

		return super.preHandle(request, response);
	}

}
