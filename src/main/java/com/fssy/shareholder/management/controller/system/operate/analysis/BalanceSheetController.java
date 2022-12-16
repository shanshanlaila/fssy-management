package com.fssy.shareholder.management.controller.system.operate.analysis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.service.system.operate.analysis.BalanceSheetService;
import com.fssy.shareholder.management.service.system.operate.analysis.ManageCompanyService;
import com.fssy.shareholder.management.tools.constant.CommonConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 资产负债表 *****数据表名： bs_operate_balance_sheet
 * *****数据表作用： 各企业公司的资产负债表，以每月出的财务报表为基础 *****变更记录： 时间 变更人 变更内容 20221213 兰宇铧 初始设计
 * 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Controller
@RequestMapping("/operate/analysis/balance-sheet/")
public class BalanceSheetController
{
	/**
	 * 资产负债功能业务实现类
	 */
	@Autowired
	private BalanceSheetService balanceSheetService;

	/**
	 *  经营公司功能业务实现类
	 */
	@Autowired
	private ManageCompanyService manageCompanyService;

	/**
	 * 资产负债表数据管理页面
	 *
	 * @return 资产负债表数据管理页面
	 */
	@RequiredLog("资产负债表管理")
	@GetMapping("index")
	@RequiresPermissions("operate:analysis:balance:sheet:index")
	public String index(Model model)
	{
		Map<String, Object> companyParams = new HashMap<>();
		companyParams.put("status", CommonConstant.COMMON_STATUS_STRING_ACTIVE);
		List<String> selectedIds = new ArrayList<>();
		List<Map<String, Object>> companyList = manageCompanyService
				.findManageCompanySelectedDataListByParams(companyParams, selectedIds);
		model.addAttribute("companyList", companyList);
		return "system/operate/analysis/basic/balance-sheet-list";
	}

	/**
	 * 返回对象列表
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

		Page<Map<String, Object>> dataPage = balanceSheetService
				.findBalanceSheetMapPerPageByParams(params);
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
	@RequiredLog("人工对接财务系统资产负债表")
	@RequestMapping("receive-data")
	@RequiresPermissions("operate:analysis:balance:sheet:receive:data")
	@ResponseBody
	public SysResult receiveData(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		balanceSheetService.receiveDataByArtificial(params);
		return SysResult.ok();
	}
}
