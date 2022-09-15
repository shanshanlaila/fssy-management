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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.service.manage.department.DepartmentRoleUserService;
import com.fssy.shareholder.management.service.manage.role.RoleService;

/**
 * 组织结构中通过角色设置用户控制器
 * 
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/changeUser/")
public class DepartmentChangeUserController
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DepartmentChangeUserController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private DepartmentRoleUserService departmentRoleUserService;

	/**
	 * 返回变更对应组织结构和角色的用户页面
	 * @param request 请求实体
	 * @param model 模型对象
	 * @return
	 */
	@RequiredLog("组织员工定岗")
	@RequiresPermissions("manage:changeUser:edit")
	@GetMapping("edit")
	public String changeUserView(HttpServletRequest request, Model model)
	{
		// 获取组织列表传来的组织结构id
		Long departmentId = Long.valueOf(request.getParameter("departmentId"));
		// 获取角色列表
		List<Long> selectedIds = new ArrayList<>();
		Map<String, Object> roleParams = new HashMap<>();
		List<Map<String, Object>> roleList = roleService
				.findRoleSelectedDataListByParams(roleParams, selectedIds);
		Long roleId = null;
		if (ObjectUtils.isNotEmpty(roleList))
		{
			roleId = (Long) roleList.get(0).get("value");
			roleList.get(0).put("selected", true);
		}
		LOGGER.debug(roleId.toString());
		List<Map<String, Object>> userList = departmentRoleUserService
				.findSelectedUserList(departmentId, roleId);
		LOGGER.debug(userList.toString());
		LOGGER.debug(roleList.toString());
		model.addAttribute("departmentId", departmentId);
		model.addAttribute("userList", userList);
		model.addAttribute("roleList", roleList);
		return "manage/department/department-user-role-show";
	}

	/**
	 * 切换角色时，获取对应组织和角色时的用户
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public SysResult getUserInfos(HttpServletRequest request)
	{
		// 获取页面传来的数据
		Long departmentId = Long.valueOf(request.getParameter("departmentId"));
		Long roleId = Long.valueOf(request.getParameter("roleId"));

		List<Map<String, Object>> userList = departmentRoleUserService
				.findSelectedUserList(departmentId, roleId);

		return SysResult.ok(userList);
	}

	/**
	 * 为组织结构中的一个角色设置用户
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@RequiredLog("组织员工定岗操作")
	@RequiresPermissions("manage:changeUser:store")
	@PostMapping("store")
	@ResponseBody
	public SysResult changeUser(HttpServletRequest request)
	{
		// 获取departmentId,roleId还有userId列表
		Long departmentId = Long.valueOf(request.getParameter("departmentId"));
		Long roleId = Long.valueOf(request.getParameter("roleId"));
		String userListStr = request.getParameter("userIds");
		List<String> userList = Arrays.asList(userListStr.split(","));
		LOGGER.debug(userList.toString());
		boolean result = departmentRoleUserService
				.changeRoleUserFromDepartment(departmentId, roleId, userList);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(200, "清空岗位用户成功");
	}

}
