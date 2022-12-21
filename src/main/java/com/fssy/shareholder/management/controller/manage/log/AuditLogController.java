/**
 * 
 */
package com.fssy.shareholder.management.controller.manage.log;

import java.text.SimpleDateFormat;
import java.util.*;

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
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.log.AuditLog;
import com.fssy.shareholder.management.service.manage.log.AuditLogService;

/**
 * 审计日志管理控制器
 * 
 * @author Solomon
 *
 */
@Controller
@RequestMapping("/manage/auditLog/")
public class AuditLogController
{
	@Autowired
	private AuditLogService auditLogService;

	/**
	 * 返回审计日志列表页面
	 * 
	 * @return
	 */
	@RequiresPermissions("manage:log:index")
	@GetMapping("index")
	public String auditLogIndex(HttpServletRequest request, Model model)
	{
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		Date date = calendar.getTime();
		String createdAtStart = sdf.format(date);
		model.addAttribute("createdAtStart", createdAtStart);

		return "manage/log/audit-log-list";
	}

	/**
	 * 返回审计日志列表页面数据
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getAuditLogDatas(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		Map<String, Object> result = new HashMap<>();
		// 获取limit和page
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		params.put("limit", limit);
		params.put("page", page);
		Page<AuditLog> auditLogPage = auditLogService
				.findAuditLogDataListPerPageByParams(params);
		if (auditLogPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");

		}
		else
		{
			result.put("code", 0);
			result.put("count", auditLogPage.getTotal());
			result.put("data", auditLogPage.getRecords());
		}
		return result;
	}

	/**
	 * 删除审计日志
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@RequiredLog("删除日志操作")
	@RequiresPermissions("manage:log:destroy")
	@DeleteMapping("destroy")
	@ResponseBody
	public SysResult destroy(@RequestParam("ids[]") String[] ids)
	{
		List<String> auditLogIdList = Arrays
				.asList(ids);
		auditLogService.deleteAuditLogByIds(auditLogIdList);
		return SysResult.ok();
	}
	
	private Map<String, Object> getParams(HttpServletRequest request)
	{
		// region getParams
		Map<String, Object> params = new HashMap<String, Object>();
		// 操作人查询
		if (!ObjectUtils.isEmpty(request.getParameter("name")))
		{
			params.put("name", request.getParameter("name"));
		}
		// 操作功能查询
		if (!ObjectUtils.isEmpty(request.getParameter("operation")))
		{
			params.put("operation", request.getParameter("operation"));
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
		// 操作状态查询
		if (!ObjectUtils.isEmpty(request.getParameter("status")))
		{
			params.put("status", request.getParameter("status"));
		}
		// endregion
		return params;
	}
}
