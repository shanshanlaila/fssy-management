package com.fssy.shareholder.management.tools.shiro;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.fssy.shareholder.management.socket.KickoutNoticeWebSocket;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.pojo.manage.user.User;

/**
 * @Title: KickoutSessionControlFilter.java
 * @Description: 重复登录的过滤
 * @author Solomon
 * @date 2021年11月29日 下午6:23:17
 */
public class KickoutSessionControlFilter extends AccessControlFilter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(KickoutSessionControlFilter.class);

	// 采用new的方式创建
//	@Value("${business.kickout:false}")
	private String kickout;

	private String kickoutUrl = "/login";
	private boolean kickoutAfter = false;
	private int maxSession = 1;
	private Cache<User, Deque<Session>> cache;

	public KickoutSessionControlFilter(String kickout, CacheManager cacheManager)
	{
		super();
		this.kickout = kickout;
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	public void setKickoutUrl(String kickoutUrl)
	{
		this.kickoutUrl = kickoutUrl;
	}

	public void setKickoutAfter(boolean kickoutAfter)
	{
		this.kickoutAfter = kickoutAfter;
	}

	public void setMaxSession(int maxSession)
	{
		this.maxSession = maxSession;
	}

	@Autowired
	public void setCacheManager(CacheManager cacheManager)
	{
		this.cache = cacheManager.getCache("shiro-kickout-session");
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
			Object mappedValue) throws Exception
	{
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws Exception
	{
		Subject subject = getSubject(request, response);
		// 具体操作
		if ("true".equals(kickout))
		{
			// 如果不需要单用户登录的限制
			return true;
		}

		// 获取当前session
		Session session = subject.getSession();
		// 获取当前登录用户
		User user = (User) subject.getPrincipal();
		// 获取sessionId
		Serializable sessionId = session.getId();

		if (ObjectUtils.isEmpty(cache) || ObjectUtils.isEmpty(user))
		{
			return true;
		}

		// 如果当前session已经有重复登录标识，直接在操作时退出，并重定向至登录页面，不要再放入队列
		if (session.getAttribute("kickout") != null)
		{
			try
			{
				subject.logout();
			}
			catch (Exception e)
			{
			}
			saveRequest(request);
			// 重定向到登录页面
			WebUtils.issueRedirect(request, response, kickoutUrl);
			return false;
		}

		synchronized (this.cache)
		{
			Deque<Session> deque = cache.get(user);

			if (ObjectUtils.isEmpty(deque))
			{
				deque = new LinkedList<Session>();
				cache.put(user, deque);
			}

			// 如果队列 里没有此登录用户，且用户没有被踢出，放入队列
			boolean whetherPutDeque = true;
			if (deque.isEmpty())
			{
				whetherPutDeque = true;
			}
			else
			{
				for (Session sessionInqueue : deque)
				{
					LOGGER.debug("deque sessionId = " + sessionInqueue.getId());
					if (sessionId.equals(sessionInqueue.getId()))
					{
						whetherPutDeque = false;
						break;
					}
				}
			}

			if (whetherPutDeque)
			{
				deque.push(session);
			}

			LOGGER.debug(String.format("logged user: %s,deque size = %s", user.getAccount(),
					deque.size()));
			LOGGER.debug("deque = " + deque.toArray());

			// 如果队列里的sessionId数超出最大会话数，开始踢人
			while (deque.size() > maxSession)
			{
				Session kickoutSession = null;
				if (kickoutAfter) // 踢出后者
				{
					kickoutSession = deque.removeFirst();
					LOGGER.debug(
							String.format("踢出后登录的，被踢出的sessionId为： %s", kickoutSession.getId()));
				}
				else
				{ // 踢出前者
					kickoutSession = deque.removeLast();
					LOGGER.debug(
							String.format("踢出先登录的，被踢出的sessionId为： %s", kickoutSession.getId()));
				}
				// 踢的动作
				if (kickoutSession != null)
				{
					kickoutSession.setAttribute("kickout", true);
					// 通知被踢出的用户
					KickoutNoticeWebSocket.sendInfo("你已经在其他地方登录，请检查密码是否泄露",
							kickoutSession.getId().toString());
				}
			}

		}

		return true;
	}

}
