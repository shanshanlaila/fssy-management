package com.fssy.shareholder.management.service.manage.system;

import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.pojo.manage.system.System;

/**
 * 系统功能业务接口
 * 
 * @author Solomon
 */
public interface SystemService
{
	/**
	 * 添加系统功能数据
	 * 
	 * @param system 系统功能实体
	 * @return 添加系统功能实体
	 */
	public System saveSystem(System system);

	/**
	 * 通过主键删除系统功能数据
	 * 
	 * @param id 系统功能主键
	 * @return true/false
	 */
	public boolean deleteSystemById(int id);

	/**
	 * 通过查询条件查询系统功能数据列表
	 * 
	 * @param params 查询条件
	 * @return 系统功能列表
	 */
	public List<System> findSystemDataListByParams(Map<String, Object> params);

	/**
	 * 修改系统功能数据
	 * 
	 * @param system 修改后系统功能
	 * @return true/false
	 */
	public boolean updateSystem(System system);
}
