/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.performance.employee;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 事件清单主担岗位关系表 *****数据表名：
 * bs_performance_events_relation_main_role *****数据表作用： 事件清单对应的主担岗位 *****变更记录：
 * 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-10
 */
public interface EventsRelationRoleService
		extends IService<EventsRelationRole>
{

	/**
	 * 添加事件清单岗位关系
	 *
	 * @param eventsRelationRole 事件清单岗位关系实体
	 * @return 事件清单岗位关系实体
	 */
	EventsRelationRole savePerformanceEventsRelationRole(
			EventsRelationRole eventsRelationRole);

	/**
	 * 通过查询条件查询事件清单岗位关系列表
	 *
	 * @param params 查询条件
	 * @return 事件清单岗位关系列表
	 */
	List<EventsRelationRole> findPerformanceEventsRelationRoleDataListByParams(
			Map<String, Object> params);

	/**
	 * 通过查询条件分页查询事件清单岗位关系列表
	 *
	 * @param params 查询条件
	 * @return 事件清单岗位关系分页数据
	 */
	Page<EventsRelationRole> findPerformanceEventsRelationRoleDataListPerPageByParams(
			Map<String, Object> params);

	/**
	 * 通过查询条件查询事件清单岗位关系列表(map)
	 *
	 * @param params 查询条件
	 * @return 事件清单岗位关系列表
	 */
	List<Map<String, Object>> findRelationRoleMapDataByParams(
			Map<String, Object> params);

	/**
	 * 通过查询条件分页查询事件清单岗位关系列表(map)
	 *
	 * @param params 查询条件
	 * @return 事件清单岗位关系分页数据
	 */
	Page<Map<String, Object>> findPerformanceEventsRelationRoleDataMapListPerPageByParams(
			Map<String, Object> params);

	/**
	 * 通过查询条件查询用于xm-select控件的数据
	 *
	 * @param params      查询条件
	 * @param selectedIds 已选择项主键
	 * @return xm-select控件使用数据
	 */
	List<Map<String, Object>> findPerformanceEventsRelationRoleSelectedDataListByParams(
			Map<String, Object> params, List<String> selectedIds);

	/**
	 * 修改事件清单岗位关系数据
	 *
	 * @param eventsRelationRole 事件清单岗位关系实体
	 * @return map
	 */
	Map<String, Object> updatePerformanceEventsRelationRole(
			EventsRelationRole eventsRelationRole);
}
