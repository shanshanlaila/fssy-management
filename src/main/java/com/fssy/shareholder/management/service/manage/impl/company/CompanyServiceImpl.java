/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.manage.impl.company;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @author Shizn
 * @ClassName: CompanyServiceImpl
 * @Description: 公司名称业务实现类
 * @date 2022/11/17 0017 11:17
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company>
		implements CompanyService
{

	@Autowired
	private CompanyMapper companyMapper;

	/**
	 * 获取组织结构列表用于xm-select插件
	 *
	 * @param params
	 * @param selectedIds
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findCompanySelectedDataListByParams(Map<String, Object> params,
			List<String> selectedIds)
	{
		QueryWrapper<Company> queryWrapper = getQueryWrapper(params);
		List<Company> companyNameList = companyMapper.selectList(queryWrapper);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的用户角色添加selected属性
		for (Company company : companyNameList)
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("name", company.getCode() + "|" + company.getShortName());
			result.put("value", company.getId());
			result.put("id", company.getId());
			result.put("longName", company.getName());
			result.put("shortName", company.getShortName());
			result.put("code", company.getCode());
			boolean selected = false;
			for (int i = 0; i < selectedIds.size(); i++)
			{
				if (selectedIds.get(i).equals(company.getId().toString()))
				{
					selected = true;
				}
			}
			result.put("selected", selected);
			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public Page<Company> findDataPageByParams(Map<String, Object> params)
	{
		QueryWrapper<Company> queryWrapper = getQueryWrapper(params);
		Page<Company> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
		return companyMapper.selectPage(myPage, queryWrapper);
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<Company> getQueryWrapper(Map<String, Object> params)
	{
		QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
		// region 添加查询条件
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
		if (params.containsKey("status"))
		{
			queryWrapper.eq("status", params.get("status"));
		}
		if (params.containsKey("activeDateStart"))
		{
			queryWrapper.ge("activeDate", params.get("activeDateStart"));
		}
		if (params.containsKey("activeDateEnd"))
		{
			queryWrapper.le("activeDate", params.get("activeDateEnd"));
		}
		if (params.containsKey("inactiveDateStart"))
		{
			queryWrapper.ge("inactiveDate", params.get("inactiveDateStart"));
		}
		if (params.containsKey("inactiveDateEnd"))
		{
			queryWrapper.le("inactiveDate", params.get("inactiveDateEnd"));
		}
		if (params.containsKey("createdAtStart"))
		{
			queryWrapper.ge("createdAt", params.get("createdAtStart"));
		}
		if (params.containsKey("createdAtEnd"))
		{
			queryWrapper.le("createdAt", params.get("createdAtEnd"));
		}
		if (params.containsKey("updatedAtStart"))
		{
			queryWrapper.ge("updatedAt", params.get("updatedAtStart"));
		}
		if (params.containsKey("updatedAtEnd"))
		{
			queryWrapper.le("updatedAt", params.get("updatedAtEnd"));
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
		if (params.containsKey("note"))
		{
			queryWrapper.like("note", params.get("note"));
		}
		if (params.containsKey("createdName"))
		{
			queryWrapper.like("createdName", params.get("createdName"));
		}
		if (params.containsKey("updatedName"))
		{
			queryWrapper.like("updatedName", params.get("updatedName"));
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
	public List<Company> findDataListByParams(Map<String, Object> params)
	{
		QueryWrapper<Company> queryWrapper = getQueryWrapper(params);
		List<Company> companyList = companyMapper.selectList(queryWrapper);
		return companyList;
	}

	@Override
	public Page<Map<String, Object>> findDataMapPageByParams(Map<String, Object> params)
	{
		QueryWrapper<Company> queryWrapper = getQueryWrapper(params);
		Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return companyMapper.selectMapsPage(myPage, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> findDataListMapByParams(Map<String, Object> params)
	{
		QueryWrapper<Company> queryWrapper = getQueryWrapper(params);
		List<Map<String, Object>> companyList = companyMapper.selectMaps(queryWrapper);
		return companyList;
	}

	@Override
	public Company saveCompany(Company company)
	{
		// region 业务判断
		QueryWrapper<Company> checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("code", company.getCode());
		if (companyMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(String.format("代码为【%s】的公司已经存在，不能重复添加", company.getCode()));
		}

		checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("name", company.getName());
		if (companyMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(String.format("名称为【%s】的公司已经存在，不能重复添加", company.getName()));
		}
		// endregion
		company.setActiveDate(LocalDate.now());
		companyMapper.insert(company);
		return company;
	}

	@Override
	public Map<String, Object> updateCompany(Company company)
	{
		// region 业务判断
		QueryWrapper<Company> checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("code", company.getCode()).ne("id", company.getId());
		if (companyMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(String.format("代码为【%s】的公司已经存在，不能修改", company.getCode()));
		}

		checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("name", company.getName()).ne("id", company.getId());
		if (companyMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(String.format("名称为【%s】的公司已经存在，不能修改", company.getName()));
		}
		// endregion
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		companyMapper.updateById(company);
		return result;
	}

	@Override
	public Map<String, Object> activateOrInactivateCompany(int id, String active)
	{
		// region 业务判断
		// endregion
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		UpdateWrapper<Company> updateWrapper = new UpdateWrapper<>();
		// 修改人
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		updateWrapper.eq("id", id).set("status", active).set("updatedAt", LocalDateTime.now())
				.set("updatedId", user.getId());
		if (CommonConstant.COMMON_STATUS_STRING_ACTIVE.equals(active))
		{
			updateWrapper.set("inactiveDate", null).set("activeDate", LocalDate.now());
		}
		else
		{
			updateWrapper.set("inactiveDate", LocalDate.now());
		}
		companyMapper.update(null, updateWrapper);
		return result;
	}
}