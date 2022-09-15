/**
 * 
 */
package com.fssy.shareholder.management.service.manage.log;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.manage.log.AuditLog;

/**
 * 审计日志业务接口
 * 
 * @author Solomon
 */
public interface AuditLogService
{
	/**
	 * 通过主键列表删除审计日志
	 * 
	 * @param id 审计日志主键
	 * @return true/false
	 */
	boolean deleteAuditLogByIds(List<String> auditLogList);

	/**
	 * 通过查询条件查询审计日志列表
	 * 
	 * @param params 查询条件
	 * @return 审计日志列表数据
	 */
	List<AuditLog> findAuditLogDataListByParams(Map<String, Object> params);
	
	/**
	 * 通过查询条件分页查询审计日志列表
	 * 
	 * @param params 查询条件
	 * @return 审计日志分页数据
	 */
	Page<AuditLog> findAuditLogDataListPerPageByParams(Map<String, Object> params);
}
