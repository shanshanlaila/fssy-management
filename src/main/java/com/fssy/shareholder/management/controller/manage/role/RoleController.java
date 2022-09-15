package com.fssy.shareholder.management.controller.manage.role;

import java.util.HashMap;
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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.role.Role;
import com.fssy.shareholder.management.service.manage.role.RoleService;

/**
 * 角色功能控制器
 * 
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/role/")
public class RoleController
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	/**
	 * 返回角色列表页面
	 * @return
	 */
	@RequiredLog("岗位管理")
	@RequiresPermissions("manage:role:index")
	@GetMapping("index")
	public String roleIndex()
	{
		return "manage/role/role-list";
	}

	/**
	 * 返回角色列表页面数据
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getRoleDatas(HttpServletRequest request)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		// 获取limit和page
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		params.put("limit", limit);
		params.put("page", page);
		if (!ObjectUtils.isEmpty(request.getParameter("name")))
		{
			params.put("name", request.getParameter("name"));
		}
		LOGGER.debug(params.toString());
		Page<Role> rolePage = roleService.findRoleDataListPerPageByParams(params);
		if (rolePage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");

		}
		else
		{
			result.put("code", 0);
			result.put("count", rolePage.getTotal());
			result.put("data", rolePage.getRecords());
		}
		return result;
	}
	
	/**
	 * 返回创建页面
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@RequiredLog("创建岗位")
	@RequiresPermissions("manage:role:create")
	@GetMapping("create")
	public String createView(HttpServletRequest request)
	{
		return "manage/role/role-create";
	}
	
	/**
	 * 创建角色
	 * 
	 * @param role 角色实体
	 * @return
	 */
	@RequiredLog("添加岗位操作")
	@RequiresPermissions("manage:role:store")
	@PostMapping("store")
	@ResponseBody
	public SysResult store(Role role)
	{
		roleService.saveRole(role);
		return SysResult.ok();
	}
	
	/**
	 * 返回修改页面
	 * 
	 * @param request 请求实体
	 * @param model   视图模型
	 * @return
	 */
	@RequiredLog("修改岗位")
	@RequiresPermissions("manage:role:edit")
	@GetMapping("edit")
	public String editView(HttpServletRequest request, Model model)
	{
		// 获取角色id
		String id = request.getParameter("id");
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 查询角色信息
		Role role = roleService.findRoleDataListByParams(params).get(0);
		LOGGER.info("role name :" + role.getName());
		model.addAttribute("role", role);
		return "manage/role/role-edit";
	}
	
	/**
	 * 修改角色
	 * 
	 * @param role 角色实体
	 * @return
	 */
	@RequiredLog("修改岗位操作")
	@RequiresPermissions("manage:role:update")
	@PutMapping("update")
	@ResponseBody
	public SysResult update(Role role)
	{
		boolean result = roleService.updateRole(role);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "角色没有更新，请检查数据后重新尝试");
	}

	/**
	 * 删除角色
	 * @param id 角色表主键
	 * @return
	 */
	@RequiredLog("删除岗位操作")
	@RequiresPermissions("manage:role:destroy")
	@DeleteMapping("{id}")
	@ResponseBody
	public SysResult destroy(@PathVariable(value = "id") Integer id)
	{
		boolean result = roleService.deleteRoleById(id);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "角色没有删除，请确认操作后重新尝试");
	}
}
