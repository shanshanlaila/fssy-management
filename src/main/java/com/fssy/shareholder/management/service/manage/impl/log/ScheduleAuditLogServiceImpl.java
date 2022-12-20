/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.manage.impl.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.log.ScheduleAuditLogMapper;
import com.fssy.shareholder.management.pojo.manage.log.ScheduleAuditLog;
import com.fssy.shareholder.management.service.manage.log.ScheduleAuditLogService;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @Title: ScheduleAuditLogServiceImpl.java
 * @Description: 定时任务记录功能业务实现类
 * @author Solomon
 * @date 2022年12月16日 上午10:44:19
 */
@Service
public class ScheduleAuditLogServiceImpl extends
		ServiceImpl<ScheduleAuditLogMapper, ScheduleAuditLog> implements ScheduleAuditLogService
{
	/**
	 * 定时任务记录表数据访问实现类
	 */
	@Autowired
	private ScheduleAuditLogMapper scheduleAuditLogMapper;

	@Override
	public boolean deleteScheduleAuditLogByIds(List<String> auditLogIdList)
	{
		if (ObjectUtils.isEmpty(auditLogIdList))
		{
			throw new ServiceException("请选择需要删除的记录");
		}
		scheduleAuditLogMapper.deleteBatchIds(auditLogIdList);
		return true;
	}

	@Override
	public List<ScheduleAuditLog> findScheduleAuditLogDataListByParams(Map<String, Object> params)
	{
		QueryWrapper<ScheduleAuditLog> queryWrapper = getQueryWrapper(params);
		List<ScheduleAuditLog> logs = scheduleAuditLogMapper.selectList(queryWrapper);
		return logs;
	}

	@Override
	public Page<ScheduleAuditLog> findScheduleAuditLogDataListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<ScheduleAuditLog> queryWrapper = getQueryWrapper(params);
		queryWrapper.orderByDesc("id");
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<ScheduleAuditLog> myPage = new Page<>(page, limit);
		Page<ScheduleAuditLog> scheduleAuditLogPage = scheduleAuditLogMapper.selectPage(myPage,
				queryWrapper);
		return scheduleAuditLogPage;
	}

	@Override
	public List<Map<String, Object>> findScheduleAuditLogMapDataListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<ScheduleAuditLog> queryWrapper = getQueryWrapper(params);
		List<Map<String, Object>> logs = scheduleAuditLogMapper.selectMaps(queryWrapper);
		return logs;
	}

	@Override
	public Page<Map<String, Object>> findScheduleAuditLogMapDataListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<ScheduleAuditLog> queryWrapper = getQueryWrapper(params);
		queryWrapper.orderByDesc("id");
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Map<String, Object>> myPage = new Page<>(page, limit);
		Page<Map<String, Object>> scheduleAuditLogPage = scheduleAuditLogMapper
				.selectMapsPage(myPage, queryWrapper);
		return scheduleAuditLogPage;
	}

	@SuppressWarnings("unchecked")
	private QueryWrapper<ScheduleAuditLog> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建queryWrapper
		QueryWrapper<ScheduleAuditLog> queryWrapper = new QueryWrapper<>();
		// 定时任务记录表主键查询
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		if (params.containsKey("ids"))
		{
			queryWrapper.in("id", (List<String>) params.get("ids"));
		}
		if (params.containsKey("status"))
		{
			queryWrapper.eq("status", params.get("status"));
		}
		// 操作时间查询
		if (params.containsKey("createdAtStart"))
		{
			queryWrapper.ge("createdAt", params.get("createdAtStart"));
		}
		if (params.containsKey("createdAtEnd"))
		{
			queryWrapper.le("createdAt", params.get("createdAtEnd"));
		}
		// 操作人模糊查询
		if (params.containsKey("createdName"))
		{
			queryWrapper.like("createdName", params.get("createdName"));
		}
		if (params.containsKey("note"))
		{
			queryWrapper.like("note", params.get("note"));
		}
		// endregion
		return queryWrapper;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Map<String, Object> saveScheduleAuditLogWithoutTransaction(
			ScheduleAuditLog scheduleAuditLog)
	{
		Map<String, Object> result = new HashMap<>();
		scheduleAuditLogMapper.insert(scheduleAuditLog);
		return result;
	}
}
