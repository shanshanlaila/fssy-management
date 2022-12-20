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
import com.fssy.shareholder.management.mapper.system.operate.analysis.ManageCompanyMapper;
import com.fssy.shareholder.management.mapper.system.operate.analysis.ProfitStatementMapper;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ProfitStatement;
import com.fssy.shareholder.management.service.system.operate.analysis.ProfitStatementService;
import com.fssy.shareholder.management.tools.common.ClientTool;
import com.fssy.shareholder.management.tools.common.DateTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 利润表 *****数据表名： bs_operate_profit_statement
 * *****数据表作用： 各企业公司的利润表，以每月出的财务报表为基础 *****变更记录： 时间 变更人 变更内容 20221213 兰宇铧 初始设计
 * 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Service
public class ProfitStatementServiceImpl extends ServiceImpl<ProfitStatementMapper, ProfitStatement>
		implements ProfitStatementService
{
	/**
	 * 利润表数据访问实现类
	 */
	@Autowired
	private ProfitStatementMapper profitStatementMapper;

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
	public List<ProfitStatement> findProfitStatementDataByParams(Map<String, Object> params)
	{
		QueryWrapper<ProfitStatement> queryWrapper = getQueryWrapper(params);
		return profitStatementMapper.selectList(queryWrapper);
	}

	@Override
	public Page<ProfitStatement> findProfitStatementDataPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<ProfitStatement> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<ProfitStatement> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return profitStatementMapper.selectPage(myPage, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> findProfitStatementMapDataByParams(Map<String, Object> params)
	{
		QueryWrapper<ProfitStatement> queryWrapper = getQueryWrapper(params);
		return profitStatementMapper.selectMaps(queryWrapper);
	}

	@Override
	public Page<Map<String, Object>> findProfitStatementMapPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<ProfitStatement> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return profitStatementMapper.selectMapsPage(myPage, queryWrapper);
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<ProfitStatement> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建query
		QueryWrapper<ProfitStatement> queryWrapper = Wrappers.query();
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
		// region 构建参数
		Map<String, Object> transmitParams = new HashMap<>();
		transmitParams.put("uid", uid);
		transmitParams.put("pwd", pwd);
		transmitParams.put("report", "lyb");
		if (params.containsKey("year"))
		{
			// 格式校验
			Integer year = InstandTool
					.stringToInteger(InstandTool.objectToString(params.get("year")));
			if (0 >= year && year >= 9999)
			{
				throw new ServiceException(String.format("年份【%s】必须为4位数", year));
			}
			String pattern = "/.";
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
			String pattern = "/.";
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
			StringTool.setMsg(sb, String.format("年【%s】，月【%s】的利润数据,无法找到;", transmitParams.get("y"),
					transmitParams.get("m")));
			result.put("result", false);
			result.put("msg", sb.toString());
			return result;
		}
		if (resultMap.containsKey("data"))
		{
			boolean flag = true;
			List<ProfitStatement> insertDataList = new ArrayList<>();
			// 2022-12-16 添加空值判断，当查询数据为空时，记录对接情况
			if (ObjectUtils.isEmpty(resultMap.get("data")))
			{
				flag = false;
				StringTool.setMsg(sb, String.format("年【%s】，月【%s】的利润数据,无法找到",
						transmitParams.get("y"), transmitParams.get("m")));
			}
			else
			{
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> resultDataList = (List<Map<String, Object>>) resultMap
						.get("data");

				if (!ObjectUtils.isEmpty(resultDataList))
				{
					// 查询系统中已经存在的利润数据
					QueryWrapper<ManageCompany> queryWrapper = new QueryWrapper<>();
					List<ManageCompany> manageCompanies = manageCompanyMapper
							.selectList(queryWrapper);
					Map<String, ManageCompany> manageCompanyKeyBy = IteratorTool
							.keyByPattern("code", manageCompanies);

					// 系统查询利润表缓存map
					Map<String, ProfitStatement> cacheProfitStatement = new HashMap<>();
					QueryWrapper<ProfitStatement> profitStatementQueryWrapper;
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
									"公司【%s】,项目【%s】，年【%s】，月【%s】的利润数据，录入日期【%s】格式不对，正确格式为【yyyy-MM-dd HH:mm:ss】",
									tempCompany.getName(), project, year, month, createDateStr));
							continue;
						}
						// endregion

						// 查询系统的利润表数据
						// region 缓存查询系统已经有的利润表数据
						String key = projectCode + "_" + tempCompany.getCode() + "_" + year + "_"
								+ month;
						ProfitStatement exitProfitData;
						if (cacheProfitStatement.containsKey(key))
						{
							exitProfitData = cacheProfitStatement.get(key);
						}
						else
						{
							profitStatementQueryWrapper = new QueryWrapper<>();
							profitStatementQueryWrapper.eq("projectCode", projectCode)
									.eq("companyCode", tempCompany.getCode()).eq("year", year)
									.eq("month", month);
							List<ProfitStatement> profitStatementList = profitStatementMapper
									.selectList(profitStatementQueryWrapper);
							if (!ObjectUtils.isEmpty(profitStatementList))
							{
								exitProfitData = profitStatementList.get(0);
								cacheProfitStatement.put(key, exitProfitData);
							}
							else
							{
								exitProfitData = null;
								cacheProfitStatement.put(key, null);
							}
						}
						// endregion

						// region 判断如果利润数据不存在，需要添加利润数据;存在时，判断createDate是否相同，不同时删除原来的，重新添加
						if (ObjectUtils.isEmpty(exitProfitData))
						{
							ProfitStatement profitStatement = new ProfitStatement();
							profitStatement.setNote("");
							profitStatement.setCompanyCode(tempCompany.getCode());
							profitStatement.setCompanyName(tempCompany.getName());
							profitStatement.setCompanyId(tempCompany.getId());
							profitStatement.setYear(year);
							profitStatement.setMonth(month);
							profitStatement.setProjectCode(projectCode);
							profitStatement.setProject(project);
							profitStatement.setAmount(new BigDecimal(amount));
							profitStatement.setCumulativeBalance(new BigDecimal(cumulativeBalance));
							profitStatement.setCreateName(createName);
							profitStatement.setCreateDate(createDate);
							insertDataList.add(profitStatement);
							successInsertDataCount++;
						}
						// 存在时，判断createDate是否相同，不同时删除原来的，重新添加
						else
						{
							if (!exitProfitData.getCreateDate().isEqual(createDate))
							{
								profitStatementQueryWrapper = new QueryWrapper<>();
								profitStatementQueryWrapper.eq("projectCode", projectCode)
										.eq("companyCode", tempCompany.getCode()).eq("year", year)
										.eq("month", month);
								profitStatementMapper.delete(profitStatementQueryWrapper);
								ProfitStatement profitStatement = new ProfitStatement();
								profitStatement.setNote("");
								profitStatement.setCompanyCode(tempCompany.getCode());
								profitStatement.setCompanyName(tempCompany.getName());
								profitStatement.setCompanyId(tempCompany.getId());
								profitStatement.setYear(year);
								profitStatement.setMonth(month);
								profitStatement.setProjectCode(projectCode);
								profitStatement.setProject(project);
								profitStatement.setAmount(new BigDecimal(amount));
								profitStatement
										.setCumulativeBalance(new BigDecimal(cumulativeBalance));
								profitStatement.setCreateName(createName);
								profitStatement.setCreateDate(createDate);
								insertDataList.add(profitStatement);
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

			String totalStatistics = String.format("【%s】年-【%s】月,共对接数据【%s】条，成功添加【%s】条，成功变更【%s】条，成功对接但未改变【%s】条；",
					transmitParams.get("y"), transmitParams.get("m"), totalDataCount, successInsertDataCount, successUpdateDataCount,
					successSameDataCount);
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
				profitStatementMapper.insertBatchSomeColumn(insertDataList);
			}
		}
		// endregion
		return result;
	}
}
