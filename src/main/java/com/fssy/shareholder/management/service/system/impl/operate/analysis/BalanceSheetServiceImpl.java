package com.fssy.shareholder.management.service.system.impl.operate.analysis;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.analysis.BalanceSheetMapper;
import com.fssy.shareholder.management.mapper.system.operate.analysis.ManageCompanyMapper;
import com.fssy.shareholder.management.pojo.system.operate.analysis.BalanceSheet;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;
import com.fssy.shareholder.management.service.system.operate.analysis.BalanceSheetService;
import com.fssy.shareholder.management.tools.common.ClientTool;
import com.fssy.shareholder.management.tools.common.DateTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.StringTool;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 资产负债表 *****数据表名： bs_operate_balance_sheet
 * *****数据表作用： 各企业公司的资产负债表，以每月出的财务报表为基础 *****变更记录： 时间 变更人 变更内容 20221213 兰宇铧 初始设计
 * 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Service
public class BalanceSheetServiceImpl extends ServiceImpl<BalanceSheetMapper, BalanceSheet>
		implements BalanceSheetService
{
	/**
	 * 资产负债表数据访问实现类
	 */
	@Autowired
	private BalanceSheetMapper balanceSheetMapper;

	/**
	 * 双系统对接工具类
	 */
	@Autowired
	private ClientTool clientTool;

	@Value(value = "${business.finance.system.username}")
	private String uid;

	@Value(value = "${business.finance.system.password}")
	private String pwd;

	/**
	 * 授权访问的公司代码列表数据访问实现类
	 */
	@Autowired
	private ManageCompanyMapper manageCompanyMapper;

	@Override
	public List<BalanceSheet> findBalanceSheetDataByParams(Map<String, Object> params)
	{
		QueryWrapper<BalanceSheet> queryWrapper = getQueryWrapper(params);
		return balanceSheetMapper.selectList(queryWrapper);
	}

	@Override
	public Page<BalanceSheet> findBalanceSheetDataPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<BalanceSheet> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<BalanceSheet> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
		return balanceSheetMapper.selectPage(myPage, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> findBalanceSheetMapDataByParams(Map<String, Object> params)
	{
		QueryWrapper<BalanceSheet> queryWrapper = getQueryWrapper(params);
		return balanceSheetMapper.selectMaps(queryWrapper);
	}

	@Override
	public Page<Map<String, Object>> findBalanceSheetMapPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<BalanceSheet> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return balanceSheetMapper.selectMapsPage(myPage, queryWrapper);
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<BalanceSheet> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建query
		QueryWrapper<BalanceSheet> queryWrapper = Wrappers.query();
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		if (params.containsKey("ids"))
		{
			queryWrapper.in("id", (List<String>) params.get("ids"));
		}
		if (params.containsKey("companyId"))
		{
			queryWrapper.eq("companyId", params.get("companyId"));
		}
		if (params.containsKey("companyIds"))
		{
			queryWrapper.in("companyId", (List<String>) params.get("companyIds"));
		}
		if (params.containsKey("companyCodeEq"))
		{
			queryWrapper.eq("companyCode", params.get("companyCodeEq"));
		}
		if (params.containsKey("year"))
		{
			queryWrapper.eq("year", params.get("year"));
		}
		if (params.containsKey("month"))
		{
			queryWrapper.eq("month", params.get("month"));
		}
		if (params.containsKey("projectCodeEq"))
		{
			queryWrapper.eq("projectCode", params.get("projectCodeEq"));
		}
		if (params.containsKey("projectEq"))
		{
			queryWrapper.eq("project", params.get("projectEq"));
		}
		if (params.containsKey("createDateStart"))
		{
			queryWrapper.ge("createDate", params.get("createDateStart"));
		}
		if (params.containsKey("createDateEnd"))
		{
			queryWrapper.le("createDate", params.get("createDateEnd"));
		}
		if (params.containsKey("companyCode"))
		{
			queryWrapper.like("companyCode", params.get("companyCode"));
		}
		if (params.containsKey("companyName"))
		{
			queryWrapper.like("companyName", params.get("companyName"));
		}
		if (params.containsKey("projectCode"))
		{
			queryWrapper.like("projectCode", params.get("projectCode"));
		}
		if (params.containsKey("project"))
		{
			queryWrapper.like("project", params.get("project"));
		}
		if (params.containsKey("createName"))
		{
			queryWrapper.like("createName", params.get("createName"));
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

	@Override
	public Map<String, Object> receiveData(Map<String, Object> params)
	{
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);

		// region 构建参数
		Map<String, Object> transmitParams = new HashMap<>();
		transmitParams.put("uid", uid);
		transmitParams.put("pwd", pwd);
		transmitParams.put("report", "zcfzb");
		if (params.containsKey("year"))
		{
			transmitParams.put("y", params.get("year"));
		}
		else
		{
			transmitParams.put("y", LocalDate.now().getYear());
		}
		if (params.containsKey("month"))
		{
			transmitParams.put("m", params.get("month"));
		}
		// endregion

		// region 发送请求
		String url = "http://192.168.30.232/cw/json/json_value.asp";
		Map<String, Object> resultMap = clientTool.dispatch(transmitParams, url);
		// endregion

		// region 处理返回数据
		if (resultMap.containsKey("data"))
		{
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> resultDataList = (List<Map<String, Object>>) resultMap
					.get("data");

			boolean flag = true;
			StringBuffer sb = new StringBuffer();
			List<BalanceSheet> insertDataList = new ArrayList<>();
			if (!ObjectUtils.isEmpty(resultDataList))
			{
				// 查询系统中已经存在的资产负债数据
				QueryWrapper<ManageCompany> queryWrapper = new QueryWrapper<>();
				List<ManageCompany> manageCompanies = manageCompanyMapper.selectList(queryWrapper);
				Map<String, ManageCompany> manageCompanyKeyBy = IteratorTool.keyByPattern("code",
						manageCompanies);

				// 系统查询资产负债表缓存map
				Map<String, BalanceSheet> cacheBalanceSheet = new HashMap<>();
				QueryWrapper<BalanceSheet> balanceSheetQueryWrapper;
				for (Map<String, Object> transmitData : resultDataList)
				{
					// region 整理对接数据
					String companyCode = InstandTool.objectToString(transmitData.get("公司代码"));
					ManageCompany tempCompany = manageCompanyKeyBy.get(companyCode);
					Integer year = InstandTool
							.stringToInteger(InstandTool.objectToString(transmitData.get("年")));
					Integer month = InstandTool
							.stringToInteger(InstandTool.objectToString(transmitData.get("月")));
					String projectCode = InstandTool.objectToString(transmitData.get("项目代码"));
					String project = StringTool
							.rightTrim(InstandTool.objectToString(transmitData.get("项目")));
					Double amount = InstandTool
							.stringToDouble(InstandTool.objectToString(transmitData.get("金额")));
					Double initialAmount = InstandTool
							.stringToDouble(InstandTool.objectToString(transmitData.get("期初")));
					String createName = StringTool
							.rightTrim(InstandTool.objectToString(transmitData.get("录入")));
					String createDateStr = InstandTool.objectToString(transmitData.get("录入日期"));
					LocalDateTime createDate = null;
					try
					{
						createDate = DateTool.transferTimeToLocalDateTime(createDateStr);
					}
					catch (Exception e)
					{
						flag = false;
						StringTool.setMsg(sb, String.format(
								"公司【%s】,项目【%s】，年【%s】，月【%s】的资产负债数据，录入日期【%s】格式不对，正确格式为【yyyy-MM-dd HH:mm:ss】",
								tempCompany.getName(), project, year, month, createDateStr));
						continue;
					}
					// endregion

					// 查询系统的资产负债表数据
					// region 缓存查询系统已经有的资产负债表数据
					String key = projectCode + "_" + tempCompany.getCode() + "_" + year + "_"
							+ month;
					BalanceSheet exitBalanceSheet;
					if (cacheBalanceSheet.containsKey(key))
					{
						exitBalanceSheet = cacheBalanceSheet.get(key);
					}
					else
					{
						balanceSheetQueryWrapper = new QueryWrapper<>();
						balanceSheetQueryWrapper.eq("projectCode", projectCode)
								.eq("companyCode", tempCompany.getCode()).eq("year", year)
								.eq("month", month);
						List<BalanceSheet> balanceSheetList = balanceSheetMapper
								.selectList(balanceSheetQueryWrapper);
						if (!ObjectUtils.isEmpty(balanceSheetList))
						{
							exitBalanceSheet = balanceSheetList.get(0);
							cacheBalanceSheet.put(key, exitBalanceSheet);
						}
						else
						{
							exitBalanceSheet = null;
							cacheBalanceSheet.put(key, null);
						}
					}
					// endregion

					// region 判断如果资产负债数据不存在，需要添加资产负债数据;存在时，判断createDate是否相同，不同时删除原来的，重新添加
					if (ObjectUtils.isEmpty(exitBalanceSheet))
					{
						BalanceSheet balanceSheet = new BalanceSheet();
						balanceSheet.setNote("");
						balanceSheet.setCompanyCode(tempCompany.getCode());
						balanceSheet.setCompanyName(tempCompany.getName());
						balanceSheet.setCompanyId(tempCompany.getId());
						balanceSheet.setYear(year);
						balanceSheet.setMonth(month);
						balanceSheet.setProjectCode(projectCode);
						balanceSheet.setProject(project);
						balanceSheet.setAmount(new BigDecimal(amount));
						balanceSheet.setInitialAmount(new BigDecimal(initialAmount));
						balanceSheet.setCreateName(createName);
						balanceSheet.setCreateDate(createDate);
						insertDataList.add(balanceSheet);
					}
					// 存在时，判断createDate是否相同，不同时删除原来的，重新添加
					else
					{
						if (!exitBalanceSheet.getCreateDate().isEqual(createDate))
						{
							balanceSheetQueryWrapper = new QueryWrapper<>();
							balanceSheetQueryWrapper.eq("projectCode", projectCode)
									.eq("companyCode", tempCompany.getCode()).eq("year", year)
									.eq("month", month);
							balanceSheetMapper.delete(balanceSheetQueryWrapper);
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setNote("");
							balanceSheet.setCompanyCode(tempCompany.getCode());
							balanceSheet.setCompanyName(tempCompany.getName());
							balanceSheet.setCompanyId(tempCompany.getId());
							balanceSheet.setYear(year);
							balanceSheet.setMonth(month);
							balanceSheet.setProjectCode(projectCode);
							balanceSheet.setProject(project);
							balanceSheet.setAmount(new BigDecimal(amount));
							balanceSheet.setInitialAmount(new BigDecimal(initialAmount));
							balanceSheet.setCreateName(createName);
							balanceSheet.setCreateDate(createDate);
							insertDataList.add(balanceSheet);
						}
					}
					// endregion
				}
			}

			if (!flag)
			{
				// TODO 写到记录表
				System.out.println(String.format("部分数据对接失败，错误描述为【%s】", sb.toString()));
			}

			if (!ObjectUtils.isEmpty(insertDataList))
			{
				balanceSheetMapper.insertBatchSomeColumn(insertDataList);
			}
		}
		// endregion

		return result;
	}
}
