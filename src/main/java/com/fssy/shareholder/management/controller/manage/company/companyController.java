/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.manage.company;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.tools.common.InstandTool;

/**
 * @author nonghao
 * @ClassName: companyController
 * @Description: TODO
 * @date 2023/1/10 9:53
 */
@Controller
@RequestMapping("/manage/company/")
public class companyController
{
	@Autowired
	private CompanyService companyService;

	/**
	 * 返回公司列表页面
	 *
	 * @return
	 */
	@RequiredLog("公司管理")
	@RequiresPermissions("manage:company:index")
	@GetMapping("index")
	public String companyIndex()
	{
		return "manage/company/company-list";
	}

	/**
	 * 返回组公司列表页面
	 *
	 * @param request 请求实体
	 * @return
	 */
	@RequiresPermissions("manage:company:getObjects")
	@RequestMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getObjects(HttpServletRequest request)
	{
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = getParams(request);
		params.put("page", Integer.parseInt(request.getParameter("page")));
		params.put("limit", Integer.parseInt(request.getParameter("limit")));

		Page<Company> handlersItemPage = companyService.findDataPageByParams(params);
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
		// region 构建查询参数
		Map<String, Object> params = new HashMap<>();
		if (!ObjectUtils.isEmpty(request.getParameter("id")))
		{
			params.put("id", request.getParameter("id"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("note")))
		{
			params.put("note", request.getParameter("note"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("name")))
		{
			params.put("name", request.getParameter("name"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("shortName")))
		{
			params.put("shortName", request.getParameter("shortName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createdAtStart")))
		{
			params.put("createdAtStart", request.getParameter("createdAtStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createdAtEnd")))
		{
			params.put("createdAtEnd", request.getParameter("createdAtEnd"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("updatedAtStart")))
		{
			params.put("updatedAtStart", request.getParameter("updatedAtStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("updatedAtEnd")))
		{
			params.put("updatedAtEnd", request.getParameter("updatedAtEnd"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createdId")))
		{
			params.put("createdId", request.getParameter("createdId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createdId")))
		{
			params.put("createdId", request.getParameter("createdId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("updatedId")))
		{
			params.put("updatedId", request.getParameter("updatedId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createdName")))
		{
			params.put("createdName", request.getParameter("createdName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("updatedName")))
		{
			params.put("updatedName", request.getParameter("updatedName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("code")))
		{
			params.put("code", request.getParameter("code"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("codeEq")))
		{
			params.put("codeEq", request.getParameter("codeEq"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("status")))
		{
			params.put("status", request.getParameter("status"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("activeDateStart")))
		{
			params.put("activeDateStart", request.getParameter("activeDateStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("activeDateEnd")))
		{
			params.put("activeDateEnd", request.getParameter("activeDateEnd"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("inactiveDateStart")))
		{
			params.put("inactiveDateStart", request.getParameter("inactiveDateStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("inactiveDateEnd")))
		{
			params.put("inactiveDateEnd", request.getParameter("inactiveDateEnd"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("idDesc")))
		{
			params.put("idDesc", request.getParameter("idDesc"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("select")))
		{
			params.put("select", request.getParameter("select"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("groupBy")))
		{
			params.put("groupBy", request.getParameter("groupBy"));
		}
		// endregion
		return params;
	}

	/**
	 * 返回创建页面
	 *
	 * @param request 请求实体
	 * @return
	 */
	@RequiredLog("添加公司")
	@RequiresPermissions("manage:company:create")
	@GetMapping("create")
	public String createView(HttpServletRequest request)
	{
		return "manage/company/company-create";
	}

	/**
	 * 创建公司
	 *
	 * @param company 公司实体
	 * @return
	 */
	@RequiredLog("添加公司操作")
	@RequiresPermissions("manage:company:store")
	@PostMapping("store")
	@ResponseBody
	public SysResult store(Company company)
	{
		companyService.saveCompany(company);
		return SysResult.ok();
	}

	/**
	 * 返回修改页面
	 *
	 * @param request 请求实体
	 * @param model   视图模型
	 * @return
	 */
	@RequiredLog("修改公司")
	@RequiresPermissions("manage:company:edit")
	@GetMapping("edit")
	public String editView(HttpServletRequest request, Model model)
	{
		// 获取公司id
		String id = request.getParameter("id");
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 查询公司信息
		Company company = companyService.findDataListByParams(params).get(0);
		model.addAttribute("company", company);
		return "manage/company/company-edit";
	}

	/**
	 * 修改公司
	 *
	 * @param company 公司实体
	 * @return
	 */
	@RequiredLog("修改公司操作")
	@RequiresPermissions("manage:company:update")
	@PutMapping("update")
	@ResponseBody
	public SysResult update(Company company)
	{
		companyService.updateCompany(company);
		return SysResult.ok();
	}

	/**
	 * 启用或停用公司
	 *
	 * @param request 请求实体
	 * @return
	 */
	@RequiredLog("启用停用公司")
	@RequiresPermissions("manage:company:activate")
	@RequestMapping("activate")
	@ResponseBody
	public SysResult activate(HttpServletRequest request)
	{
		int id = Integer.valueOf(request.getParameter("id"));
		String active = request.getParameter("active");
		String msg = String.valueOf(request.getParameter("msg"));
		Map<String, Object> result = companyService.activateOrInactivateCompany(id, active);
		if (Boolean.valueOf(InstandTool.objectToString(result.get("result"))))
		{
			return SysResult.build(200, String.format("公司%s成功", msg));
		}
		return SysResult.build(500, String.format("公司%s失败，请确认操作后重新尝试", msg));
	}
}
