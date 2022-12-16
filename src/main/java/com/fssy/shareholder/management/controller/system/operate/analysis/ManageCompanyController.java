package com.fssy.shareholder.management.controller.system.operate.analysis;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.service.system.operate.analysis.ManageCompanyService;

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
}
