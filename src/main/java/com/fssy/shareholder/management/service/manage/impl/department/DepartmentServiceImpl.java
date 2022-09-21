/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-12-24  	      添加departmentId,officeId,classId和factoryId字段的维护
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-12  	      添加departmentId,officeId,classId和factoryId字段的查询；下拉数据添加这几个字段信息
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-05-10 	      修改问题，如果涉及班组变更，需要把生效的生产关系和未完结的生产计划的班组同步修改
 */
package com.fssy.shareholder.management.service.manage.impl.department;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRolePermissionMapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRolePermission;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRoleUser;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.tools.common.UserCacheTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @Title: ManufacturePlanNoticeForMdServiceImpl.java
 * @Description: 组织结构功能业务实现类
 * @author Solomon
 * @date 2021年11月5日 上午9:37:42
 */
@Service
public class DepartmentServiceImpl implements DepartmentService
{
	/**
	 * 组织结构数据访问实现类
	 */
	@Autowired
	private DepartmentMapper departmentMapper;

	/**
	 * 组织结构功能设置角色的权限数据访问实现类
	 */
	@Autowired
	private DepartmentRolePermissionMapper departmentRolePermissionMapper;

	/**
	 * 组织结构功能设置角色的用户数据访问实现类
	 */
	@Autowired
	private DepartmentRoleUserMapper departmentRoleUserMapper;

	/**
	 * 用户缓存工具类
	 */
	@Autowired
	private UserCacheTool userCacheTool;

	@Override
	@Transactional
	@CacheEvict(value = "departments", key = "'allDepartment'")
	public Department saveDepartment(Department department)
	{
		// 业务判断
		// 1.编号不能重复
		QueryWrapper<Department> checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("code", department.getCode());
		if (departmentMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(String.format("组织结构编号:%s已存在，不能添加", department.getCode()));
		}
		// 添加组织结构
		departmentMapper.insert(department);

		// 修改相应字段
		UpdateWrapper<Department> departmentUpdateWrapper = new UpdateWrapper<>();
		// 2021-12-24 添加departmentId,officeId,classId和factoryId
		if (CommonConstant.DEPARTMENT_TYPE_DEPARTMENT.equals(department.getDepartmentType())) // 添加部门
		{
			departmentUpdateWrapper.eq("id", department.getId())
					.set("departmentId", department.getId())
					.set("departmentName", department.getName()).set("officeId", null)
					.set("officeName", null).set("classId", null).set("className", null)
					.set("factoryId", null).set("factoryName", null);
		}
		else if (CommonConstant.DEPARTMENT_TYPE_OFFICE.equals(department.getDepartmentType()))
		{ // 添加课室
			departmentUpdateWrapper.eq("id", department.getId()).set("officeId", department.getId())
					.set("officeName", department.getName()).set("classId", null)
					.set("className", null).set("factoryId", null).set("factoryName", null);
		}
		else if (CommonConstant.DEPARTMENT_TYPE_CLASS.equals(department.getDepartmentType()))
		{ // 添加班或课室细分
			departmentUpdateWrapper.eq("id", department.getId()).set("classId", department.getId())
					.set("className", department.getName());
		}
		departmentMapper.update(null, departmentUpdateWrapper);

		// 修改父组织leaf字段
		UpdateWrapper<Department> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", department.getParent()).set("leaf", 0).set("updatedAt",
				LocalDateTime.now());
		departmentMapper.update(null, updateWrapper);

