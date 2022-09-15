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

import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Title: ManufacturePlanNoticeForMdServiceImpl.java
 * @Description: 组织结构功能业务实现类
 * @author Solomon
 * @date 2021年11月5日 上午9:37:42
 */
@Service
public class DepartmentServiceImpl implements DepartmentService
{
	@Override
	public Department saveDepartment(Department department) {
		return null;
	}

	@Override
	public boolean deleteById(int id) {
		return false;
	}

	@Override
	public List<Department> findDepartmentDataListByParams(Map<String, Object> params) {
		return null;
	}

	@Override
	public boolean updateDepartment(Department department) {
		return false;
	}

	@Override
	public List<Map<String, Object>> findAllDepartments() {
		return null;
	}

	@Override
	public List<Map<String, Object>> findDepartmentsSelectedDataListByParams(Map<String, Object> params, List<String> selectedIds) {
		return null;
	}

	@Override
	public Department findDepartmentById(Long id) {
		return null;
	}
}
