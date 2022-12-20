/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
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
import com.fssy.shareholder.management.mapper.system.operate.analysis.CashFlowMapper;
import com.fssy.shareholder.management.mapper.system.operate.analysis.ManageCompanyMapper;
import com.fssy.shareholder.management.pojo.system.operate.analysis.CashFlow;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;
import com.fssy.shareholder.management.service.system.operate.analysis.CashFlowService;
import com.fssy.shareholder.management.tools.common.ClientTool;
import com.fssy.shareholder.management.tools.common.DateTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @Title: CashFlowServiceImpl.java
 * @Description: 现金流量功能业务实现类
 * @author Solomon
 * @date 2022年12月20日 上午10:06:58
 */
@Service
public class CashFlowServiceImpl extends ServiceImpl<CashFlowMapper, CashFlow>
		implements CashFlowService
{
	/**
	 * 现金流量数据访问实现类
	 */
	@Autowired
	private CashFlowMapper cashFlowMapper;

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
	public List<CashFlow> findCashFlowDataByParams(Map<String, Object> params)
	{
		QueryWrapper<CashFlow> queryWrapper = getQueryWrapper(params);
		return cashFlowMapper.selectList(queryWrapper);
	}

	@Override
	public Page<CashFlow> findCashFlowDataPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<CashFlow> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<CashFlow> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
		return cashFlowMapper.selectPage(myPage, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> findCashFlowMapDataByParams(Map<String, Object> params)
	{
		QueryWrapper<CashFlow> queryWrapper = getQueryWrapper(params);
		return cashFlowMapper.selectMaps(queryWrapper);
	}

	@Override
	public Page<Map<String, Object>> findCashFlowMapPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<CashFlow> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return cashFlowMapper.selectMapsPage(myPage, queryWrapper);
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<CashFlow> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建query
		QueryWrapper<CashFlow> queryWrapper = Wrappers.query();
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
	public synchronized Map<String, Object> receiveData(Map<String, Object> params)
	{
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("msg", "操作成功；");
		// region 构建参数
		Map<String, Object> transmitParams = new HashMap<>();
		transmitParams.put("uid", uid);
		transmitParams.put("pwd", pwd);
		transmitParams.put("report", "xjllb");
		if (params.containsKey("year"))
		{
			// 格式校验
			Integer year = InstandTool
					.stringToInteger(InstandTool.objectToString(params.get("year")));
			if (0 >= year && year >= 9999)
			{
				throw new ServiceException(String.format("年份【%s】必须为4位数", year));
			}
			String pattern = ".";
			if (year.toString().matches(pattern))
			{
				throw new ServiceException(String.format("年份【%s】必须为整数", year));
			}
			transmitParams.put("y", params.get("year"));
		}
		else
		{
			transmitParams.put("y", LocalDate.now().getYear());
		}
		if (params.containsKey("month"))
		{
			// 格式校验
			Integer month = InstandTool
					.stringToInteger(InstandTool.objectToString(params.get("month")));
			if (0 >= month && month > 12)
			{
				throw new ServiceException(String.format("月份【%s】必须为【1到12】的数", month));
			}
			String pattern = ".";
			if (month.toString().matches(pattern))
			{
				throw new ServiceException(String.format("月份【%s】必须为整数", month));
			}
			transmitParams.put("m", params.get("month"));
		}
		// endregion

		// region 发送请求
		String url = "http://192.168.30.232/cw/json/json_value.asp";
		Map<String, Object> resultMap = clientTool.dispatch(transmitParams, url);
		// endregion

		// region 处理返回数据
		StringBuffer sb = new StringBuffer();
		int totalDataCount = 0;
		int successInsertDataCount = 0;
		int successUpdateDataCount = 0;
		int successSameDataCount = 0;
		if (ObjectUtils.isEmpty(resultMap))
		{
			StringTool.setMsg(sb, String.format("年【%s】，月【%s】的现金流量数据,无法找到;", transmitParams.get("y"),
					transmitParams.get("m")));
			result.put("result", false);
			result.put("msg", sb.toString());
			return result;
		}
		if (resultMap.containsKey("data"))
		{
			boolean flag = true;
			List<CashFlow> insertDataList = new ArrayList<>();
			// 2022-12-16 添加空值判断，当查询数据为空时，记录对接情况
			if (ObjectUtils.isEmpty(resultMap.get("data")))
			{
				flag = false;
				StringTool.setMsg(sb, String.format("年【%s】，月【%s】的现金流量数据,无法找到",
						transmitParams.get("y"), transmitParams.get("m")));
			}
			else
			{
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> resultDataList = (List<Map<String, Object>>) resultMap
						.get("data");

				if (!ObjectUtils.isEmpty(resultDataList))
				{
					// 查询系统中已经存在的现金流量数据
					QueryWrapper<ManageCompany> queryWrapper = new QueryWrapper<>();
					List<ManageCompany> manageCompanies = manageCompanyMapper
							.selectList(queryWrapper);
					Map<String, ManageCompany> manageCompanyKeyBy = IteratorTool
							.keyByPattern("code", manageCompanies);

					// 系统查询现金流量表缓存map
					Map<String, CashFlow> cacheCashFlow = new HashMap<>();
					QueryWrapper<CashFlow> cashFlowQueryWrapper;
					for (Map<String, Object> transmitData : resultDataList)
					{
						totalDataCount++;
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
						Double cumulativeBalance = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("金额累计")));
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
									"公司【%s】,项目【%s】，年【%s】，月【%s】的现金流量数据，录入日期【%s】格式不对，正确格式为【yyyy-MM-dd HH:mm:ss】",
									tempCompany.getName(), project, year, month, createDateStr));
							continue;
						}
						// endregion

						// 查询系统的现金流量表数据
						// region 缓存查询系统已经有的现金流量表数据
						String key = projectCode + "_" + tempCompany.getCode() + "_" + year + "_"
								+ month;
						CashFlow exitProfitData;
						if (cacheCashFlow.containsKey(key))
						{
							exitProfitData = cacheCashFlow.get(key);
						}
						else
						{
							cashFlowQueryWrapper = new QueryWrapper<>();
							cashFlowQueryWrapper.eq("projectCode", projectCode)
									.eq("companyCode", tempCompany.getCode()).eq("year", year)
									.eq("month", month);
							List<CashFlow> cashFlowList = cashFlowMapper
									.selectList(cashFlowQueryWrapper);
							if (!ObjectUtils.isEmpty(cashFlowList))
							{
								exitProfitData = cashFlowList.get(0);
								cacheCashFlow.put(key, exitProfitData);
							}
							else
							{
								exitProfitData = null;
								cacheCashFlow.put(key, null);
							}
						}
						// endregion

						// region 判断如果现金流量数据不存在，需要添加现金流量数据;存在时，判断createDate是否相同，不同时删除原来的，重新添加
						if (ObjectUtils.isEmpty(exitProfitData))
						{
							CashFlow cashFlow = new CashFlow();
							cashFlow.setNote("");
							cashFlow.setCompanyCode(tempCompany.getCode());
							cashFlow.setCompanyName(tempCompany.getName());
							cashFlow.setCompanyId(tempCompany.getId());
							cashFlow.setYear(year);
							cashFlow.setMonth(month);
							cashFlow.setProjectCode(projectCode);
							cashFlow.setProject(project);
							cashFlow.setAmount(new BigDecimal(amount));
							cashFlow.setCumulativeBalance(new BigDecimal(cumulativeBalance));
							cashFlow.setCreateName(createName);
							cashFlow.setCreateDate(createDate);
							insertDataList.add(cashFlow);
							successInsertDataCount++;
						}
						// 存在时，判断createDate是否相同，不同时删除原来的，重新添加
						else
						{
							if (!exitProfitData.getCreateDate().isEqual(createDate))
							{
								cashFlowQueryWrapper = new QueryWrapper<>();
								cashFlowQueryWrapper.eq("projectCode", projectCode)
										.eq("companyCode", tempCompany.getCode()).eq("year", year)
										.eq("month", month);
								cashFlowMapper.delete(cashFlowQueryWrapper);
								CashFlow cashFlow = new CashFlow();
								cashFlow.setNote("");
								cashFlow.setCompanyCode(tempCompany.getCode());
								cashFlow.setCompanyName(tempCompany.getName());
								cashFlow.setCompanyId(tempCompany.getId());
								cashFlow.setYear(year);
								cashFlow.setMonth(month);
								cashFlow.setProjectCode(projectCode);
								cashFlow.setProject(project);
								cashFlow.setAmount(new BigDecimal(amount));
								cashFlow.setCumulativeBalance(new BigDecimal(cumulativeBalance));
								cashFlow.setCreateName(createName);
								cashFlow.setCreateDate(createDate);
								insertDataList.add(cashFlow);
								successUpdateDataCount++;
							}
							else
							{
								successSameDataCount++;
							}
						}
						// endregion
					}
				}
			}

			String totalStatistics = String.format(
					"【%s】年-【%s】月,共对接数据【%s】条，成功添加【%s】条，成功变更【%s】条，成功对接但未改变【%s】条；",
					transmitParams.get("y"), transmitParams.get("m"), totalDataCount,
					successInsertDataCount, successUpdateDataCount, successSameDataCount);
			if (flag)
			{
				if (successSameDataCount > 0)
				{
					if (successSameDataCount == totalDataCount)
					{
						result.put("result", true);
						result.put("msg", totalStatistics + String.format("全部数据对接失败，数据未改变;"));
					}
					else
					{
						result.put("result", true);
						result.put("msg", totalStatistics + String.format("部分数据对接失败，部分数据未改变;"));
					}
				}
				else
				{
					result.put("result", true);
					result.put("msg", totalStatistics + String.format("数据对接成功;"));
				}
			}
			else
			{
				result.put("result", false);
				result.put("msg", totalStatistics + sb.toString());
			}

			if (!ObjectUtils.isEmpty(insertDataList))
			{
				cashFlowMapper.insertBatchSomeColumn(insertDataList);
			}
		}
		// endregion
		return result;
	}

}
