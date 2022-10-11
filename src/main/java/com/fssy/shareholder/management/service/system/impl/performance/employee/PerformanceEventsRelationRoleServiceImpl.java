/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.employee.PerformanceEventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.system.performance.employee.PerformanceEventsRelationRole;
import com.fssy.shareholder.management.service.system.performance.employee.PerformanceEventsRelationRoleService;
import com.fssy.shareholder.management.tools.common.InstandTool;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 事件清单主担岗位关系表 *****数据表名：
 * bs_performance_events_relation_main_role *****数据表作用： 事件清单对应的主担岗位 *****变更记录：
 * 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-10
 */
@Service
public class PerformanceEventsRelationRoleServiceImpl
		extends ServiceImpl<PerformanceEventsRelationRoleMapper, PerformanceEventsRelationRole>
		implements PerformanceEventsRelationRoleService
{
	/**
	 * 事件清单岗位关系数据访问实现类
	 */
	@Autowired
	private PerformanceEventsRelationRoleMapper performanceEventsRelationRoleMapper;

	@Override
	@Transactional
	public PerformanceEventsRelationRole savePerformanceEventsRelationRole(
			PerformanceEventsRelationRole performanceEventsRelationRole)
	{
		performanceEventsRelationRoleMapper.insert(performanceEventsRelationRole);
		return performanceEventsRelationRole;
	}

	@Override
	public List<PerformanceEventsRelationRole> findPerformanceEventsRelationRoleDataListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<PerformanceEventsRelationRole> queryWrapper = getQueryWrapper(params);
		List<PerformanceEventsRelationRole> vehicleList = performanceEventsRelationRoleMapper
				.selectList(queryWrapper);
		return vehicleList;
	}

	@Override
	public Page<PerformanceEventsRelationRole> findPerformanceEventsRelationRoleDataListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<PerformanceEventsRelationRole> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<PerformanceEventsRelationRole> myPage = new Page<>(page, limit);
		Page<PerformanceEventsRelationRole> vehiclePage = performanceEventsRelationRoleMapper
				.selectPage(myPage, queryWrapper);
		return vehiclePage;
	}

	@Override
	public List<Map<String, Object>> findPerformanceEventsRelationRoleDataMapListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<PerformanceEventsRelationRole> queryWrapper = getQueryWrapper(params);
		List<Map<String, Object>> vehicleList = performanceEventsRelationRoleMapper
				.selectMaps(queryWrapper);
		return vehicleList;
	}

	@Override
	public Page<Map<String, Object>> findPerformanceEventsRelationRoleDataMapListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<PerformanceEventsRelationRole> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Map<String, Object>> myPage = new Page<>(page, limit);
		Page<Map<String, Object>> vehiclePage = performanceEventsRelationRoleMapper
				.selectMapsPage(myPage, queryWrapper);
		return vehiclePage;
	}

	@Override
	public List<Map<String, Object>> findPerformanceEventsRelationRoleSelectedDataListByParams(
			Map<String, Object> params, List<String> selectedIds)
	{
		QueryWrapper<PerformanceEventsRelationRole> queryWrapper = getQueryWrapper(params);
		List<PerformanceEventsRelationRole> vehicleList = performanceEventsRelationRoleMapper
				.selectList(queryWrapper);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的数据添加selected属性
		Map<String, Object> result;
		for (PerformanceEventsRelationRole performanceEventsRelationRole : vehicleList)
		{
			result = new HashMap<String, Object>();
			result.put("name", performanceEventsRelationRole.getUserName());
			result.put("value", performanceEventsRelationRole.getId());
			result.put("id", performanceEventsRelationRole.getId());
			boolean selected = false;
			for (int i = 0; i < selectedIds.size(); i++)
			{
				if (selectedIds.get(i)
						.equals(InstandTool.objectToString(performanceEventsRelationRole.getId())))
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

	@Override
	@Transactional
	public Map<String, Object> updatePerformanceEventsRelationRole(
			PerformanceEventsRelationRole performanceEventsRelationRole)
	{
		performanceEventsRelationRoleMapper.updateById(performanceEventsRelationRole);
		return null;
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<PerformanceEventsRelationRole> getQueryWrapper(Map<String, Object> params)
	{
		QueryWrapper<PerformanceEventsRelationRole> queryWrapper = new QueryWrapper<>();
		// 事件清单岗位关系表主键查询
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		// 事件清单岗位关系表主键列表查询
		if (params.containsKey("ids"))
		{
			queryWrapper.in("id", (List<String>) params.get("ids"));
		}
		// 事件表主键主键查询
		if (params.containsKey("eventsId"))
		{
			queryWrapper.eq("eventsId", params.get("eventsId"));
		}
		// 事件表主键主键列表查询
		if (params.containsKey("eventsIds"))
		{
			queryWrapper.in("eventsId", (List<String>) params.get("eventsIds"));
		}
		// 岗位名称精确查询
		if (params.containsKey("roleNameEq"))
		{
			queryWrapper.eq("roleName", params.get("roleNameEq"));
		}
		// 岗位主键精确查询
		if (params.containsKey("roleId"))
		{
			queryWrapper.eq("roleId", params.get("roleId"));
		}
		if (params.containsKey("year"))
		{
			queryWrapper.eq("year", params.get("year"));
		}
		if (params.containsKey("month"))
		{
			queryWrapper.eq("month", params.get("month"));
		}
		// 生效日期查询
		if (params.containsKey("activeDateStart"))
		{
			queryWrapper.ge("activeDate", params.get("activeDateStart"));
		}
		if (params.containsKey("activeDateEnd"))
		{
			queryWrapper.le("activeDate", params.get("activeDateEnd"));
		}
		// 编制日期查询
		if (params.containsKey("createDateStart"))
		{
			queryWrapper.ge("createDate", params.get("createDateStart"));
		}
		if (params.containsKey("createDateEnd"))
		{
			queryWrapper.le("createDate", params.get("createDateEnd"));
		}
		// 部门名称精确查询
		if (params.containsKey("departmentNameEq"))
		{
			queryWrapper.eq("departmentName", params.get("departmentNameEq"));
		}
		// 部门主键精确查询
		if (params.containsKey("departmentId"))
		{
			queryWrapper.eq("departmentId", params.get("departmentId"));
		}
		// 事件类别精确查询
		if (params.containsKey("eventsTypeEq"))
		{
			queryWrapper.eq("eventsType", params.get("eventsTypeEq"));
		}
		if (params.containsKey("isMainOrNext"))
		{
			queryWrapper.eq("isMainOrNext", params.get("isMainOrNext"));
		}
		// 用户名称精确查询
		if (params.containsKey("userNameEq"))
		{
			queryWrapper.eq("userName", params.get("userNameEq"));
		}
		// 用户主键精确查询
		if (params.containsKey("userId"))
		{
			queryWrapper.eq("userId", params.get("userId"));
		}
		// 岗位名称查询
		if (params.containsKey("roleName"))
		{
			queryWrapper.like("roleName", params.get("roleName"));
		}
		// 部门名称查询
		if (params.containsKey("departmentName"))
		{
			queryWrapper.like("departmentName", params.get("departmentName"));
		}
		// 事件类别查询
		if (params.containsKey("eventsType"))
		{
			queryWrapper.like("eventsType", params.get("eventsType"));
		}
		// 用户名称查询
		if (params.containsKey("userName"))
		{
			queryWrapper.like("userName", params.get("userName"));
		}
		if (params.containsKey("idDesc"))
		{
			queryWrapper.orderByDesc("id");
		}
		if (params.containsKey("groupBy"))
		{
			queryWrapper.groupBy(InstandTool.objectToString(params.get("groupBy")));
		}
		if (params.containsKey("select"))
		{
			queryWrapper.select(InstandTool.objectToString(params.get("select")));
		}
		return queryWrapper;
	}
}
