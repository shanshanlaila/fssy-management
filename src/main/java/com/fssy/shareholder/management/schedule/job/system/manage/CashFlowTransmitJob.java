/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.schedule.job.system.manage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.fssy.shareholder.management.pojo.manage.log.ScheduleAuditLog;
import com.fssy.shareholder.management.service.manage.log.ScheduleAuditLogService;
import com.fssy.shareholder.management.service.system.operate.analysis.CashFlowService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;

/**
 * @Title: CashFlowTransmitJob.java
 * @Description: 现金流量表对接任务
 * @author Solomon
 * @date 2022年12月20日 下午4:01:39
 */
public class CashFlowTransmitJob implements Job
{
	/**
	 * 现金流量功能业务实现类
	 */
	@Autowired
	private CashFlowService cashFlowService;

	/**
	 * 定时任务记录表功能业务实现类
	 */
	@Autowired
	private ScheduleAuditLogService scheduleAuditLogService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		// region 设置查询条件
		// 设置查前两个月、上月、当月
		// 调整到两个月前
		LocalDateTime twoMonthAgo = LocalDateTime.now().minusMonths(2);
		// 获取年、月、日
		int twoAgoYear = twoMonthAgo.getYear();
		int twoAgoMonth = twoMonthAgo.getMonthValue();

		// 上个月
		LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
		// 获取年、月、日
		int lastMonthYear = lastMonth.getYear();
		int lastMonthValue = lastMonth.getMonthValue();

		// 当月
//		LocalDateTime localDateTime = LocalDateTime.now();
		// 获取年、月、日
//		int theMonthYear = localDateTime.getYear();
//		int theMonthValue = localDateTime.getMonthValue();
		// endregion

		// region 构造条件并发送
		Map<String, Object> params;
		ScheduleAuditLog scheduleAuditLog;
		// 两个月前
		// 设置回滚点,只回滚以下异常
		Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", twoAgoYear);
			params.put("month", twoAgoMonth);
			Map<String, Object> result = cashFlowService.receiveData(params);
			scheduleAuditLog = new ScheduleAuditLog();
			scheduleAuditLog.setName("系统定时任务");
			scheduleAuditLog.setNote(InstandTool.objectToString(result.get("msg")));
			scheduleAuditLog
					.setStatus(Boolean.valueOf(InstandTool.objectToString(result.get("result")))
							? CommonConstant.COMMON_STATUS_STRING_SUCCESS
							: CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
		}
		catch (Exception e)
		{
			scheduleAuditLog = new ScheduleAuditLog();
			scheduleAuditLog.setName("系统定时任务");
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月对接系统错误，错误原因：【%s】", twoAgoYear,
					twoAgoMonth, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
		}

		// 上月
		// 设置回滚点,只回滚以下异常
		Object savePoint2 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", lastMonthYear);
			params.put("month", lastMonthValue);
			Map<String, Object> result = cashFlowService.receiveData(params);
			scheduleAuditLog = new ScheduleAuditLog();
			scheduleAuditLog.setName("系统定时任务");
			scheduleAuditLog.setNote(InstandTool.objectToString(result.get("msg")));
			scheduleAuditLog
					.setStatus(Boolean.valueOf(InstandTool.objectToString(result.get("result")))
							? CommonConstant.COMMON_STATUS_STRING_SUCCESS
							: CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
		}
		catch (Exception e)
		{
			scheduleAuditLog = new ScheduleAuditLog();
			scheduleAuditLog.setName("系统定时任务");
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月对接系统错误，错误原因：【%s】", lastMonthYear,
					lastMonthValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint2);
		}

		// 当月
		// 设置回滚点,只回滚以下异常
//		Object savePoint3 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//		try
//		{
//			params = new HashMap<>();
//			params.put("year", theMonthYear);
//			params.put("month", theMonthValue);
//			Map<String, Object> result = cashFlowService.receiveData(params);
//			scheduleAuditLog = new ScheduleAuditLog();
//			scheduleAuditLog.setName("系统定时任务");
//			scheduleAuditLog.setNote(InstandTool.objectToString(result.get("msg")));
//			scheduleAuditLog
//					.setStatus(Boolean.valueOf(InstandTool.objectToString(result.get("result")))
//							? CommonConstant.COMMON_STATUS_STRING_SUCCESS
//							: CommonConstant.COMMON_STATUS_STRING_ERROR);
//			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
//		}
//		catch (Exception e)
//		{
//			// 手工回滚异常,回滚到savePoint
//			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint3);
//			scheduleAuditLog = new ScheduleAuditLog();
//			scheduleAuditLog.setName("系统定时任务");
//			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月对接系统错误，错误原因：【%s】", theMonthYear,
//					theMonthValue, e.getMessage()));
//			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
//			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
//		}
		// endregion
	}

}
