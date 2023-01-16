package com.fssy.shareholder.management.controller.system.operate.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.operate.analysis.ManageCompanyService;
import com.fssy.shareholder.management.tools.constant.CommonConstant;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 授权访问的公司代码列表 *****数据表名：
 * bs_operate_manage_company *****数据表作用： 经营分析可以授权访问的公司列表 *****变更记录： 时间 变更人 变更内容
 * 20221213 兰宇铧 初始设计 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Controller
@RequestMapping("/operate/analysis/manage-company/")
public class ManageCompanyController
{
	@Autowired
	private ManageCompanyService manageCompanyService;

	/**
	 * 管理公司业务实现类
	 */
	@Autowired
	private CompanyService companyService;
	
	/**
	 * 经营公司管理页面
	 *
	 * @return 经营公司管理页面
	 */
	@RequiredLog("经营公司管理")
	@RequiresPermissions("operate:analysis:manage:company:index")
	@GetMapping("index")
	public String index(Model model)
	{
		return "system/operate/analysis/basic/manage-company-list";
	}

	/**
	 * 返回 对象列表
	 *
	 * @param request 前端请求参数
	 * @return Map集合
	 */
	@RequiresPermissions("operate:analysis:manage:company:getObjects")
	@RequestMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getObjects(HttpServletRequest request)
	{
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = getParams(request);
		params.put("page", Integer.parseInt(request.getParameter("page")));
		params.put("limit", Integer.parseInt(request.getParameter("limit")));

		Page<Map<String, Object>> handlersItemPage = manageCompanyService
				.findManageCompanyMapPerPageByParams(params);
		if (handlersItemPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");
		}
		else
		{
			// 查出数据，返回分页数据
			result.put("code", 0);
			result.put("count", handlersItemPage.getTotal());
			result.put("data", handlersItemPage.getRecords());
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
		if (!ObjectUtils.isEmpty(request.getParameter("codeEq")))
		{
			params.put("codeEq", request.getParameter("codeEq"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("code")))
		{
			params.put("code", request.getParameter("code"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("name")))
		{
			params.put("name", request.getParameter("name"));
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
		if (!ObjectUtils.isEmpty(request.getParameter("shortName")))
		{
			params.put("shortName", request.getParameter("shortName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("basicCompanyId")))
		{
			params.put("basicCompanyId", request.getParameter("basicCompanyId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("basicCompanyIds")))
		{
			String basicCompanyIdsStr = request.getParameter("basicCompanyIds");
			List<String> basicCompanyIds = Arrays.asList(basicCompanyIdsStr.split(","));
			params.put("basicCompanyIds", basicCompanyIds);
		}
		// endregion
		return params;
	}

	/**
	 * 获取对接系统数据
	 *
	 * @param request 前端请求参数
	 * @return Map集合
	 */
	@RequiredLog("人工对接财务系统经营公司")
	@RequiresPermissions("operate:analysis:manage:company:receive:data")
	@RequestMapping("receive-data")
	@ResponseBody
	public SysResult receiveData(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		manageCompanyService.receiveDataByArtificial(params);
		return SysResult.ok();
	}
	
	/**
	 * 返回修改页面
	 *
	 * @param request 请求实体
	 * @param model   视图模型
	 * @return
	 */
	@RequiredLog("修改经营公司")
	@RequiresPermissions("operate:analysis:manage:company:edit")
	@GetMapping("edit")
	public String editView(HttpServletRequest request, Model model)
	{
		// 获取公司id
		String id = request.getParameter("id");
		// 查询公司信息
		ManageCompany manageCompany = manageCompanyService.findDataById(id);
		model.addAttribute("manageCompany", manageCompany);

		// 查询管理公司
		Map<String, Object> companyParams = new HashMap<>();
		companyParams.put("status", CommonConstant.COMMON_STATUS_STRING_ACTIVE);
		List<String> selectedIds = new ArrayList<>();
		if (!ObjectUtils.isEmpty(manageCompany.getBasicCompanyId()))
		{
			selectedIds.add(manageCompany.getBasicCompanyId().toString());
		}
		List<Map<String, Object>> companyList = companyService
				.findCompanySelectedDataListByParams(companyParams, selectedIds);
		model.addAttribute("companyList", companyList);

		return "system/operate/analysis/basic/manage-company-edit";
	}

	/**
	 * 修改经营公司操作
	 *
	 * @param company 公司实体
	 * @return
	 */
	@RequiredLog("修改经营公司操作")
	@RequiresPermissions("operate:analysis:manage:company:update")
	@PutMapping("update")
	@ResponseBody
	public SysResult update(@Valid ManageCompany mangeCompany)
	{
		manageCompanyService.update(mangeCompany);
		return SysResult.ok();
	}
}
