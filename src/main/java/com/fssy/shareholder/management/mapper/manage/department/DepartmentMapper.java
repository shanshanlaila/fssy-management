package com.fssy.shareholder.management.mapper.manage.department;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;

/**
 * 组织结构数据访问接口
 * 
 * @author Solomon
 */
public interface DepartmentMapper extends BaseMapper<Department>
{
	List<Map<String, Object>> getSomethings();
}
