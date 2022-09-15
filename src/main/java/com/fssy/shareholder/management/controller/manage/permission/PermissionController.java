package com.fssy.shareholder.management.controller.manage.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fssy.shareholder.management.annotation.RequiredLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.pojo.manage.system.System;
import com.fssy.shareholder.management.service.manage.permission.PermissionService;
import com.fssy.shareholder.management.service.manage.system.SystemService;

/**
 * 权限管理控制器
 * 
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/permission/")
public class PermissionController
{
	public static final Logger LOGGER = LoggerFactory
			.getLogger(PermissionController.class);

	@Autowired
	private SystemService systemService;

	@Autowired
	private PermissionService permissionService;

	/**
	 * 返回权限列表页面
	 * 
	 * @return
	 */
	@RequiredLog("权限管理")
	@RequiresPermissions("manage:permission:index")
	@GetMapping("index")
	public String permissionIndex(HttpServletRequest request, Model model)
	{
		// 获取system
		String system = request.getParameter("system");
		// 获取所有的system
		List<System> systems = systemService
				.findSystemDataListByParams(new HashMap<String, Object>());
		// 添加到模型
		model.addAttribute("system",
				system == null ? 1 : Integer.parseInt(system));
		model.addAttribute("systems", systems);
		return "manage/permission/permission-list";
	}

	/**
	 * 返回权限列表页面数据
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public List<Permission> getPermissionDatas(HttpServletRequest request)
	{
		Map<String, Object> params = new HashMap<>();
		// 构建参数查找表
		if (!ObjectUtils.isEmpty(request.getParameter("system")))
		{
			params.put("system", request.getParameter("system"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("permissionIds")))
		{
			params.put("permissionIds", request.getParameter("permissionIds"));
		}
		List<Permission> permissions = permissionService
				.findPermissionDataListByParams(params);
		return permissions;
	}

	/**
	 * 返回创建页面
	 * 
	 * @param request 请求实体
	 * @param model   视图对象
	 * @return
	 */
	@RequiredLog("添加权限")
	@RequiresPermissions("manage:permission:create")
	@GetMapping("create")
	public String createView(HttpServletRequest request, Model model)
	{
		// 获取页面传过来的system
		String system = request.getParameter("system");
		model.addAttribute("system", system);
		return "manage/permission/permission-create";
	}

	/**
	 * 创建权限
	 * 
	 * @param permission 权限实体
	 * @return
	 */
	@RequiredLog("添加权限操作")
	@RequiresPermissions("manage:permission:store")
	@PostMapping("store")
	@ResponseBody
	public SysResult store(Permission permission)
	{
		permissionService.savePermission(permission);
		return SysResult.ok();
	}

	/**
	 * 返回修改页面
	 * 
	 * @param request 请求实体
	 * @param model   视图模型
	 * @return
	 */
	@RequiredLog("修改权限")
	@RequiresPermissions("manage:permission:edit")
	@GetMapping("edit")
	public String editView(HttpServletRequest request, Model model)
	{
		// 获取权限id
		int id = Integer.parseInt(request.getParameter("id"));
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 查询权限信息
		Permission permission = permissionService
				.findPermissionDataListByParams(params).get(0);
		LOGGER.info("permission name :" + permission.getName());
		// 查询父级系统名称
		params.put("id", permission.getParent());
		List<Permission> parentPermissions = permissionService
				.findPermissionDataListByParams(params);
		Permission parentPermission = new Permission();
		if (!parentPermissions.isEmpty())
		{
			parentPermission = parentPermissions.get(0);
		}
		model.addAttribute("system", permission.getSystem());
		model.addAttribute("permission", permission);
		model.addAttribute("parentPermission", parentPermission);
		return "manage/permission/permission-edit";
	}

	/**
	 * 修改权限
	 * 
	 * @param permission 权限实体
	 * @return
	 */
	@RequiredLog("修改权限操作")
	@RequiresPermissions("manage:permission:update")
	@PutMapping("update")
	@ResponseBody
	public SysResult update(Permission permission)
	{
		LOGGER.info(permission.toString());
		boolean result = permissionService.updatePermission(permission);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "权限没有更新，请检查数据后重新尝试");
	}

	/**
	 * 删除权限
	 * 
	 * @param id 权限表主键
	 * @return
	 */
	@RequiredLog("删除权限操作")
	@RequiresPermissions("manage:permission:destroy")
	@DeleteMapping("{id}")
	@ResponseBody
	public SysResult destroy(@PathVariable(value = "id") Integer id)
	{
		boolean result = permissionService.deletePermissionById(id);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "权限没有删除，请确认操作后重新尝试");
	}
}
