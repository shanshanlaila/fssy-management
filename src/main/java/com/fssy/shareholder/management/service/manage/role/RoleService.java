package com.fssy.shareholder.management.service.manage.role;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.manage.role.Role;

/**
 * 角色功能业务接口
 * 
 * @author Solomon
 */
public interface RoleService
{
	/**
	 * 添加角色数据
	 * 
	 * @param role 角色实体
	 * @return 角色实体
	 */
	Role saveRole(Role role);

	/**
	 * 根据id删除角色
	 * 
	 * @param id 角色表主键
	 * @return true/false
	 */
	boolean deleteRoleById(int id);

	/**
	 * 通过查询条件查询角色列表
	 * 
	 * @param params 查询条件
	 * @return 角色列表
	 */
	List<Role> findRoleDataListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询角色列表
	 * 
	 * @param params 查询条件
	 * @return 角色分页数据
	 */
	Page<Role> findRoleDataListPerPageByParams(Map<String, Object> params);

	/**
	 * 通过查询条件查询用于xm-select控件的数据
	 * 
	 * @param params      查询条件
	 * @param selectedIds 已选择项主键列表
	 * @return xm-select控件使用数据
	 */
	List<Map<String, Object>> findRoleSelectedDataListByParams(Map<String, Object> params,
			List<Long> selectedIds);

	/**
	 * 修改角色数据
	 * 
	 * @param role 角色实体
	 * @return true/false
	 */
	boolean updateRole(Role role);
}
