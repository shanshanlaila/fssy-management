package com.fssy.shareholder.management.controller.manage.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;

/**
 * 组织结构功能控制器
 * 
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/department/")
public class DepartmentController
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DepartmentController.class);

	/**
	 * 组织结构功能业务实现类
	 */
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 返回组织结构列表页面
	 * 
	 * @return
	 */
	@RequiredLog("组织结构管理")
	@RequiresPermissions("manage:department:index")
	@GetMapping("index")
	public String departmentIndex()
	{
		return "manage/department/department-list";
	}

	/**
	 * 返回组织结构页面数据
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public List<Department> getDepartmentDatas(HttpServletRequest request)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		List<Department> departmentList = departmentService
				.findDepartmentDataListByParams(params);
		return departmentList;
	}

	/**
	 * 返回创建页面
	 * 
	 * @param request 请求实体
	 * @param model   model对象
	 * @return 页面
	 */
	@RequiredLog("添加组织结构")
	@RequiresPermissions("manage:department:create")
	@GetMapping("create")
	public String createView(HttpServletRequest request, Model model)
	{
		return "manage/department/department-create";
	}

	/**
	 * 创建组织结构
	 * 
	 * @param department 组织结构实体
	 * @return
	 */
	@RequiredLog("添加组织结构操作")
	@RequiresPermissions("manage:department:store")
	@PostMapping("store")
	@ResponseBody
	public SysResult store(Department department)
	{
		departmentService.saveDepartment(department);
		return SysResult.ok();
	}

	/**
	 * 返回修改页面
	 * 
	 * @param request 请求实体
	 * @param model   视图模型
	 * @return
	 */
	@RequiredLog("修改组织结构")
	@RequiresPermissions("manage:department:edit")
	@GetMapping("edit")
	public String editView(HttpServletRequest request, Model model)
	{
		// 获取组织结构id
		String id = request.getParameter("id");
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		// 查询组织结构信息
		Department department = departmentService
				.findDepartmentDataListByParams(params).get(0);
		LOGGER.info("department name :" + department.getName());
		// 查询父级系统名称
		params.put("id", department.getParent());
		List<Department> parentDepartments = departmentService
				.findDepartmentDataListByParams(params);
		Department parentDepartment = new Department();
		if (!parentDepartments.isEmpty())
		{
			parentDepartment = parentDepartments.get(0);
		}
		model.addAttribute("department", department);
		model.addAttribute("parentDepartment", parentDepartment);
		model.addAttribute("departmentIdForManufacture", null);
		// 2021-12-24 添加工厂
		List<String> selectedIds = new ArrayList<>();
		if (!ObjectUtils.isEmpty(department.getFactoryId()))
		{
			selectedIds.add(department.getFactoryId().toString());
		}
		return "manage/department/department-edit";
	}

	/**
	 * 修改组织结构
	 * 
	 * @param department 组织结构实体
	 * @return
	 */
	@RequiredLog("修改组织结构操作")
	@RequiresPermissions("manage:department:update")
	@PutMapping("update")
	@ResponseBody
	public SysResult update(Department department)
	{
		boolean result = departmentService.updateDepartment(department);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "组织结构没有更新，请检查数据后重新尝试");
	}

	/**
	 * 操作组织结构
	 * 
	 * @param id 组织结构表主键
	 * @return
	 */
	@RequiredLog("删除组织结构操作")
	@RequiresPermissions("manage:department:destroy")
	@DeleteMapping("{id}")
	@ResponseBody
	public SysResult destroy(@PathVariable(value = "id") Integer id)
	{
		boolean result = departmentService.deleteById(id);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "组织结构没有删除，请确认操作后重新尝试");
	}
}
