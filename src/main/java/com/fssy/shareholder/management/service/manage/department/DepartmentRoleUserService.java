package com.fssy.shareholder.management.service.manage.department;

import java.util.List;
import java.util.Map;

/**
 * 组织结构功能设置角色的用户业务接口
 * 
 * @author Solomon
 */
public interface DepartmentRoleUserService
{
	/**
	 * 通过组织机构设置一个角色的用户
	 * 
	 * @param departmentId 组织结构表主键
	 * @param roleId       角色表主键
	 * @param userList     用户表主键列表
	 * @return
	 */
	boolean changeRoleUserFromDepartment(Long departmentId, Long roleId,
			List<String> userList);

	/**
	 * 通过组织结构和角色找到出所有的用户map
	 * 
	 * @param departmentId 组织结构表主键
	 * @param roleId       角色表主键
	 * @return
	 */
	List<Map<String, Object>> findSelectedUserList(Long departmentId,
			Long roleId);
}
