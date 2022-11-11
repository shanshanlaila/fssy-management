/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
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
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleService;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 事件清单主担岗位关系表 *****数据表名：
 * bs_performance_events_relation_main_role *****数据表作用： 事件清单对应的主担岗位 *****变更记录：
 * 时间 变更人 变更内容 20220915 兰宇铧 初始设计 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-10-10
 */
@Controller
@RequestMapping("/system/performance/employee/events-relation-role/")
public class EventsRelationRoleController
{
	/**
	 * 事件清单岗位关系功能业务实现类
	 */
	@Autowired
	private EventsRelationRoleService eventsRelationRoleService;

	/**
	 * 组织结构功能业务实现类
	 */
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 角色功能业务实现类
	 */
	@Autowired
	private RoleService roleService;

	/**
	 * 返回事件清单岗位关系管理页面
	 *
	 * @param model model对象
	 * @return
	 */
	@RequiredLog("事件清单岗位关系管理")
	@RequiresPermissions("performance:employee:event:relation:role:index")
	@GetMapping("index")
	public String index(Model model)
	{
		Map<String, Object> params = new HashMap<>();
		List<String> selectedIds = new ArrayList<>();
		List<Map<String, Object>> departmentList = departmentService
				.findDepartmentsSelectedDataListByParams(params, selectedIds);
		model.addAttribute("departmentList", departmentList);
		params = new HashMap<>();
		List<Long> longSelectedIds = new ArrayList<>();
		List<Map<String, Object>> roleList = roleService.findRoleSelectedDataListByParams(params,
				longSelectedIds);
		model.addAttribute("roleList", roleList);
		return "system/performance/employee/performance-event-relation-role-list";
	}

