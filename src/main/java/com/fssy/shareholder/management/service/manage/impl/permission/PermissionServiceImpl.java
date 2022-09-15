/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-18 	      添加权限时，也需要清空缓存
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-04-08 	      修改问题，未修改父级权限也不能提交
 */
package com.fssy.shareholder.management.service.manage.impl.permission;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.service.manage.permission.PermissionService;
import com.fssy.shareholder.management.tools.common.UserCacheTool;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRolePermissionMapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRolePermission;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * 
 * @author Solomon
 */
@Service
public class PermissionServiceImpl implements PermissionService
{
	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private DepartmentRolePermissionMapper departmentRolePermissionMapper;

	@Autowired
	private UserCacheTool userCacheTool;

	@Override
	@Transactional
	public Permission savePermission(Permission permission)
	{
		// 当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		// 1.写入新数据
		permissionMapper.insert(permission);

		// 2.设置父节点数据的leaf字段
		// 满足条件父节点存在且添加权限为菜单时
		if (permission.getType() == CommonConstant.PERMISSION_TYPE_FOR_MENU)
		{
			UpdateWrapper<Permission> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", permission.getParent())
					.set("leaf", CommonConstant.FALSE)
					.set("updatedAt", LocalDateTime.now())
					.set("updatedId", user.getId())
					.set("updatedName", user.getName());
			permissionMapper.update(null, updateWrapper);
		}


		// 2022-01-18 添加权限时，也需要清空缓存
		// 清除所有用户shiro框架使用的缓存
		userCacheTool.removeAllAuthorizationCache();
		
		return permission;
	}

	@Override
	@Transactional
	public boolean deletePermissionById(int id)
	{
		// 业务判断
		// 删除项存在子项时不允许删除
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", id);
		if (permissionMapper.selectCount(queryWrapper) > 0)
		{
			throw new ServiceException("该权限存在子项，需要先对子项进行操作");
		}

		// 当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		// 删除项是菜单时，如果父项只有一个菜单子项，需要修改父项的leaf属性
		Permission deletePermission = permissionMapper.selectById(id);
		QueryWrapper<Permission> allChildMenuQueryWrapper = new QueryWrapper<>();
		allChildMenuQueryWrapper.eq("parent", deletePermission.getParent())
				.eq("type", CommonConstant.PERMISSION_TYPE_FOR_MENU);
		if (deletePermission
				.getType() == CommonConstant.PERMISSION_TYPE_FOR_MENU
				&& permissionMapper.selectCount(allChildMenuQueryWrapper) == 1)
		{
			UpdateWrapper<Permission> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", deletePermission.getParent())
					.set("leaf", CommonConstant.TRUE)
					.set("updatedAt", LocalDateTime.now())
					.set("updatedId", user.getId())
					.set("updatedName", user.getName());
			permissionMapper.update(null, updateWrapper);
		}

		// 删除组织结构，角色，权限对照表数据
		QueryWrapper<DepartmentRolePermission> deletePermissionRelationQueryWrapper = new QueryWrapper<>();
		deletePermissionRelationQueryWrapper.eq("permissionId", id);
		departmentRolePermissionMapper
				.delete(deletePermissionRelationQueryWrapper);

		int num = permissionMapper.deleteById(id);

		// 清除所有用户shiro框架使用的缓存
		userCacheTool.removeAllAuthorizationCache();

		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<Permission> findPermissionDataListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		// 系统权限id查询
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		// 系统权限id列表查询
		if (params.containsKey("permissionIds"))
		{
			String permissionIdsStr = String
					.valueOf(params.get("permissionIds"));
			List<String> permissionIdList = Arrays
					.asList(permissionIdsStr.split(","));
			queryWrapper.in("id", permissionIdList);
		}
		// 系统权限id列表查询
		if (params.containsKey("permissionIdsArr"))
		{
			@SuppressWarnings("unchecked")
			List<String> permissionIdList = (List<String>) params
					.get("permissionIdsArr");
			queryWrapper.in("id", permissionIdList);
		}
		// 系统功能查询
		if (params.containsKey("system"))
		{
			queryWrapper.eq("`system`", params.get("system"));
		}
		// 权限名称查询
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		// 请求路径查询
		if (params.containsKey("route"))
		{
			queryWrapper.like("route", params.get("route"));
		}
		// 是否为叶子节点查询
		if (params.containsKey("leaf"))
		{
			queryWrapper.eq("leaf", params.get("leaf"));
		}
		// 是否为通用权限查询
		if (params.containsKey("isNormal"))
		{
			queryWrapper.eq("isNormal", params.get("isNormal"));
		}
		// 根据父id查询
		if (params.containsKey("parent"))
		{
			queryWrapper.eq("parent", params.get("parent"));
		}
		// 权限类型
		if (params.containsKey("type"))
		{
			queryWrapper.eq("type", params.get("type"));
		}
		List<Permission> permissions = permissionMapper
				.selectList(queryWrapper);
		return permissions;
	}

