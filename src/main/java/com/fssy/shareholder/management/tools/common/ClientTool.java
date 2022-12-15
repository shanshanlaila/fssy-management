/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.tools.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @Title: ClientTool.java
 * @Description: 双系统对接工具类 
 * @author Solomon  
 * @date 2022年12月15日 上午10:18:13 
 */
@Component
public class ClientTool
{
	Logger LOGGER = LoggerFactory.getLogger(ClientTool.class);
	
	/**
	 * 发送对接消息
	 * 
	 * @param transmitParams 对接参数
	 * @param url            对接系统的url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> dispatch(Map<String, Object> transmitParams, String url)
	{
		Map<String, Object> result = new HashMap<>();

		// region 添加参数
		List<NameValuePair> nvps = new ArrayList<>();
		Set<String> keySet = transmitParams.keySet();
		for (String key : keySet)
		{
			nvps.add(new BasicNameValuePair(key, transmitParams.get(key).toString()));
		}
		// endregion

		// region 创建client请求并发送
		// 创建HTTP默认的客户端
		CloseableHttpClient client = HttpClients.createDefault();
		// 实例化HPPT post请求
		HttpPost httpPost1 = new HttpPost(url);
		// 设置提交方式
		// 处理参数为字符串
		// 定义传入的entiry, 并且类型为Json格式
		try
		{
			httpPost1.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		CloseableHttpResponse response = null;
		try
		{
			response = client.execute(httpPost1);
			String responseDataStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			try
			{
				result = JSON.parseObject(responseDataStr, Map.class);
			}
			catch (Exception e)
			{
				throw new ServiceException(responseDataStr);
			}
		}
		catch (IOException e)
		{
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new ServiceException("发送到提供端时系统错误");
		}
		try
		{
			response.close();
			client.close();
		}
		catch (IOException e)
		{
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		// endregion
		return result;
	}
}
