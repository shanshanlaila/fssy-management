package com.fssy.shareholder.management.service.manage.log;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.manage.log.ScheduleAuditLog;

/**
 * 定时任务记录功能业务接口
 *
 * @author Solomon
 * @since 2022-12-16
 */
public interface ScheduleAuditLogService extends IService<ScheduleAuditLog>
{
	/**
	 * 通过主键列表删除定时任务记录
	 * 
	 * @param auditLogIdList 定时任务记录主键列表
	 * @return true/false
	 */
	boolean deleteScheduleAuditLogByIds(List<String> auditLogIdList);

	/**
	 * 通过查询条件查询定时任务记录列表
	 * 
	 * @param params 查询条件
	 * @return 定时任务记录列表数据
	 */
	List<ScheduleAuditLog> findScheduleAuditLogDataListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询定时任务记录列表
	 * 
	 * @param params 查询条件
	 * @return 定时任务记录分页数据
	 */
	Page<ScheduleAuditLog> findScheduleAuditLogDataListPerPageByParams(Map<String, Object> params);

	/**
	 * 通过查询条件查询定时任务记录列表（map）
	 * 
	 * @param params 查询条件
	 * @return 定时任务记录列表数据
	 */
	List<Map<String, Object>> findScheduleAuditLogMapDataListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询定时任务记录列表（map）
	 * 
	 * @param params 查询条件
	 * @return 定时任务记录分页数据
	 */
	Page<Map<String, Object>> findScheduleAuditLogMapDataListPerPageByParams(
			Map<String, Object> params);

	/**
	 * 不支持事务添加定时任务日志
	 * 
	 * @param scheduleAuditLog 定时任务日志实体
	 * @return
	 */
	Map<String, Object> saveScheduleAuditLogWithoutTransaction(ScheduleAuditLog scheduleAuditLog);
}
