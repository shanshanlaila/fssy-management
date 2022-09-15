package com.fssy.shareholder.management.controller.manage.department;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.service.manage.department.DepartmentRolePermissionService;
import com.fssy.shareholder.management.service.manage.permission.PermissionService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.tools.common.InstandTool;

/**
 * 组织结构中通过角色设置权限控制器
 * 
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/changePermission/")
public class DepartmentChangePermissionController
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DepartmentChangePermissionController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private DepartmentRolePermissionService departmentRolePermissionService;

	/**
	 * 返回变更对应组织结构和角色的权限页面
	 * @param request 请求实体
	 * @param model 模型对象
	 * @return
	 */
	@RequiredLog("组织岗位定责")
	@RequiresPermissions("manage:changePermission:edit")
	@GetMapping("edit")
	public String changePermissionView(HttpServletRequest request, Model model)
	{
		// 获取组织列表传来的组织结构id
		Long departmentId = Long.valueOf(request.getParameter("departmentId"));
		// 2022-01-20 添加从用户列表定责
		
		// 获取角色列表
		List<Long> selectedIds = new ArrayList<>();
		if (!ObjectUtils.isEmpty(request.getParameter("roleId")))
		{
			selectedIds.add(InstandTool.stringToLong(request.getParameter("roleId")));
		}
		Map<String, Object> roleParams = new HashMap<>();
		List<Map<String, Object>> roleList = roleService
				.findRoleSelectedDataListByParams(roleParams, selectedIds);
		// 获取页面传来的数据
		Map<String, Object> params = new HashMap<>();
		if (!ObjectUtils.isEmpty(request.getParameter("roleId")))
		{
			params.put("roleId", request.getParameter("roleId"));
		}
		else
		{
			if (!ObjectUtils.isEmpty(roleList))
			{
				params.put("roleId", roleList.get(0).get("id"));
				roleList.get(0).put("selected", true);
			}
		}
		if (!ObjectUtils.isEmpty(request.getParameter("departmentId")))
		{
			params.put("departmentId", request.getParameter("departmentId"));
		}

		List<Map<String, Object>> permissionList = departmentRolePermissionService
				.findDepartmentRolePermissionListByParams(params);
		List<String> resultPermissionIds = new ArrayList<String>();
		List<String> resultPermissionNames = new ArrayList<String>();
		for (Map<String, Object> map : permissionList)
		{
			resultPermissionIds.add(String.valueOf(map.get("id")));
			resultPermissionNames.add(String.valueOf(map.get("name")));
		}
		
		String resultPermissionIdStr = String.join(",", resultPermissionIds);
		String resultPermissionNameStr = String.join(",",
				resultPermissionNames);
		model.addAttribute("departmentId", departmentId);
		model.addAttribute("resultPermissionIds", resultPermissionIds);
		model.addAttribute("resultPermissionNames", resultPermissionNames);
		model.addAttribute("resultPermissionIdStr", resultPermissionIdStr);
		model.addAttribute("resultPermissionNameStr", resultPermissionNameStr);
		model.addAttribute("roleList", roleList);
		return "manage/department/department-permission-role-show";
	}

	/**
	 * 切换角色时，获取对应组织和角色时的用户
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public SysResult getPermissionInfos(HttpServletRequest request)
	{
		// 获取页面传来的数据
		Map<String, Object> params = new HashMap<>();
		if (!ObjectUtils.isEmpty(request.getParameter("roleId")))
		{
			params.put("roleId", request.getParameter("roleId"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("departmentId")))
		{
			params.put("departmentId", request.getParameter("departmentId"));
		}
		// 获取组织结构对应角色所关联的权限id
		List<Map<String, Object>> departmentRolePermissionList = departmentRolePermissionService
				.findDepartmentRolePermissionListByParams(params);
		List<Object> permissionIdList = new ArrayList<>();
		for (Map<String, Object> map : departmentRolePermissionList)
		{
			permissionIdList.add(map.get("id"));
		}
		// 查询所有权限信息
		params = new HashMap<>();
		params.put("permissionIdsArr", permissionIdList);
		LOGGER.error(params.toString());
		List<Permission> permissionList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(permissionIdList))
		{
			permissionList = permissionService.findPermissionDataListByParams(params);
		}
		return SysResult.ok(permissionList);
	}

	/**
	 * 点开权限选择时，获取ztree数据
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getZtreeObjects")
	@ResponseBody
	public SysResult getZtreePermissionInfos(HttpServletRequest request)
	{
		String permissionIdsStr = String
				.valueOf(request.getParameter("permissionIds"));

		List<Map<String, Object>> results = departmentRolePermissionService
				.findZtreeCheckedPermissionList(permissionIdsStr);
		return SysResult.ok(results);
	}

	/**
	 * 为组织结构中的一个角色设置权限
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@RequiredLog("组织岗位定责操作")
	@RequiresPermissions("manage:changePermission:store")
	@PostMapping("store")
	@ResponseBody
	public SysResult changePermission(HttpServletRequest request)
	{
		// 获取departmentId,roleId还有permissionId列表
		Long departmentId = Long.valueOf(request.getParameter("departmentId"));
		Long roleId = Long.valueOf(request.getParameter("roleId"));
		String permissionListStr = request.getParameter("permissionIds");
		List<String> permissionList = Arrays
				.asList(permissionListStr.split(","));
		LOGGER.debug(permissionList.toString());
		boolean result = departmentRolePermissionService
				.changeRolePermissionFromDepartment(departmentId, roleId,
						permissionList);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(200, "清空岗位职责成功");
	}
}