		return department;
	}

	@Override
	@Transactional
	@CacheEvict(value = "departments", key = "'allDepartment'")
	public boolean deleteById(int id)
	{
		// 业务判断
		// 删除项存在子项时不允许删除
		QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", id);
		if (departmentMapper.selectCount(queryWrapper) > 0)
		{
			throw new ServiceException("该组织结构存在子项，需要先对子项进行操作");
		}

		// 删除项父项只有一个子项，需要修改父项的leaf属性为1
		Department deleteDepartment = departmentMapper.selectById(id);
		QueryWrapper<Department> allChildQueryWrapper = new QueryWrapper<>();
		allChildQueryWrapper.eq("parent", deleteDepartment.getParent());
		if (departmentMapper.selectCount(allChildQueryWrapper) == 1)
		{
			UpdateWrapper<Department> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", deleteDepartment.getParent()).set("leaf", 1);
			departmentMapper.update(null, updateWrapper);
		}

		// 删除组织结构，角色，权限对照表数据
		QueryWrapper<DepartmentRolePermission> deletePermissionRelationQueryWrapper = new QueryWrapper<>();
		deletePermissionRelationQueryWrapper.eq("departmentId", id);
		departmentRolePermissionMapper.delete(deletePermissionRelationQueryWrapper);

		// 删除缓缓结构，角色，用户对照表数据
		QueryWrapper<DepartmentRoleUser> deleteUserRelationQueryWrapper = new QueryWrapper<>();
		deleteUserRelationQueryWrapper.eq("departmentId", id);
		departmentRoleUserMapper.delete(deleteUserRelationQueryWrapper);

		int num = departmentMapper.deleteById(id);

		// 清除所有用户shiro框架使用的缓存
		userCacheTool.removeAllAuthorizationCache();

		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<Department> findDepartmentDataListByParams(Map<String, Object> params)
	{
		QueryWrapper<Department> queryWrapper = getQueryWrapper(params);
		List<Department> departmentList = departmentMapper.selectList(queryWrapper);
		return departmentList;
	}

	@Override
	@Transactional
	@CacheEvict(value = "departments", key = "'allDepartment'")
	public boolean updateDepartment(Department department)
	{
		// 业务判断
		// 删除项存在子项时不允许删除
		QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", department.getId());
		if (departmentMapper.selectCount(queryWrapper) > 0)
		{
			throw new ServiceException("该组织结构存在子项，需要先对子项进行操作");
		}

		// 1.编号不能重复
		QueryWrapper<Department> checkQueryWrapper = new QueryWrapper<>();
		checkQueryWrapper.eq("code", department.getCode()).ne("id", department.getId());
		if (departmentMapper.selectCount(checkQueryWrapper) > 0)
		{
			throw new ServiceException(String.format("组织结构编号:%s已存在，不能修改", department.getCode()));
		}

		// 判断是否修改原来的父节点权限的leaf
		// 条件为修改前后的父节点不同，并且原父节点的子项只有一个
		Department oriDepartment = departmentMapper.selectById(department.getId());
		QueryWrapper<Department> oriAllChildrenQueryWrapper = new QueryWrapper<>();
		oriAllChildrenQueryWrapper.eq("parent", oriDepartment.getParent());
		if (department.getParent() != oriDepartment.getParent()
				&& departmentMapper.selectCount(oriAllChildrenQueryWrapper) == 1)
		{
			UpdateWrapper<Department> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", oriDepartment.getParent()).set("leaf", 1).set("updatedAt",
					LocalDateTime.now());
			departmentMapper.update(null, updateWrapper);
		}

		// 修改新的父节点为权限的leaf为0（有子项）
		UpdateWrapper<Department> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", department.getParent()).set("leaf", 0).set("updatedAt",
				LocalDateTime.now());
		departmentMapper.update(null, updateWrapper);

		int num = departmentMapper.updateById(department);

		UpdateWrapper<Department> departmentUpdateWrapper = new UpdateWrapper<>();
		// 2021-12-24 添加departmentId,officeId,classId和factoryId
		if (CommonConstant.DEPARTMENT_TYPE_DEPARTMENT.equals(department.getDepartmentType())) // 添加部门
		{
			departmentUpdateWrapper.eq("id", department.getId())
					.set("departmentId", department.getId())
					.set("departmentName", department.getName()).set("officeId", null)
					.set("officeName", null).set("classId", null).set("className", null)
					.set("factoryId", null).set("factoryName", null);
		}
		else if (CommonConstant.DEPARTMENT_TYPE_OFFICE.equals(department.getDepartmentType()))
		{ // 添加课室
			departmentUpdateWrapper.eq("id", department.getId()).set("officeId", department.getId())
					.set("officeName", department.getName()).set("classId", null)
					.set("className", null).set("factoryId", null).set("factoryName", null);
		}
		else if (CommonConstant.DEPARTMENT_TYPE_CLASS.equals(department.getDepartmentType()))
		{ // 添加班或课室细分
			departmentUpdateWrapper.eq("id", department.getId()).set("classId", department.getId())
					.set("className", department.getName());
		}
		departmentMapper.update(null, departmentUpdateWrapper);

		if (num > 0)
		{
			return true;
		}
		return false;
	}

	private QueryWrapper<Department> getQueryWrapper(Map<String, Object> params)
	{
		QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
		// 添加查询条件
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		// 根据父id查询
		if (params.containsKey("parent"))
		{
			queryWrapper.eq("parent", params.get("parent"));
		}
		// 是否为叶子节点查询
		if (params.containsKey("leaf"))
		{
			queryWrapper.eq("leaf", params.get("leaf"));
		}
		// 2022-01-12 添加组织结构所在部门id等查询
		if (params.containsKey("departmentId"))
		{
			queryWrapper.eq("departmentId", params.get("departmentId"));
		}
		if (params.containsKey("officeId"))
		{
			queryWrapper.eq("officeId", params.get("officeId"));
		}
		// 2022-6-21 部门编号精确查询
		if (params.containsKey("codeEq"))
		{
			queryWrapper.eq("code", params.get("codeEq"));
		}
		if (params.containsKey("classId"))
		{
			queryWrapper.eq("classId", params.get("classId"));
		}
		if (params.containsKey("departmentType"))
		{
			queryWrapper.eq("departmentType", params.get("departmentType"));
		}
		if (params.containsKey("factoryId"))
		{
			queryWrapper.eq("factoryId", params.get("factoryId"));
		}
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		// 部门名称精确查询
		if (params.containsKey("nameEq"))
		{
			queryWrapper.eq("name", params.get("nameEq"));
		}
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		// 根据描述查询
		if (params.containsKey("desc"))
		{
			queryWrapper.like("desc", params.get("desc"));
		}
		// 根据组织结构所在部门名称模糊查询
		if (params.containsKey("departmentName"))
		{
			queryWrapper.like("departmentName", params.get("departmentName"));
		}
		// 根据组织结构所在课室名称模糊查询
		if (params.containsKey("officeName"))
		{
			queryWrapper.like("officeName", params.get("officeName"));
		}
		// 根据组织结构所在班组名称模糊查询
		if (params.containsKey("className"))
		{
			queryWrapper.like("className", params.get("className"));
		}
		// 根据组织结构所在工厂名称模糊查询
		if (params.containsKey("factoryName"))
		{
			queryWrapper.like("factoryName", params.get("factoryName"));
		}
		return queryWrapper;
	}

	@Override
	@Cacheable(value = "departments", key = "'alldepartment'")
	public List<Map<String, Object>> findAllDepartments()
	{
		return departmentMapper.selectMaps(null);
	}

	@Override
	public List<Map<String, Object>> findDepartmentsSelectedDataListByParams(
			Map<String, Object> params, List<String> selectedIds)
	{
		QueryWrapper<Department> queryWrapper = getQueryWrapper(params);
		List<Department> departmentList = departmentMapper.selectList(queryWrapper);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的用户角色添加selected属性

		for (Department department : departmentList)
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("name", department.getName());
			result.put("value", department.getId());
			result.put("id", department.getId());
			result.put("classId", department.getClassId());
			result.put("className", department.getClassName());
			result.put("officeId", department.getOfficeId());
			result.put("officeName", department.getOfficeName());
			result.put("factoryId", department.getFactoryId());
			result.put("factoryName", department.getFactoryName());
			result.put("departmentId", department.getDepartmentId());
			result.put("departmentName", department.getDepartmentName());
			boolean selected = false;
			for (int i = 0; i < selectedIds.size(); i++)
			{
				if (selectedIds.get(i).equals(department.getId().toString()))
				{
					selected = true;
					break;
				}
			}
			result.put("selected", selected);

			// 物料保障课放最前面，好选
			if ("物料保障课".equals(department.getName()))
			{
				resultList.add(0, result);

			}
			else
			{
				resultList.add(result);
			}

		}
		return resultList;
	}

	@Override
	@Cacheable(value = "departments", key = "#id")
	public Department findDepartmentById(Long id)
	{
		return departmentMapper.selectById(id);
	}
}