	/**
	 * 获取数据格式
	 *
	 * @param request 请求对象
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getObjects(HttpServletRequest request)
	{
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = getParams(request);
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		params.put("limit", limit);
		params.put("page", page);

		Page<EventsRelationRole> performanceEventsRelationRolePage = eventsRelationRoleService
				.findPerformanceEventsRelationRoleDataListPerPageByParams(params);

		if (performanceEventsRelationRolePage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查到任何数据");
		}
		else
		{
			result.put("code", 0);
			result.put("count", performanceEventsRelationRolePage.getTotal());
			result.put("data", performanceEventsRelationRolePage.getRecords());
		}

		return result;
	}

	private Map<String, Object> getParams(HttpServletRequest request)
	{
		Map<String, Object> params = new HashMap<>();
		// 事件清单岗位关系表主键查询
		if (!ObjectUtils.isEmpty(request.getParameter("id")))
		{
			params.put("id", request.getParameter("id"));
		}
		// 事件清单岗位关系表主键列表查询
		if (!ObjectUtils.isEmpty(request.getParameter("ids")))
		{
			String idsStr = request.getParameter("ids");
			List<String> ids = Arrays.asList(idsStr.split(","));
			params.put("ids", ids);
		}
		// 事件表主键主键查询
		if (!ObjectUtils.isEmpty(request.getParameter("eventsId")))
		{
			params.put("eventsId", request.getParameter("eventsId"));
		}
		// 事件表主键主键列表查询
		if (!ObjectUtils.isEmpty(request.getParameter("eventsIds")))
		{
			String eventsIdStr = request.getParameter("eventsIds");
			List<String> eventsId = Arrays.asList(eventsIdStr.split(","));
			params.put("eventsId", eventsId);
		}
		// 岗位名称精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("roleNameEq")))
		{
			params.put("roleName", request.getParameter("roleNameEq"));
		}
		// 岗位主键精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("roleId")))
		{
			params.put("roleId", request.getParameter("roleId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("year")))
		{
			params.put("year", request.getParameter("year"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("month")))
		{
			params.put("month", request.getParameter("month"));
		}
		// 生效日期查询
		if (!ObjectUtils.isEmpty(request.getParameter("activeDateStart")))
		{
			params.put("activeDate", request.getParameter("activeDateStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("activeDateEnd")))
		{
			params.put("activeDate", request.getParameter("activeDateEnd"));
		}
		// 编制日期查询
		if (!ObjectUtils.isEmpty(request.getParameter("createDateStart")))
		{
			params.put("createDate", request.getParameter("createDateStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("createDateEnd")))
		{
			params.put("createDate", request.getParameter("createDateEnd"));
		}
		// 部门名称精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("departmentNameEq")))
		{
			params.put("departmentName", request.getParameter("departmentNameEq"));
		}
		// 部门主键精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("departmentId")))
		{
			params.put("departmentId", request.getParameter("departmentId"));
		}
		// 事件类别精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("eventsTypeEq")))
		{
			params.put("eventsType", request.getParameter("eventsTypeEq"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("isMainOrNext")))
		{
			params.put("isMainOrNext", request.getParameter("isMainOrNext"));
		}
		// 用户名称精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("userNameEq")))
		{
			params.put("userName", request.getParameter("userNameEq"));
		}
		// 用户主键精确查询
		if (!ObjectUtils.isEmpty(request.getParameter("userId")))
		{
			params.put("userId", request.getParameter("userId"));
		}
		// 岗位名称查询
		if (!ObjectUtils.isEmpty(request.getParameter("roleName")))
		{
			params.put("roleName", request.getParameter("roleName"));
		}
		// 部门名称查询
		if (!ObjectUtils.isEmpty(request.getParameter("departmentName")))
		{
			params.put("departmentName", request.getParameter("departmentName"));
		}
		// 事件类别查询
		if (!ObjectUtils.isEmpty(request.getParameter("eventsType")))
		{
			params.put("eventsType", request.getParameter("eventsType"));
		}
		// 用户名称查询
		if (!ObjectUtils.isEmpty(request.getParameter("userName")))
		{
			params.put("userName", request.getParameter("userName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("idDesc")))
		{
			params.put("idDesc", request.getParameter("idDesc"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("groupBy")))
		{
			params.put("groupBy", request.getParameter("groupBy"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("select")))
		{
			params.put("select", request.getParameter("select"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("departmentIds")))
		{
			String departmentIdsStr = request.getParameter("departmentIds");
			List<String> departmentIds = Arrays.asList(departmentIdsStr.split(","));
			params.put("departmentIds", departmentIds);
		}
		if (!ObjectUtils.isEmpty(request.getParameter("roleIds")))
		{
			String roleIdsStr = request.getParameter("roleIds");
			List<String> roleIds = Arrays.asList(roleIdsStr.split(","));
			params.put("roleIds", roleIds);
		}
		return params;
	}

	/**
	 * excel导出(按钮：导出事件清单填报履职计划)
	 *
	 * @param request  请求
	 * @param response 响应
	 */
	@GetMapping("downloadForCharge")
	public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = getParams(request);
		params.put("select",
						"id,"+
						"eventsId," +
						"jobName," +
						"workEvents," +
						"departmentName," +
						"standardValue," +
						"eventsFirstType," +
						"isMainOrNext"
		);
		List<Map<String, Object>> relationRoleLists = eventsRelationRoleService.findRelationRoleMapDataByParams(params);

		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
		// 需要改背景色的格子
		fieldMap.put("id", "系统序号");
		fieldMap.put("eventsId", "事件清单序号");
		fieldMap.put("eventsFirstType", "事务类型");
		fieldMap.put("jobName", "工作职责");
		fieldMap.put("workEvents", "流程（工作事件）");
		fieldMap.put("departmentName", "部门");
		fieldMap.put("standardValue", "事件价值标准分");
		fieldMap.put("isMainOrNext", "主/次担");
		// 需要填写的部分
		fieldMap.put("jixiaoleixing", "*绩效类型");
		fieldMap.put("duiyingjihuaneirong", "*对应工作事件的计划内容");// planningWork
		fieldMap.put("pinci", "*频次");
		fieldMap.put("biaodan", "*表单（输出内容）");// planOutput
		fieldMap.put("jihuakaishishijian", "*计划开始时间");
		fieldMap.put("jihuawanchengshijian", "*计划完成时间");
		fieldMap.put("gangweimingcheng", "*岗位名称");
		fieldMap.put("gangweirenyuanxingming", "*岗位人员姓名");
		fieldMap.put("shengbaoyuefen", "*申报日期");
		// 标识字符串的列
		List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21);
		SheetOutputService sheetOutputService = new SheetOutputService();
		if (ObjectUtils.isEmpty(relationRoleLists)) {
			throw new ServiceException("未查出数据");
		}
		sheetOutputService.exportNum("履职管控表", relationRoleLists, fieldMap, response, strList, null);
	}
}
