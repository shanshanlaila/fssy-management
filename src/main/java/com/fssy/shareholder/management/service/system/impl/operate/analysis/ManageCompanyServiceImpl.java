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
 * *****??????????????? ??????????????? *****????????????????????? ????????????????????????????????? *****???????????????
 * bs_operate_manage_company *****?????????????????? ????????????????????????????????????????????? *****??????????????? ?????? ????????? ????????????
 * 20221213 ????????? ???????????? ???????????????
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
	 * ??????????????????????????????????????????????????????
	 */
	@Autowired
	private ManageCompanyMapper manageCompanyMapper;
	
	@Value(value = "${business.finance.system.username}")
	private String uid;
	
	@Value(value = "${business.finance.system.password}")
	private String pwd;

	/**
	 * ???????????????????????????
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
		// region ??????query
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
		if (params.containsKey("basicCompanyId"))
		{
			queryWrapper.eq("basicCompanyId", params.get("basicCompanyId"));
		}
		if (params.containsKey("basicCompanyIds"))
		{
			queryWrapper.in("basicCompanyId", (List<String>) params.get("basicCompanyIds"));
		}
		if (params.containsKey("code"))
		{
			queryWrapper.like("code", params.get("code"));
		}
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		if (params.containsKey("shortName"))
		{
			queryWrapper.like("shortName", params.get("shortName"));
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
		result.put("msg", "????????????");
		
		// region ????????????
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
		
		// region ??????client???????????????
		// ??????HTTP??????????????????
		CloseableHttpClient client = HttpClients.createDefault();
		// ?????????HPPT post??????
		HttpPost httpPost1 = new HttpPost("http://192.168.30.232/cw/json/json_value.asp");
		// ??????????????????
		// ????????????????????????
		// ???????????????entiry, ???????????????Json??????
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
			throw new ServiceException("?????????????????????????????????");
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
		
		// region ????????????????????????
		if (!ObjectUtils.isEmpty(resultDataList))
		{
			// ????????????????????????????????????
			QueryWrapper<ManageCompany> queryWrapper = new QueryWrapper<>();
			List<ManageCompany> manageCompanies = manageCompanyMapper.selectList(queryWrapper);
			// region ????????????????????????user????????????????????????????????????
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

			// region ????????????????????????????????????
			List<String> manageCompanyCodeStrings = IteratorTool.pluck("code", manageCompanies);
			Map<String, ManageCompany> manageCompanyKeyBy = IteratorTool.keyByPattern("code",
					manageCompanies);
			for (Map<String, Object> transmitData : resultDataList)
			{
				// ??????????????????????????????????????????????????????
				String companyCode = InstandTool.objectToString(transmitData.get("????????????"));
				if (!manageCompanyCodeStrings.contains(companyCode))
				{
					ManageCompany manageCompany = new ManageCompany();
					manageCompany.setNote("");
					manageCompany.setCode(companyCode);
					manageCompany.setName(InstandTool.objectToString(transmitData.get("????????????")));
					manageCompany.setStatus(CommonConstant.COMMON_STATUS_STRING_ACTIVE);
					manageCompany.setActiveDate(LocalDate.now());
					manageCompany.setInactiveDate(null);
					manageCompanyMapper.insert(manageCompany);
				}
				else // ?????????????????????????????????????????????????????????????????????????????????
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

			// region ?????????????????????????????????
			List<String> transmitCodes = IteratorTool.pluck("????????????", resultDataList);
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
		// ??????????????????????????????selected??????
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

	@Override
	@Transactional
	public Map<String, Object> update(ManageCompany manageCompany)
	{
		// region ????????????
		QueryWrapper<ManageCompany> checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("code", manageCompany.getCode()).ne("id", manageCompany.getId());
		if (manageCompanyMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(
					String.format("????????????%s?????????????????????????????????????????????", manageCompany.getCode()));
		}

		checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("name", manageCompany.getName()).ne("id", manageCompany.getId());
		if (manageCompanyMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(
					String.format("????????????%s?????????????????????????????????????????????", manageCompany.getName()));
		}
		// endregion
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		manageCompanyMapper.updateById(manageCompany);
		return result;
	}

	@Override
	public ManageCompany findDataById(String id)
	{
		return manageCompanyMapper.selectById(id);
	}
}
