/**
 * 
 */
package com.fssy.shareholder.management.socket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 前后端交互的类实现消息的接收推送(自己发送给自己)
 * 
 * @ServerEndpoint(value = "/test/one") 前端通过此URI和后端交互，建立连接
 * @author Solomon
 */
@ServerEndpoint(value = "/test/one/{userId}")
@Component
public class OneWebSocket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(OneWebSocket.class);
	
	/**
	 * 记录当前在线连接数
	 */
	public static AtomicInteger onlineCount = new AtomicInteger(
			0);

	/** concurrent包的线程安全Set，用来存放每个客户端对应的OneWebSocket对象。 */
	private static ConcurrentHashMap<String, OneWebSocket> webSocketMap = new ConcurrentHashMap<>();

	/** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
	private Session session;

	/** 接收userId */
	private String userId = "";

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session,
			@PathParam("userId") String userId) {
		System.out.println(session);
		this.session = session;
		this.userId = userId;
		if (webSocketMap.containsKey(userId)) {
			webSocketMap.remove(userId);
			webSocketMap.put(userId, this);
			// 加入set中
		} else {
			webSocketMap.put(userId, this);
			// 加入set中
			onlineCount.incrementAndGet(); // 在线数加1
		}
		System.out.println("有新连接加入：" + session.getId()
				+ "，当前在线人数为：" + onlineCount.get());
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(Session session) {
		if (webSocketMap.containsKey(userId)) {
			webSocketMap.remove(userId);
			// 从set中删除
			onlineCount.decrementAndGet(); // 在线数减1
		}
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 *            客户端发送过来的消息
	 */
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session) {
		// this.sendMessage("Hello, " + message, session);
		System.out.println("用户消息:" + userId + ",报文:"
				+ message);
		// 可以群发消息
		// 消息保存到数据库、redis
		if (!ObjectUtils.isEmpty(message)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				// 解析发送的报文
//				JSONObject jsonObject = JSON.parseObject(
//						message);
//				String str = mapper.readValue(message, new TypeReference()
//				{
//				});
//				// 追加发送人(防止串改)
//				jsonObject.put("fromUserId", this.userId);
//				String toUserId = jsonObject.getString(
//						"toUserId");
//				// 传送给对应toUserId用户的websocket
//				if (!ObjectUtils.isEmpty(toUserId)
//						&& webSocketMap.containsKey(
//								toUserId)) {
//					webSocketMap.get(toUserId).sendMessage(
//							jsonObject.toJSONString());
//				} else {
//					System.out.println("请求的userId:"
//							+ toUserId + "不在该服务器上");
//					// 否则不在这个服务器上，发送到mysql或者redis
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("用户错误:" + this.userId + ",原因:"
				+ error.getMessage());
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message)
			throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 发送自定义消息
	 * 
	 * @param message 消息内容
	 * @param userId 指定的用户表主键，为null或者空串时，表示所有已经连接的用户
	 * @throws IOException
	 */
	public static void sendInfo(String message,
			@PathParam("userId") String userId)
			throws IOException {
		System.out.println("发送消息到:" + userId + "，报文:"
				+ message);
		for (String key : webSocketMap.keySet()) {
			System.out.println("map的键值：" + key);
			System.out.println("map的值：" + webSocketMap
					.get(key));
			// 不指定范围时，发到所有连接的客户端
			if (ObjectUtils.isEmpty(userId))
			{
				webSocketMap.get(key).sendMessage(message);
			}
			// 指定范围时，只发到范围中的客户端
			else
			{
				if (userId.equals(key)) // 后期可以修改成列表范围
				{
					webSocketMap.get(key).sendMessage(message);
				}
			}
		}
	}
}
