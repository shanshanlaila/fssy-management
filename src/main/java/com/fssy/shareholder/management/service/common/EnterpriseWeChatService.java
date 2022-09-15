/**
 * 
 */
package com.fssy.shareholder.management.service.common;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.cp.bean.messagebuilder.TextBuilder;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;

/**
 * 企业微信对接业务类
 * 
 * @author Solomon
 */
@Service
public class EnterpriseWeChatService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseWeChatService.class);
	
	/** 用于发送消息的企业微信应用ID */
	public static final Integer AGENT_ID = 1000017;

	/** 企业微信的appId,通过配置文件获取 */
	@Value(value = "${wechat.corpId}")
	public String corpId;

	/** 企业微信的secretId，通过配置文件获取 */
	@Value(value = "${wechat.corpSecret}")
	public String corpSecret;
	
	/**
	 * 向企业微信推送消息
	 * 
	 * @param content 消息内容文本
	 * @param userList 推送用户账号列表，多个用户之间使用|连接
	 * @param tagList 推送用户标签列表，备选
	 * @param partyList 推送用户群体列表，备选
	 * @throws WxErrorException
	 */
	public void push(String content, List<String> userList, List<String> tagList, List<String> partyList) throws WxErrorException
	{
		WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
		config.setCorpId(corpId); // 设置微信企业号的appid
		config.setCorpSecret(corpSecret); // 设置微信企业号的app corpSecret
		config.setAgentId(AGENT_ID); // 设置微信企业号应用ID

		WxCpServiceImpl wxCpService = new WxCpServiceImpl();
		wxCpService.setWxCpConfigStorage(config);

		TextBuilder builder = WxCpMessage.TEXT().agentId(AGENT_ID).content(content);
		// 如果存在多个用户，用户之间使用|分开
		if (!ObjectUtils.isEmpty(userList))
		{
			String toUser = userList.stream().collect(Collectors.joining("|"));
			builder.toUser(toUser);
		}
		if (!ObjectUtils.isEmpty(tagList))
		{
			String toTag = tagList.stream().collect(Collectors.joining("|"));
			builder.toTag(toTag);
		}
		if (!ObjectUtils.isEmpty(partyList))
		{
			String toParty = partyList.stream().collect(Collectors.joining("|"));
			builder.toParty(toParty);
		}
		WxCpMessage message = builder.build();
		wxCpService.getMessageService().send(message);
		LOGGER.debug("accessToken===" + wxCpService.getAccessToken());
	}
}
