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
import com.fssy.shareholder.management.mapper.system.operate.analysis.ManageCompanyMapper;
import com.fssy.shareholder.management.mapper.system.operate.analysis.ProfitAnalysisMapper;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ProfitAnalysis;
import com.fssy.shareholder.management.service.system.operate.analysis.ProfitAnalysisService;
import com.fssy.shareholder.management.tools.common.ClientTool;
import com.fssy.shareholder.management.tools.common.DateTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @Title: ProfitAnalysisServiceImpl.java
 * @Description: 变动分析表功能业务实现类
 * @author Solomon
 * @date 2022年12月20日 上午11:19:34
 */
@Service
public class ProfitAnalysisServiceImpl extends ServiceImpl<ProfitAnalysisMapper, ProfitAnalysis>
		implements ProfitAnalysisService
{
	/**
	 * 变动分析表数据访问实现类
	 */
	@Autowired
	private ProfitAnalysisMapper profitAnalysisMapper;

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
	public List<ProfitAnalysis> findProfitAnalysisDataByParams(Map<String, Object> params)
	{
		QueryWrapper<ProfitAnalysis> queryWrapper = getQueryWrapper(params);
		return profitAnalysisMapper.selectList(queryWrapper);
	}

	@Override
	public Page<ProfitAnalysis> findProfitAnalysisDataPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<ProfitAnalysis> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<ProfitAnalysis> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return profitAnalysisMapper.selectPage(myPage, queryWrapper);
	}

	@Override
	public List<Map<String, Object>> findProfitAnalysisMapDataByParams(Map<String, Object> params)
	{
		QueryWrapper<ProfitAnalysis> queryWrapper = getQueryWrapper(params);
		return profitAnalysisMapper.selectMaps(queryWrapper);
	}

	@Override
	public Page<Map<String, Object>> findProfitAnalysisMapPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<ProfitAnalysis> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
				(int) params.get("limit"));
		return profitAnalysisMapper.selectMapsPage(myPage, queryWrapper);
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<ProfitAnalysis> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建query
		QueryWrapper<ProfitAnalysis> queryWrapper = Wrappers.query();
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
		transmitParams.put("report", "bdfxb");
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
			StringTool.setMsg(sb, String.format("年【%s】，月【%s】的变动分析数据,无法找到;", transmitParams.get("y"),
					transmitParams.get("m")));
			result.put("result", false);
			result.put("msg", sb.toString());
			return result;
		}
		if (resultMap.containsKey("data"))
		{
			boolean flag = true;
			List<ProfitAnalysis> insertDataList = new ArrayList<>();
			// 2022-12-16 添加空值判断，当查询数据为空时，记录对接情况
			if (ObjectUtils.isEmpty(resultMap.get("data")))
			{
				flag = false;
				StringTool.setMsg(sb, String.format("年【%s】，月【%s】的变动分析数据,无法找到",
						transmitParams.get("y"), transmitParams.get("m")));
			}
			else
			{
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> resultDataList = (List<Map<String, Object>>) resultMap
						.get("data");

				if (!ObjectUtils.isEmpty(resultDataList))
				{
					// 查询系统中已经存在的变动分析数据
					QueryWrapper<ManageCompany> queryWrapper = new QueryWrapper<>();
					List<ManageCompany> manageCompanies = manageCompanyMapper
							.selectList(queryWrapper);
					Map<String, ManageCompany> manageCompanyKeyBy = IteratorTool
							.keyByPattern("code", manageCompanies);

					// 系统查询变动分析表缓存map
					Map<String, ProfitAnalysis> cacheProfitAnalysis = new HashMap<>();
					QueryWrapper<ProfitAnalysis> profitAnalysisQueryWrapper;
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
						Double accumulateMoney = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("累计实际")));
						Double accumulateBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("累计预算")));
						Double lastYearAccumulate = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("上年金额累计")));
						Double yearBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("全年预算")));
						Double moneyChangeToLastYear = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("上年_金额差")));
						Double differenceToLastYear = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("上年_量差")));
						Double structureToLastYear = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("上年_结构差")));
						Double priceToLastYear = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("上年_价差")));
						Double comparisonToLastYear = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("上年_比对")));
						Double moneyChangeToBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("预算_金额差")));
						Double differenceToBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("预算_量差")));
						Double structureToBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("预算_结构差")));
						Double priceToBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("预算_价差")));
						Double comparisonToBudget = InstandTool.stringToDouble(
								InstandTool.objectToString(transmitData.get("预算_比对")));
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
									"公司【%s】,项目【%s】，年【%s】，月【%s】的变动分析数据，录入日期【%s】格式不对，正确格式为【yyyy-MM-dd HH:mm:ss】",
									tempCompany.getName(), project, year, month, createDateStr));
							continue;
						}
						// endregion

						// 查询系统的变动分析表数据
						// region 缓存查询系统已经有的变动分析表数据
						String key = projectCode + "_" + tempCompany.getCode() + "_" + year + "_"
								+ month;
						ProfitAnalysis exitProfitData;
						if (cacheProfitAnalysis.containsKey(key))
						{
							exitProfitData = cacheProfitAnalysis.get(key);
						}
						else
						{
							profitAnalysisQueryWrapper = new QueryWrapper<>();
							profitAnalysisQueryWrapper.eq("projectCode", projectCode)
									.eq("companyCode", tempCompany.getCode()).eq("year", year)
									.eq("month", month);
							List<ProfitAnalysis> profitAnalysisList = profitAnalysisMapper
									.selectList(profitAnalysisQueryWrapper);
							if (!ObjectUtils.isEmpty(profitAnalysisList))
							{
								exitProfitData = profitAnalysisList.get(0);
								cacheProfitAnalysis.put(key, exitProfitData);
							}
							else
							{
								exitProfitData = null;
								cacheProfitAnalysis.put(key, null);
							}
						}
						// endregion

						// region 判断如果变动分析数据不存在，需要添加变动分析数据;存在时，判断createDate是否相同，不同时删除原来的，重新添加
						if (ObjectUtils.isEmpty(exitProfitData))
						{
							ProfitAnalysis profitAnalysis = new ProfitAnalysis();
							profitAnalysis.setNote("");
							profitAnalysis.setCompanyCode(tempCompany.getCode());
							profitAnalysis.setCompanyName(tempCompany.getName());
							profitAnalysis.setCompanyId(tempCompany.getId());
							profitAnalysis.setYear(year);
							profitAnalysis.setMonth(month);
							profitAnalysis.setProjectCode(projectCode);
							profitAnalysis.setProject(project);
							profitAnalysis.setAccumulateMoney(new BigDecimal(accumulateMoney)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setAccumulateBudget(new BigDecimal(accumulateBudget)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setLastYearAccumulate(new BigDecimal(lastYearAccumulate)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setYearBudget(new BigDecimal(yearBudget).setScale(4,
									BigDecimal.ROUND_HALF_UP));

							profitAnalysis
									.setMoneyChangeToLastYear(new BigDecimal(moneyChangeToLastYear)
											.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis
									.setDifferenceToLastYear(new BigDecimal(differenceToLastYear)
											.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis
									.setStructureToLastYear(new BigDecimal(structureToLastYear)
											.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setPriceToLastYear(new BigDecimal(priceToLastYear)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis
									.setComparisonToLastYear(new BigDecimal(comparisonToLastYear)
											.setScale(4, BigDecimal.ROUND_HALF_UP));

							profitAnalysis
									.setMoneyChangeToBudget(new BigDecimal(moneyChangeToBudget)
											.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setDifferenceToBudget(new BigDecimal(differenceToBudget)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setStructureToBudget(new BigDecimal(structureToBudget)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setPriceToBudget(new BigDecimal(priceToBudget)
									.setScale(4, BigDecimal.ROUND_HALF_UP));
							profitAnalysis.setComparisonToBudget(new BigDecimal(comparisonToBudget)
									.setScale(4, BigDecimal.ROUND_HALF_UP));

							profitAnalysis.setCreateName(createName);
							profitAnalysis.setCreateDate(createDate);
							insertDataList.add(profitAnalysis);
							successInsertDataCount++;
						}
						// 存在时，判断createDate是否相同，不同时删除原来的，重新添加
						else
						{
							if (!exitProfitData.getCreateDate().isEqual(createDate))
							{
								profitAnalysisQueryWrapper = new QueryWrapper<>();
								profitAnalysisQueryWrapper.eq("projectCode", projectCode)
										.eq("companyCode", tempCompany.getCode()).eq("year", year)
										.eq("month", month);
								profitAnalysisMapper.delete(profitAnalysisQueryWrapper);
								ProfitAnalysis profitAnalysis = new ProfitAnalysis();
								profitAnalysis.setNote("");
								profitAnalysis.setCompanyCode(tempCompany.getCode());
								profitAnalysis.setCompanyName(tempCompany.getName());
								profitAnalysis.setCompanyId(tempCompany.getId());
								profitAnalysis.setYear(year);
								profitAnalysis.setMonth(month);
								profitAnalysis.setProjectCode(projectCode);
								profitAnalysis.setProject(project);

								profitAnalysis.setAccumulateMoney(new BigDecimal(accumulateMoney)
										.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis.setAccumulateBudget(new BigDecimal(accumulateBudget)
										.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis
										.setLastYearAccumulate(new BigDecimal(lastYearAccumulate)
												.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis.setYearBudget(new BigDecimal(yearBudget).setScale(4,
										BigDecimal.ROUND_HALF_UP));

								profitAnalysis.setMoneyChangeToLastYear(
										new BigDecimal(moneyChangeToLastYear).setScale(4,
												BigDecimal.ROUND_HALF_UP));
								profitAnalysis.setDifferenceToLastYear(
										new BigDecimal(differenceToLastYear).setScale(4,
												BigDecimal.ROUND_HALF_UP));
								profitAnalysis
										.setStructureToLastYear(new BigDecimal(structureToLastYear)
												.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis.setPriceToLastYear(new BigDecimal(priceToLastYear)
										.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis.setComparisonToLastYear(
										new BigDecimal(comparisonToLastYear).setScale(4,
												BigDecimal.ROUND_HALF_UP));

								profitAnalysis
										.setMoneyChangeToBudget(new BigDecimal(moneyChangeToBudget)
												.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis
										.setDifferenceToBudget(new BigDecimal(differenceToBudget)
												.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis
										.setStructureToBudget(new BigDecimal(structureToBudget)
												.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis.setPriceToBudget(new BigDecimal(priceToBudget)
										.setScale(4, BigDecimal.ROUND_HALF_UP));
								profitAnalysis
										.setComparisonToBudget(new BigDecimal(comparisonToBudget)
												.setScale(4, BigDecimal.ROUND_HALF_UP));

								profitAnalysis.setCreateName(createName);
								profitAnalysis.setCreateDate(createDate);
								insertDataList.add(profitAnalysis);
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
					"【%s】年-【%s】月【变动分析表】,共对接数据【%s】条，成功添加【%s】条，成功变更【%s】条，成功对接但未改变【%s】条；",
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
				profitAnalysisMapper.insertBatchSomeColumn(insertDataList);
			}
		}
		// endregion
		return result;
	}
}