	@Override
	@Transactional
	public boolean updatePermission(Permission permission)
	{
		// 业务校验
		// 修改项存在子项且修改父级时不允许修改
		QueryWrapper<Permission> oriPermissionQueryWrapper = new QueryWrapper<>();
		oriPermissionQueryWrapper.eq("id", permission.getId());
		Permission oriPermission = permissionMapper
				.selectOne(oriPermissionQueryWrapper);
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", permission.getId());
		// 2022-04-08 修改问题，未修改父级权限也不能提交
		if (permissionMapper.selectCount(queryWrapper) > 0
				&& !oriPermission.getParent().equals(permission.getParent()))
		{
			throw new ServiceException("该权限存在子项，需要先对子项进行操作");
		}
		// 新父级为按钮时，不允许添加菜单
		QueryWrapper<Permission> currentParentQueryWrapper = new QueryWrapper<>();
		currentParentQueryWrapper.eq("id", permission.getParent()).eq("type",
				CommonConstant.PERMISSION_TYPE_FOR_BUTTON);
		if (permissionMapper.selectCount(currentParentQueryWrapper) == 1)
		{
			throw new ServiceException("不允许为按钮项添加子菜单");
		}

		// 当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		// 判断是否修改原来的父节点权限的leaf
		// 条件为父权限只有一个菜单权限，并且此权限为正在修改的权限
		QueryWrapper<Permission> oriChildPermissionQueryWrapper = new QueryWrapper<>();
		oriChildPermissionQueryWrapper.eq("parent", oriPermission.getParent())
				.eq("type", CommonConstant.PERMISSION_TYPE_FOR_MENU);
		List<Permission> oriChildPermissionsForMenuList = permissionMapper
				.selectList(oriChildPermissionQueryWrapper);
		if (oriChildPermissionsForMenuList.size() == 1
				&& oriChildPermissionsForMenuList.get(0)
						.getId() == oriPermission.getId())
		{
			UpdateWrapper<Permission> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", oriPermission.getParent())
					.set("leaf", CommonConstant.TRUE)
					.set("updatedAt", LocalDateTime.now())
					.set("updatedId", user.getId())
					.set("updatedName", user.getName());
			permissionMapper.update(null, updateWrapper);
		}

		// 判断是否修改新的父节点为权限的leaf
		// 条件为编辑的权限为菜单
		if (permission.getType() == CommonConstant.PERMISSION_TYPE_FOR_MENU)
		{
			UpdateWrapper<Permission> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", permission.getParent())
					.set("leaf", CommonConstant.FALSE)
					.set("updatedAt", LocalDateTime.now())
					.set("updatedId", user.getId())
					.set("updatedName", user.getName());
			permissionMapper.update(null, updateWrapper);
		}

		int num = permissionMapper.updateById(permission);

		// 清除所有用户shiro框架使用的缓存
		userCacheTool.removeAllAuthorizationCache();

		if (num > 0)
		{
			return true;
		}
		return false;
	}

}
