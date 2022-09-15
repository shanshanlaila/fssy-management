package com.fssy.shareholder.management.service.manage.department;

import java.util.List;
import java.util.Map;

/**
 * 组织结构功能设置角色的权限业务接口
 * 
 * @author Solomon
 */
public interface DepartmentRolePermissionService
{
	/**
	 * 通过组织机构设置一个角色的权限
	 * 
	 * @param departmentId   组织结构表主键
	 * @param roleId         角色表主键
	 * @param permissionList 权限表主键列表
	 * @return
	 */
	boolean changeRolePermissionFromDepartment(Long departmentId, Long roleId,
			List<String> permissionList);

	/**
	 * 通过查询条件找出所有的权限map
	 * 
	 * @param params 查询条件
	 * @return
	 */
	List<Map<String, Object>> findDepartmentRolePermissionListByParams(Map<String, Object> params);
	
	/**
	 * 查询Ztree使用的数据格式，已经选择的添加checked属性
	 * @param permissionIdStr 已经选择的权限id字符串，如"1,2"
	 * @return
	 */
	List<Map<String, Object>> findZtreeCheckedPermissionList(String permissionIdStr);
}
