/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.config;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 导入场景表 *****数据表名： bs_common_import_module
 * *****数据表作用： 各导入模块（场景）记录 *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
public interface ImportModuleService extends IService<ImportModule>
{
	/**
	 * 通过查询条件查询导入场景列表
	 * 
	 * @param params 查询条件
	 * @return 导入场景列表
	 */
	List<ImportModule> findImportModuleDataListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询导入场景列表
	 * 
	 * @param params 查询条件
	 * @return 导入场景分页数据
	 */
	Page<ImportModule> findImportModuleDataListPerPageByParams(Map<String, Object> params);

	/**
	 * 通过查询条件查询导入场景列表(map)
	 * 
	 * @param params 查询条件
	 * @return 导入场景列表
	 */
	List<Map<String, Object>> findImportModuleDataMapListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询导入场景列表(map)
	 * 
	 * @param params 查询条件
	 * @return 导入场景分页数据
	 */
	Page<Map<String, Object>> findImportModuleDataMapListPerPageByParams(
			Map<String, Object> params);

	/**
	 * 通过查询条件查询用于xm-select控件的数据
	 * 
	 * @param params      查询条件
	 * @param selectedIds 已选择项主键
	 * @return xm-select控件使用数据
	 */
	List<Map<String, Object>> findImportModuleSelectedDataListByParams(Map<String, Object> params,
			List<String> selectedIds);

	/**
	 * 根据主键查询导入场景对象
	 * 
	 * @param id 主键
	 * @return
	 */
	ImportModule findById(Long id);
}
