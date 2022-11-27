/**
 * ------------------------修改日志---------------------------------
 * 修改人    修改日期                   修改内容
 * 兰宇铧    2021-11-29 	    开放login的登录方法，否则永远执行一次登录请求
 *
 * 修改人    修改日期                   修改内容
 * 兰宇铧    2021-12-01 	    添加重复登录的踢人操作，由application.yml的kickout做为开关，默认开启
 *
 * 修改人    修改日期                   修改内容
 * 兰宇铧    2022-03-30 	    修改session过期时间为两个小时,默认为30分钟
 */
package com.fssy.shareholder.management.configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.shiro.KickoutSessionControlFilter;
import com.fssy.shareholder.management.tools.shiro.LogoutSessionControllerFilter;
import com.fssy.shareholder.management.tools.shiro.ShiroMatcher;
import com.fssy.shareholder.management.tools.shiro.ShiroRealm;
import com.fssy.shareholder.management.tools.shiro.ShiroSessionManager;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import net.sf.ehcache.CacheManager;

/**
 * shiro配置类
 *
 * @author Solomon
 */
@Configuration
public class ShiroConfiguration
{
	/**
	 * 是否采用重复登录开关
	 */
	@Value("${business.kickout:false}")
	private String kickout;

	/**
	 * 自定义口令认证器
	 *
	 * @return
	 */
	@Bean("CredentialsMatcher")
	public CredentialsMatcher credentialsMatcher()
	{
		return new ShiroMatcher();
	}

	/**
	 * 配置重写的realm
	 *
	 * @return
	 */
	@Bean("ShiroRealm")
	public ShiroRealm myShiroRealm(
			@Qualifier("CredentialsMatcher") CredentialsMatcher credentialsMatcher)
	{
		ShiroRealm shiroRealm = new ShiroRealm();
		// 设置自定义口令认证器
		shiroRealm.setCredentialsMatcher(credentialsMatcher);
		// 设置缓存名称
		/* 允许缓存 */
		shiroRealm.setCachingEnabled(true);

        /* 允许认证缓存 */
		shiroRealm.setAuthenticationCachingEnabled(true);
		shiroRealm.setAuthenticationCacheName(CommonConstant.CACHE_AUTHENTICATION);

        /* 允许授权缓存 */
		shiroRealm.setAuthorizationCachingEnabled(true);
		shiroRealm.setAuthorizationCacheName(CommonConstant.CACHE_AUTHORIZATION);
		return shiroRealm;
	}

	/**
	 * 由于使用了spring-boot-shiro-start这个bean与realm冲突
	 *
	 * @return
	 */
	@Bean
	public Authenticator authenticator()
	{
		return new ModularRealmAuthenticator();
	}

	/**
	 * 由于使用了spring-boot-shiro-start这个bean与realm冲突
	 *
	 * @return
	 */
	@Bean
	public Authorizer authorizer()
	{
		return new ModularRealmAuthorizer();
	}

	/**
	 * ehcache管理
	 *
	 * @param cacheManager
	 * @return
	 */
	@Bean("EhCacheManager")
	public EhCacheManager ehCacheManager(CacheManager cacheManager)
	{
		EhCacheManager em = new EhCacheManager();
		// 将ehcacheManager转换成shiro包装后的ehcacheManager对象
		em.setCacheManager(cacheManager);
		// em.setCacheManagerConfigFile("classpath:ehcache.xml");
		return em;
	}

	@Bean("SessionDAO")
	public SessionDAO sessionDAO(
			@Qualifier("EhCacheManager") EhCacheManager ehCacheManager)
	{
		EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
		// 使用ehcache
		enterpriseCacheSessionDAO.setCacheManager(ehCacheManager);
		// 设置session缓存的名字 默认为 shiro-activeSessionCache，不用设置
//		enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		return enterpriseCacheSessionDAO;
	}

