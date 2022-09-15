/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.socket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @Title: KickoutNoticeWebSocket.java
 * @Description: 被登录踢出通信
 * @author Solomon
 * @date 2021年12月1日 下午12:33:32
 */
@ServerEndpoint(value = "/kickout/{shiroSessionId}")
@Component
public class KickoutNoticeWebSocket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(KickoutNoticeWebSocket.class);

	/**
	 * 记录当前在线连接数
	 */
	public static AtomicInteger onlineCount = new AtomicInteger(0);

	/** concurrent包的线程安全Set，用来存放每个客户端对应的OneWebSocket对象。 */
	private static ConcurrentHashMap<String, KickoutNoticeWebSocket> webSocketMap = new ConcurrentHashMap<>();

	/** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
	private Session session;

	/** 接收sessionId */
	private String shiroSessionId = "";

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("shiroSessionId") String shiroSessionId)
	{
		LOGGER.debug("sessionId : " + session.getId());
		this.session = session;
		this.shiroSessionId = shiroSessionId;
		if (webSocketMap.containsKey(shiroSessionId))
		{
			webSocketMap.remove(shiroSessionId);
			webSocketMap.put(shiroSessionId, this);
			// 加入set中
		}
		else
		{
			webSocketMap.put(shiroSessionId, this);
			// 加入set中
			onlineCount.incrementAndGet(); // 在线数加1
		}
		LOGGER.debug("有新连接加入：" + session.getId() + "，当前在线人数为：" + onlineCount.get());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session)
	{
		if (webSocketMap.containsKey(shiroSessionId))
		{
			webSocketMap.remove(shiroSessionId);
			// 从set中删除
			onlineCount.decrementAndGet(); // 在线数减1
			System.out.println("有连接退出：" + session.getId()
			+ "，当前在线人数为：" + onlineCount.get());
		}
	}

	@OnError
	public void onError(Session session, Throwable error)
	{
		LOGGER.debug("会话错误:" + this.shiroSessionId + ",原因:" + error.getMessage());
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) throws IOException
	{
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 发送自定义消息
	 * 
	 * @param message        消息内容
	 * @param shiroSessionId 指定的shrio会话id，为null或者空串时，表示所有已经连接的用户
	 * @throws IOException
	 */
	public static void sendInfo(String message, @PathParam("shiroSessionId") String shiroSessionId)
			throws IOException
	{
		LOGGER.debug("发送消息到:" + shiroSessionId + "，报文:" + message);
		for (String key : webSocketMap.keySet())
		{
			LOGGER.debug("map的键值：" + key);
			LOGGER.debug("map的值：" + webSocketMap.get(key));
			// 不指定范围时，发到所有连接的客户端
			if (ObjectUtils.isEmpty(shiroSessionId))
			{
				webSocketMap.get(key).sendMessage(message);
			}
			// 指定范围时，只发到范围中的客户端
			else
			{
				if (shiroSessionId.equals(key)) // 后期可以修改成列表范围
				{
					webSocketMap.get(key).sendMessage(message);
				}
			}
		}
	}
}
