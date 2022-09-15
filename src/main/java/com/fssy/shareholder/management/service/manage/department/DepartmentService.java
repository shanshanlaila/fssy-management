package com.fssy.shareholder.management.service.manage.department;

import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.pojo.manage.department.Department;

/**
 * 组织结构功能业务接口
 * 
 * @author Solomon
 */
public interface DepartmentService
{
	/**
	 * 添加组织结构数据
	 * 
	 * @param department 组织结构实体
	 * @return 组织结构实体
	 */
	Department saveDepartment(Department department);

	/**
	 * 通过主键删除组织结构
	 * 
	 * @param id 组织结构表主键
	 * @return true/false
	 */
	boolean deleteById(int id);

	/**
	 * 通过查询条件查询组织结构列表
	 * 
	 * @param params 查询条件
	 * @return 组织结构列表数据
	 */
	List<Department> findDepartmentDataListByParams(Map<String, Object> params);

	/**
	 * 修改组织结构数据
	 * 
	 * @param department 组织结构实体
	 * @return true/false
	 */
	boolean updateDepartment(Department department);
	
	/**
	 * 查询所有组织结构
	 * 
	 * @return 组织结构列表
	 */
	List<Map<String, Object>> findAllDepartments();

	/**
	 * 获取组织结构列表用于xm-select插件
	 * @param params
	 * @param selectedIds
	 * @return
	 */
	List<Map<String, Object>> findDepartmentsSelectedDataListByParams(Map<String, Object> params, List<String> selectedIds);

	/**
	 * 通过主键找部门信息
	 *
	 * @param id 部门表主键
	 * @return
	 */
	Department findDepartmentById(Long id);
}
