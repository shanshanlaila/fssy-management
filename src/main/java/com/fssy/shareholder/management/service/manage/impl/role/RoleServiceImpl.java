/**
 * ------------------------修改日志---------------------------------
 * 修改人    修改日期                   修改内容
 * 兰宇铧    2022-01-21 	   添加从用户列表添加用户的岗位权限
 */
package com.fssy.shareholder.management.service.manage.impl.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.tools.common.UserCacheTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRolePermissionMapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRolePermission;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.role.Role;
import com.fssy.shareholder.management.service.manage.role.RoleService;

/**
 * 角色功能业务实现类
 * 
 * @author Solomon
 */
@Service
public class RoleServiceImpl implements RoleService
{
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private DepartmentRolePermissionMapper departmentRolePermissionMapper;
	
	@Autowired
	private DepartmentRoleUserMapper departmentRoleUserMapper;
	
	@Autowired
	private UserCacheTool userCacheTool;

	@Override
	@Transactional
	public Role saveRole(Role role)
	{
		// 写入数据
		roleMapper.insert(role);
		return role;
	}

	@Override
	@Transactional
	public boolean deleteRoleById(int id)
	{
		// 删除组织结构，角色，权限对照表数据
		QueryWrapper<DepartmentRolePermission> deletePermissionRelationQueryWrapper = new QueryWrapper<>();
		deletePermissionRelationQueryWrapper.eq("roleId", id);
		departmentRolePermissionMapper.delete(deletePermissionRelationQueryWrapper);
		
		// 删除缓缓结构，角色，用户对照表数据
		QueryWrapper<DepartmentRoleUser> deleteUserRelationQueryWrapper = new QueryWrapper<>();
		deleteUserRelationQueryWrapper.eq("roleId", id);
		departmentRoleUserMapper.delete(deleteUserRelationQueryWrapper);
		
		int num = roleMapper.deleteById(id);
		
		// 清除所有用户shiro框架使用的缓存
		userCacheTool.removeAllAuthorizationCache();
		
		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<Role> findRoleDataListByParams(Map<String, Object> params)
	{
		QueryWrapper<Role> queryWrapper = getQueryWrapper(params);
		List<Role> roleList = roleMapper.selectList(queryWrapper);
		return roleList;
	}

	@Override
	public Page<Role> findRoleDataListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<Role> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Role> myPage = new Page<>(page, limit);
		Page<Role> rolePage = roleMapper.selectPage(myPage, queryWrapper);
		return rolePage;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean updateRole(Role role)
	{
		int num = roleMapper.updateById(role);
		
		// 清除当前用户shiro框架使用的缓存
		userCacheTool.removeCurrentUserAuthorizationCache();
		
		if (num > 0)
		{
			return true;
		}
		return false;
	}

	private QueryWrapper<Role> getQueryWrapper(Map<String, Object> params)
	{
		QueryWrapper<Role> queryWrapper = new QueryWrapper<>();

		// 主键查询
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		// 用户名称查询
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}

		return queryWrapper;
	}

	@Override
	public List<Map<String, Object>> findRoleSelectedDataListByParams(Map<String, Object> params,
			List<Long> selectedIds)
	{
		QueryWrapper<Role> queryWrapper = getQueryWrapper(params);
		// 查询所有角色
		List<Role> roleList = roleMapper.selectList(queryWrapper);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的用户角色添加selected属性
		// 2022-01-21 添加已经选择项
		for (Role role : roleList)
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("name", role.getName());
			result.put("value", role.getId());
			result.put("id", role.getId());
			result.put("selected", false);
			if (selectedIds.contains(role.getId()))
			{
				result.put("selected", true);
			}
			resultList.add(result);
		}
		return resultList;
	}
}
