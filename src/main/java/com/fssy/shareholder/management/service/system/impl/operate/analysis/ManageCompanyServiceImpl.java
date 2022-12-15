package com.fssy.shareholder.management.service.system.impl.operate.analysis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.operate.analysis.ManageCompanyMapper;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;
import com.fssy.shareholder.management.service.system.operate.analysis.ManageCompanyService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 授权访问的公司代码列表 *****数据表名：
 * bs_operate_manage_company *****数据表作用： 经营分析可以授权访问的公司列表 *****变更记录： 时间 变更人 变更内容
 * 20221213 兰宇铧 初始设计 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Service
public class ManageCompanyServiceImpl extends ServiceImpl<ManageCompanyMapper, ManageCompany>
		implements ManageCompanyService
{
	Logger LOGGER = LoggerFactory.getLogger(ManageCompanyServiceImpl.class);
	
	/**
	 * 授权访问的公司代码列表数据访问实现类
	 */
	@Autowired
	private ManageCompanyMapper manageCompanyMapper;
	
	@Value(value = "${business.finance.system.username}")
	private String uid;
	
	@Value(value = "${business.finance.system.password}")
	private String pwd;

	/**
	 * 用户数据访问实现类
	 */
	@Autowired
	private UserMapper userMapper;

	@Override
	public List<ManageCompany> findManageCompanyDataByParams(Map<String, Object> params)
	{
		QueryWrapper<ManageCompany> queryWrapper = getQueryWrapper(params);
		return manageCompanyMapper.selectList(queryWrapper);
	}

	@Override
	public Page<ManageCompany> findManageCompanyDataPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<ManageCompany> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<ManageCompany> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return manageCompanyMapper.selectPage(myPage, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> findManageCompanyMapDataByParams(Map<String, Object> params)
	{
		QueryWrapper<ManageCompany> queryWrapper = getQueryWrapper(params);
		return manageCompanyMapper.selectMaps(queryWrapper);
	}

	@Override
	public Page<Map<String, Object>> findManageCompanyMapPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<ManageCompany> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return manageCompanyMapper.selectMapsPage(myPage, queryWrapper);
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<ManageCompany> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建query
		QueryWrapper<ManageCompany> queryWrapper = Wrappers.query();
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		if (params.containsKey("ids"))
		{
			queryWrapper.in("id", (List<String>) params.get("ids"));
		}
		if (params.containsKey("codeEq"))
		{
			queryWrapper.eq("code", params.get("codeEq"));
		}
		if (params.containsKey("code"))
		{
			queryWrapper.like("code", params.get("code"));
		}
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		if (params.containsKey("select"))
		{
			queryWrapper.select(InstandTool.objectToString(params.get("select")));
		}
		if (params.containsKey("groupBy"))
		{
			queryWrapper.select(InstandTool.objectToString(params.get("groupBy")));
		}
		if (params.containsKey("idDesc"))
		{
			queryWrapper.orderByDesc("id");
		}
		// endregion
		return queryWrapper;
	}

	@Override
	@Transactional
	public Map<String, Object> receiveDataByArtificial(Map<String, Object> params)
	{
		return receiveData(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> receiveData(Map<String, Object> params)
	{
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("msg", "操作成功");
		
		// region 添加参数
		Map<String, Object> transmitParams = new HashMap<>();
		transmitParams.put("uid", uid);
		transmitParams.put("pwd", pwd);
		transmitParams.put("report", "gsdm");
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
		HttpPost httpPost1 = new HttpPost("http://192.168.30.232/cw/json/json_value.asp");
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
		List<Map<String, Object>> resultDataList = null;
		try
		{
			response = client.execute(httpPost1);
			String responseDataStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			try
			{
				Map<String, Object> parseObject = JSON.parseObject(responseDataStr, Map.class);
				resultDataList = (List<Map<String, Object>>) parseObject.get("data");
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
		
		// region 添加数据到数据库
		if (!ObjectUtils.isEmpty(resultDataList))
		{
			// 查询系统中已经存在的公司
			QueryWrapper<ManageCompany> queryWrapper = new QueryWrapper<>();
			List<ManageCompany> manageCompanies = manageCompanyMapper.selectList(queryWrapper);
			// region 系统自动任务时，user默认为张三（系统管理员）
			User user;
			if (ObjectUtils.isEmpty(SecurityUtils.getSubject())
					|| ObjectUtils.isEmpty(SecurityUtils.getSubject().getPrincipal()))
			{
				user = userMapper.selectById(1L);
			}
			else
			{
				user = (User) SecurityUtils.getSubject().getPrincipal();
			}
			// endregion
			UpdateWrapper<ManageCompany> updateWrapper;

			// region 通过对接数据比较已有数据
			List<String> manageCompanyCodeStrings = IteratorTool.pluck("code", manageCompanies);
			Map<String, ManageCompany> manageCompanyKeyBy = IteratorTool.keyByPattern("code",
					manageCompanies);
			for (Map<String, Object> transmitData : resultDataList)
			{
				// 判断如果公司代码不存在，需要添加公司
				String companyCode = InstandTool.objectToString(transmitData.get("公司代码"));
				if (!manageCompanyCodeStrings.contains(companyCode))
				{
					ManageCompany manageCompany = new ManageCompany();
					manageCompany.setNote("");
					manageCompany.setCode(companyCode);
					manageCompany.setName(InstandTool.objectToString(transmitData.get("公司名称")));
					manageCompany.setStatus(CommonConstant.COMMON_STATUS_STRING_ACTIVE);
					manageCompany.setActiveDate(LocalDate.now());
					manageCompany.setInactiveDate(null);
					manageCompanyMapper.insert(manageCompany);
				}
				else // 如果公司代码存在，需要判断是否启用，停用时需要重新启用
				{
					ManageCompany tempCompany = manageCompanyKeyBy.get(companyCode);
					if (CommonConstant.COMMON_STATUS_STRING_INACTIVE
							.equals(tempCompany.getStatus()))
					{
						updateWrapper = new UpdateWrapper<>();
						updateWrapper.eq("id", tempCompany.getId())
								.set("status", CommonConstant.COMMON_STATUS_STRING_ACTIVE)
								.set("activeDate", LocalDate.now()).set("inactiveDate", null)
								.set("updatedId", user.getId()).set("updatedName", user.getName())
								.set("updatedAt", LocalDateTime.now());
						manageCompanyMapper.update(null, updateWrapper);
					}
				}
			}
			// endregion

			// region 通过已有数据找对接数据
			List<String> transmitCodes = IteratorTool.pluck("公司代码", resultDataList);
			for (ManageCompany manageCompany : manageCompanies)
			{
				if (!transmitCodes.contains(manageCompany.getCode()))
				{
					updateWrapper = new UpdateWrapper<>();
					updateWrapper.eq("id", manageCompany.getId())
							.set("status", CommonConstant.COMMON_STATUS_STRING_INACTIVE)
							.set("inactiveDate", LocalDate.now()).set("updatedId", user.getId())
							.set("updatedName", user.getName())
							.set("updatedAt", LocalDateTime.now());
					manageCompanyMapper.update(null, updateWrapper);
				}
			}
			// endregion
		}
		// endregion
		
		return result;
	}

	@Override
	public List<Map<String, Object>> findManageCompanySelectedDataListByParams(
			Map<String, Object> params, List<String> selectedIds)
	{
		QueryWrapper<ManageCompany> queryWrapper = getQueryWrapper(params);
		List<ManageCompany> dataList = manageCompanyMapper.selectList(queryWrapper);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的用户角色添加selected属性
		Map<String, Object> result;
		for (ManageCompany data : dataList)
		{
			result = new HashMap<String, Object>();
			result.put("name", data.getCode() + "|" + data.getName());
			result.put("value", data.getId());
			result.put("id", data.getId());
			result.put("companyName", data.getName());
			result.put("code", data.getCode());
			boolean selected = false;
			for (int i = 0; i < selectedIds.size(); i++)
			{
				if (selectedIds.get(i).equals(data.getId().toString()))
				{
					selected = true;
					break;
				}
			}
			result.put("selected", selected);
			resultList.add(result);
		}
		return resultList;
	}
}
