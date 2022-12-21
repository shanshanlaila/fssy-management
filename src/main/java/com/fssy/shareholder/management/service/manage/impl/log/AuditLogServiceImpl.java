/**
 * 
 */
package com.fssy.shareholder.management.service.manage.impl.log;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.log.AuditLogMapper;
import com.fssy.shareholder.management.pojo.manage.log.AuditLog;
import com.fssy.shareholder.management.service.manage.log.AuditLogService;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * 审计日志业务实现类
 * 
 * @author Solomon
 */
@Service
public class AuditLogServiceImpl implements AuditLogService
{
	@Autowired
	private AuditLogMapper auditLogMapper;

	@Override
	@Transactional
	public boolean deleteAuditLogByIds(List<String> ids)
	{
		if (ObjectUtils.isEmpty(ids))
		{
			throw new ServiceException("请选择需要删除的记录");
		}
		int num = auditLogMapper.deleteBatchIds(ids);
		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<AuditLog> findAuditLogDataListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<AuditLog> queryWrapper = getQueryWrapper(params);
		List<AuditLog> logs = auditLogMapper.selectList(queryWrapper);
		return logs;
	}

	@Override
	public Page<AuditLog> findAuditLogDataListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<AuditLog> queryWrapper = getQueryWrapper(params);
		queryWrapper.orderByDesc("id");
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<AuditLog> myPage = new Page<>(page, limit);
		Page<AuditLog> auditLogPage = auditLogMapper.selectPage(myPage, queryWrapper);
		return auditLogPage;
	}

	private QueryWrapper<AuditLog> getQueryWrapper(Map<String, Object> params)
	{
		// region 构建queryWrapper
		QueryWrapper<AuditLog> queryWrapper = new QueryWrapper<>();
		// 审计日志表主键查询
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		// 操作状态查询
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
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		// 操作功能模糊查询
		if (!ObjectUtils.isEmpty(params.get("operation")))
		{
			queryWrapper.like("operation", params.get("operation"));
		}
		// endregion
		return queryWrapper;
	}
}