	/**
	 * 自定义session管理
	 *
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager sessionManager(
			@Qualifier("EhCacheManager") EhCacheManager ehCacheManager)
	{
		ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
		shiroSessionManager.setCacheManager(ehCacheManager);
		// 这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
		shiroSessionManager.setSessionDAO(sessionDAO(ehCacheManager));
		// 此设置，可以避免URL中出现JSessionId
		shiroSessionManager.setSessionIdUrlRewritingEnabled(false);
		// 2022-03-30 修改session过期时间为两个小时
		shiroSessionManager.setGlobalSessionTimeout(1000L * 60 * 60 * 2);
		return shiroSessionManager;
	}

	/**
	 * 配置realm和缓存管理器到安全管理控制类中
	 *
	 * @param shiroRealm     自定义的realm
	 * @param ehCacheManager ehcache的缓存
	 * @return
	 */
	@Bean("SecurityManager")
	public SecurityManager securityManager(
			@Qualifier("ShiroRealm") ShiroRealm shiroRealm,
			@Qualifier("EhCacheManager") EhCacheManager ehCacheManager)
	{
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm);
		// 重新设置shiro的缓存管理器
		securityManager.setCacheManager(ehCacheManager);
		// 自定义session管理
		securityManager.setSessionManager(sessionManager(ehCacheManager));
		return securityManager;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator()
	{
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	/**
	 * 开启shiro aop注解支持 使用代理方式所以需要开启代码支持
	 * 一定要写入上面advisorAutoProxyCreator（）自动代理。不然AOP注解不会生效
	 *
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("SecurityManager") SecurityManager securityManager)
	{
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 配置过滤器 设置对应的过滤条件和跳转条件
	 *
	 * @param securityManager 管理控制
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(
			@Qualifier(value = "SecurityManager") SecurityManager securityManager)
	{
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 设置登录路径
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 设置首页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		Map<String, String> map = new LinkedHashMap<String, String>();//2022-11-25将HashMap更改为了LinkedHashMap有序map
		// 设置登出路径，不用额外写退出的控制器
		//2022-11-25 OA访问过来忽略
		map.put("/oaLogin", "anon");
		// 2021-11-29 开放login的登录方法，否则永远执行一次登录请求
		map.put("/login", "anon");
		map.put("/logout", "logout");
		// 静态资源不认证
		map.put("/css/**", "anon");
		map.put("/image/**", "anon");
		map.put("/js/**", "anon");
		map.put("/plugin/**", "anon");
		// 对所有路径认证
		map.put("/**", "authc");
		// 对业务请求需要进入kickout
		map.put("/manage/**", "authc,kickout");
		map.put("/system/**", "authc,kickout");
		// 积木报表认证
		map.put("/jmreport/**", "anon");
		// 设置报错页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		// 2021-12-01 添加重复登录的踢人操作，由application.yml的kickout做为开关，默认开启
		// 添加踢出重复登入时，前面的用户
		Map<String, Filter> filterMap = new HashMap<>();
		filterMap.put("kickout", kickoutSessionControlFilter(securityManager));
		filterMap.put("logout", logoutSessionControllerFilter(securityManager));
		shiroFilterFactoryBean.setFilters(filterMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 配置使用前台的shiro标签
	 */
	@Bean
	public ShiroDialect shiroDialect()
	{
		return new ShiroDialect();
	}

	/**
	 * 创建重复登录控制过滤器
	 *
	 * @param securityManager 管理控制
	 * @return
	 */
	public KickoutSessionControlFilter kickoutSessionControlFilter(SecurityManager securityManager)
	{
		CachingSecurityManager cachingSecurityManager = (CachingSecurityManager) securityManager;
		return new KickoutSessionControlFilter(kickout, cachingSecurityManager.getCacheManager());
	}

	/**
	 * 创建自定义退出登录过滤器
	 *
	 * @param securityManager 管理控制
	 * @return
	 */
	public LogoutSessionControllerFilter logoutSessionControllerFilter(
			SecurityManager securityManager)
	{
		CachingSecurityManager cachingSecurityManager = (CachingSecurityManager) securityManager;
		return new LogoutSessionControllerFilter(cachingSecurityManager.getCacheManager());
	}
}
