package com.fssy.shareholder.management.controller.manage.log;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.log.ScheduleAuditLog;
import com.fssy.shareholder.management.service.manage.log.ScheduleAuditLogService;

/**
 * 定时任务记录管理控制器
 *
 * @author Solomon
 * @since 2022-12-16
 */
@Controller
@RequestMapping("/manage/scheduleAuditLog/")
public class ScheduleAuditLogController
{
	@Autowired
	private ScheduleAuditLogService scheduleAuditLogService;

	/**
	 * 返回审计日志列表页面
	 * 
	 * @return
	 */
	@RequiresPermissions("manage:schedule:log:index")
	@GetMapping("index")
	public String index(HttpServletRequest request, Model model)
	{
		return "manage/log/schedule-audit-log-list";
	}

	/**
	 * 返回审计日志列表页面数据
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getScheduleAuditLogDatas(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		Map<String, Object> result = new HashMap<>();
		// 获取limit和page
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		params.put("limit", limit);
		params.put("page", page);
		Page<ScheduleAuditLog> scheduleAuditLogPage = scheduleAuditLogService
				.findScheduleAuditLogDataListPerPageByParams(params);
		if (scheduleAuditLogPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");

		}
		else
		{
			result.put("code", 0);
			result.put("count", scheduleAuditLogPage.getTotal());
			result.put("data", scheduleAuditLogPage.getRecords());
		}
		return result;
	}

	/**
	 * 删除定时任务日志
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@RequiresPermissions("manage:schedule:log:destroy")
	@DeleteMapping("destroy")
	@ResponseBody
	public SysResult destroy(@RequestParam("ids[]") String[] ids)
	{
		List<String> scheduleAuditLogIdList = Arrays.asList(ids);
		scheduleAuditLogService.deleteScheduleAuditLogByIds(scheduleAuditLogIdList);
		return SysResult.ok();
	}

	private Map<String, Object> getParams(HttpServletRequest request)
	{
		// region getParams
		Map<String, Object> params = new HashMap<String, Object>();
		// 定时任务记录表主键查询
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
		// 状态查询
		if (!ObjectUtils.isEmpty(request.getParameter("status")))
		{
			params.put("status", request.getParameter("status"));
		}
		// 操作人查询
		if (!ObjectUtils.isEmpty(request.getParameter("createdName")))
		{
			params.put("createdName", request.getParameter("createdName"));
		}
		// 备注查询
		if (!ObjectUtils.isEmpty(request.getParameter("note")))
		{
			params.put("note", request.getParameter("note"));
		}
		// 操作时间开始查询
		if (!ObjectUtils.isEmpty(request.getParameter("createdAtStart")))
		{
			params.put("createdAtStart", request.getParameter("createdAtStart"));
		}
		// 操作时间结束查询
		if (!ObjectUtils.isEmpty(request.getParameter("createdAtEnd")))
		{
			params.put("createdAtEnd", request.getParameter("createdAtEnd"));
		}
		// endregion
		return params;
	}
}
