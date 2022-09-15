package com.fssy.shareholder.management.service.manage.permission;

import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.pojo.manage.permission.Permission;

/**
 * 权限功能业务接口
 * 
 * @author Solomon
 */
public interface PermissionService
{
	/**
	 * 添加权限数据
	 * 
	 * @param permission 权限实体
	 * @return 权限实体
	 */
	Permission savePermission(Permission permission);

	/**
	 * 通过主键删除权限
	 * 
	 * @param id 权限主键
	 * @return true/false
	 */
	boolean deletePermissionById(int id);

	/**
	 * 通过查询条件查询权限列表
	 * 
	 * @param params 查询条件
	 * @return 权限列表数据
	 */
	List<Permission> findPermissionDataListByParams(Map<String, Object> params);

	/**
	 * 修改权限数据
	 * 
	 * @param permission 权限实体
	 * @return true/false
	 */
	boolean updatePermission(Permission permission);
}
