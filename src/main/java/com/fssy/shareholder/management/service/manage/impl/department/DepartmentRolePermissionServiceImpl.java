/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-18 	  	 修改问题，已选择的权限，会出现两个
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-20   	 清楚所有用户权限缓存
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-21 		 允许清空岗位的所有职责功能
 */
package com.fssy.shareholder.management.service.manage.impl.department;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.tools.common.UserCacheTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRolePermissionMapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRolePermission;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.service.manage.department.DepartmentRolePermissionService;
import com.fssy.shareholder.management.tools.common.InstandTool;

/**
 * 组织结构功能设置角色权限业务实现类
 * 
 * @author Solomon
 */
@Service
public class DepartmentRolePermissionServiceImpl
		implements DepartmentRolePermissionService
{

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private DepartmentRolePermissionMapper mapper;

	@Autowired
	private UserCacheTool userCacheTool;

	@Override
	@Transactional
	public boolean changeRolePermissionFromDepartment(Long departmentId,
			Long roleId, List<String> permissionList)
	{
		// 删除原来组织和角色相应的权限
		QueryWrapper<DepartmentRolePermission> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("departmentId", departmentId).eq("roleId", roleId);
		mapper.delete(queryWrapper);

		// 添加组织和用户新的权限
		List<DepartmentRolePermission> entityList = new ArrayList<DepartmentRolePermission>();
		for (String permissionIdStr : permissionList)
		{
			if (ObjectUtils.isEmpty(permissionIdStr))
			{
				continue;
			}
			Long permissionId = Long.valueOf(permissionIdStr);
			DepartmentRolePermission entity = new DepartmentRolePermission(null, departmentId,
					roleId, permissionId);
			entityList.add(entity);
		}
		int num = 0;
		// 2022-01-21 允许清空岗位的所有职责功能
		if (!ObjectUtils.isEmpty(entityList))
		{
			num = mapper.insertBatchSomeColumn(entityList);
		}

		// 清除当前用户shiro框架使用的缓存
		// 2022-01-20 清楚所有用户权限缓存
		userCacheTool.removeAllAuthorizationCache();
		
		if (num > 0)
		{
			return true;
		}
		return false;
	}

	public List<Map<String, Object>> findSelectedPermissionList(
			Long departmentId, Long roleId)
	{
		QueryWrapper<DepartmentRolePermission> queryWrapper = new QueryWrapper<>();
		List<DepartmentRolePermission> departmentRolePermissionList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(roleId))
		{
			queryWrapper.eq("departmentId", departmentId).eq("roleId", roleId);
			departmentRolePermissionList = mapper.selectList(queryWrapper);
		}

		// 查询所有权限
		List<Permission> permissionList = permissionMapper.selectList(null);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的权限添加selected属性
		for (Permission permission : permissionList)
		{
			for (DepartmentRolePermission departmentRolePermission : departmentRolePermissionList)
			{
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("name", permission.getName());
				result.put("value", permission.getId());
				result.put("id", permission.getId());
				result.put("checked", false);
				if (departmentRolePermission.getPermissionId() == permission
						.getId())
				{
					result.put("checked", true);
					resultList.add(result);
					break;
				}
			}
		}
		return resultList;
	}

	@Override
	public List<Map<String, Object>> findZtreeCheckedPermissionList(
			String permissionIdStr)
	{
		// 处理字符串
		List<String> permissionIds = Arrays.asList(permissionIdStr.split(","));
		QueryWrapper<DepartmentRolePermission> queryWrapper = new QueryWrapper<>();
		// 2022-01-18 这里查询的是premissionId
		queryWrapper.in("permissionId", permissionIds);
		List<DepartmentRolePermission> departmentRolePermissionList = mapper
				.selectList(queryWrapper);

		// 查询所有权限
		List<Permission> permissionList = permissionMapper.selectList(null);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的权限添加checked属性
		for (Permission permission : permissionList)
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("name", permission.getName());
			result.put("id", permission.getId());
			result.put("parent", permission.getParent());
			result.put("checked", false);
			for (DepartmentRolePermission departmentRolePermission : departmentRolePermissionList)
			{
				if (departmentRolePermission.getPermissionId().equals(permission.getId()))
				{
					// 2022-01-18 这里不用添加，只需要修改
					result.put("checked", true);
					break;
				}
			}
			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public List<Map<String, Object>> findDepartmentRolePermissionListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<DepartmentRolePermission> queryWrapper = new QueryWrapper<>();
		// 权限id查询
		if (params.containsKey("departmentId"))
		{
			queryWrapper.eq("departmentId", params.get("departmentId"));
		}
		// 角色id查询
		if (params.containsKey("roleId"))
		{
			queryWrapper.eq("roleId", params.get("roleId"));
		}
		List<Map<String, Object>> results = mapper.selectMaps(queryWrapper);
		// 获取权限id列表，查询权限信息
		List<String> permissionIdsArr = new ArrayList<>();
		for (Map<String, Object> map : results)
		{
			permissionIdsArr.add(InstandTool.objectToString(map.get("permissionId")));
		}
		List<Map<String, Object>> resultPermissions = new ArrayList<>();
		if (!ObjectUtils.isEmpty(permissionIdsArr))
		{
			QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
			permissionQueryWrapper.in("id", permissionIdsArr);
			resultPermissions = permissionMapper
					.selectMaps(permissionQueryWrapper);
		}
		return resultPermissions;
	}

}
