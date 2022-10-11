/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.config.ImportModuleMapper;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.tools.common.InstandTool;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 导入场景表 *****数据表名： bs_common_import_module
 * *****数据表作用： 各导入模块（场景）记录 *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@Service
public class ImportModuleServiceImpl extends ServiceImpl<ImportModuleMapper, ImportModule>
		implements ImportModuleService
{
	/**
	 * 导入场景数据访问实现类
	 */
	@Autowired
	private ImportModuleMapper importModuleMapper;

	@Override
	public List<ImportModule> findImportModuleDataListByParams(Map<String, Object> params)
	{
		QueryWrapper<ImportModule> queryWrapper = getQueryWrapper(params);
		List<ImportModule> vehicleList = importModuleMapper
				.selectList(queryWrapper);
		return vehicleList;
	}

	@Override
	public Page<ImportModule> findImportModuleDataListPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<ImportModule> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<ImportModule> myPage = new Page<>(page, limit);
		Page<ImportModule> vehiclePage = importModuleMapper
				.selectPage(myPage, queryWrapper);
		return vehiclePage;
	}

	@Override
	public List<Map<String, Object>> findImportModuleDataMapListByParams(Map<String, Object> params)
	{
		QueryWrapper<ImportModule> queryWrapper = getQueryWrapper(params);
		List<Map<String, Object>> vehicleList = importModuleMapper
				.selectMaps(queryWrapper);
		return vehicleList;
	}

	@Override
	public Page<Map<String, Object>> findImportModuleDataMapListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<ImportModule> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Map<String, Object>> myPage = new Page<>(page, limit);
		Page<Map<String, Object>> vehiclePage = importModuleMapper
				.selectMapsPage(myPage, queryWrapper);
		return vehiclePage;
	}

	@Override
	public List<Map<String, Object>> findImportModuleSelectedDataListByParams(
			Map<String, Object> params, List<String> selectedIds)
	{
		QueryWrapper<ImportModule> queryWrapper = getQueryWrapper(params);
		List<ImportModule> vehicleList = importModuleMapper
				.selectList(queryWrapper);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的数据添加selected属性
		Map<String, Object> result;
		for (ImportModule importModule : vehicleList)
		{
			result = new HashMap<String, Object>();
			result.put("name", importModule.getName());
			result.put("value", importModule.getId());
			result.put("id", importModule.getId());
			boolean selected = false;
			for (int i = 0; i < selectedIds.size(); i++)
			{
				if (selectedIds.get(i)
						.equals(InstandTool.objectToString(importModule.getId())))
				{
					selected = true;
					break;
				}
			}
			result.put("selected", selected);
			resultList.add(result);
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<ImportModule> getQueryWrapper(Map<String, Object> params)
	{
		QueryWrapper<ImportModule> queryWrapper = new QueryWrapper<>();
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		if (params.containsKey("ids"))
		{
			queryWrapper.in("id", (List<String>) params.get("ids"));
		}
		if (params.containsKey("nameEq"))
		{
			queryWrapper.eq("name", params.get("nameEq"));
		}
		if (params.containsKey("noteEq"))
		{
			queryWrapper.eq("note", params.get("noteEq"));
		}
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		if (params.containsKey("note"))
		{
			queryWrapper.like("note", params.get("note"));
		}
		if (params.containsKey("idDesc"))
		{
			queryWrapper.orderByDesc("id");
		}
		return queryWrapper;
	}

	@Override
	public ImportModule findById(Long id)
	{
		return importModuleMapper.selectById(id);
	}
}
