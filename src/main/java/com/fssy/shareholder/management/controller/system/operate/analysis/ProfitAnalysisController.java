package com.fssy.shareholder.management.controller.system.operate.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.operate.analysis.ManageCompanyService;
import com.fssy.shareholder.management.service.system.operate.analysis.ProfitAnalysisService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * 变动分析表管理控制器
 *
 * @author Solomon
 * @since 2022-12-20
 */
@Controller
@RequestMapping("/operate/analysis/profit-analysis/")
public class ProfitAnalysisController
{
	/**
	 * 变动分析表功能业务实现类
	 */
	@Autowired
	private ProfitAnalysisService profitAnalysisService;

	/**
	 * 经营公司功能业务实现类
	 */
	@Autowired
	private ManageCompanyService manageCompanyService;

	/**
	 * 变动分析表数据管理页面
	 *
	 * @return 变动分析表数据管理页面
	 */
	@RequiredLog("变动分析表管理")
	@GetMapping("index")
	@RequiresPermissions("operate:analysis:profit:analysis:index")
	public String index(Model model)
	{
		Map<String, Object> companyParams = new HashMap<>();
		companyParams.put("status", CommonConstant.COMMON_STATUS_STRING_ACTIVE);
		List<String> selectedIds = new ArrayList<>();
		List<Map<String, Object>> companyList = manageCompanyService
				.findManageCompanySelectedDataListByParams(companyParams, selectedIds);
		model.addAttribute("companyList", companyList);
		return "system/operate/analysis/basic/profit-analysis-list";
	}

	/**
	 * 返回 对象列表
	 *
	 * @param request 前端请求参数
	 * @return Map集合
	 */
	@RequestMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getObjects(HttpServletRequest request)
	{
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = getParams(request);
		params.put("page", Integer.parseInt(request.getParameter("page")));
		params.put("limit", Integer.parseInt(request.getParameter("limit")));

		Page<Map<String, Object>> dataPage = profitAnalysisService
				.findProfitAnalysisMapPerPageByParams(params);
		if (dataPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");
		}
		else
		{
			// 查出数据，返回分页数据
			result.put("code", 0);
			result.put("count", dataPage.getTotal());
			result.put("data", dataPage.getRecords());
		}

		return result;
	}

	private Map<String, Object> getParams(HttpServletRequest request)
	{
		// region 构建查询条件map
		Map<String, Object> params = new HashMap<>();
		if (!ObjectUtils.isEmpty(request.getParameter("id")))
		{
			params.put("id", request.getParameter("id"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("ids")))
		{
			String idsStr = request.getParameter("ids");
			List<String> ids = Arrays.asList(idsStr.split(","));
			params.put("ids", ids);
		}
		if (!ObjectUtils.isEmpty(request.getParameter("companyId")))
		{
			params.put("companyId", request.getParameter("companyId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("companyIds")))
		{
			String companyIdsStr = request.getParameter("companyIds");
			List<String> companyIds = Arrays.asList(companyIdsStr.split(","));
			params.put("companyIds", companyIds);
		}
		if (!ObjectUtils.isEmpty(request.getParameter("companyCodeEq")))
		{
			params.put("companyCodeEq", request.getParameter("companyCodeEq"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("year")))
		{
			params.put("year", request.getParameter("year"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("month")))
		{
			params.put("month", request.getParameter("month"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("projectCodeEq")))
		{
			params.put("projectCodeEq", request.getParameter("projectCodeEq"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("projectEq")))
		{
			params.put("projectEq", request.getParameter("projectEq"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createDateStart")))
		{
			params.put("createDateStart", request.getParameter("createDateStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createDateEnd")))
		{
			params.put("createDateEnd", request.getParameter("createDateEnd"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("companyCode")))
		{
			params.put("companyCode", request.getParameter("companyCode"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("companyName")))
		{
			params.put("companyName", request.getParameter("companyName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("projectCode")))
		{
			params.put("projectCode", request.getParameter("projectCode"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("project")))
		{
			params.put("project", request.getParameter("project"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createName")))
		{
			params.put("createName", request.getParameter("createName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("select")))
		{
			params.put("select", request.getParameter("select"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("groupBy")))
		{
			params.put("groupBy", request.getParameter("groupBy"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("idDesc")))
		{
			params.put("idDesc", request.getParameter("idDesc"));
		}
		// endregion
		return params;
	}

	/**
	 * 获取对接系统数据（人工）
	 *
	 * @param request 前端请求参数
	 * @return Map集合
	 */
	@RequiredLog("人工对接财务系统变动分析表")
	@RequestMapping("receive-data")
	@RequiresPermissions("operate:analysis:profit:analysis:receive:data")
	@ResponseBody
	public SysResult receiveData(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		Map<String, Object> result = profitAnalysisService.receiveDataByArtificial(params);
		if (!Boolean.valueOf(InstandTool.objectToString(result.get("result"))))
		{
			SysResult.build(500, InstandTool.objectToString(result.get("msg")));
		}
		return SysResult.build(200, InstandTool.objectToString(result.get("msg")));
	}

	/**
	 * 导出变动分析表数据
	 * 
	 * @param request  请求
	 * @param response 响应
	 */
	@RequiredLog("导出变动分析表数据")
	@RequiresPermissions("operate:analysis:profit:analysis:download")
	@GetMapping("download")
	@ResponseBody
	public SysResult download(HttpServletRequest request, HttpServletResponse response)
	{
		Map<String, Object> params = getParams(request);
		// region 业务判断，年份必填
		if (!params.containsKey("year"))
		{
			throw new ServiceException(String.format("导出数据时【年份】必须选择"));
		}
		// endregion

		List<Map<String, Object>> profitList = profitAnalysisService
				.findProfitAnalysisMapDataByParams(params);

		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
		// 需要改背景色的格子
		fieldMap.put("companyCode", "公司代码");
		fieldMap.put("companyName", "公司名称");
		fieldMap.put("year", "年");
		fieldMap.put("month", "月");
		fieldMap.put("projectCode", "项目代码");
		fieldMap.put("project", "项目名称");

		fieldMap.put("accumulateMoney", "累计实际");
		fieldMap.put("accumulateBudget", "累计预算");
		fieldMap.put("lastYearAccumulate", "上年金额累计");
		fieldMap.put("yearBudget", "全年预算");

		fieldMap.put("moneyChangeToLastYear", "上年_金额差");
		fieldMap.put("differenceToLastYear", "上年_量差");
		fieldMap.put("structureToLastYear", "上年_结构差");
		fieldMap.put("priceToLastYear", "上年_价差");
		fieldMap.put("comparisonToLastYear", "上年_比对");

		fieldMap.put("moneyChangeToBudget", "预算_金额差");
		fieldMap.put("differenceToBudget", "预算_量差");
		fieldMap.put("structureToBudget", "预算_结构差");
		fieldMap.put("priceToBudget", "预算_价差");
		fieldMap.put("comparisonToBudget", "预算_比对");

		fieldMap.put("createName", "录入人");
		fieldMap.put("createDate", "录入时间");
		SheetOutputService sheetOutputService = new SheetOutputService();
		if (ObjectUtils.isEmpty(profitList))
		{
			throw new ServiceException("未查出数据");
		}
		sheetOutputService.export("变动分析表", profitList, fieldMap, response);
		return SysResult.ok();
	}
}
