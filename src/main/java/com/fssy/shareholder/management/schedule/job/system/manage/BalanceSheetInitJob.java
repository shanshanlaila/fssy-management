/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.schedule.job.system.manage;

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
import com.fssy.shareholder.management.service.system.operate.analysis.BalanceSheetService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;

/**
 * @Title: BalanceSheetInitJob.java
 * @Description: 资产负债表初始化定时任务
 * @author Solomon
 * @date 2022年12月26日 上午8:50:54
 */
public class BalanceSheetInitJob implements Job
{
	/**
	 * 资产负债数据访问实现类
	 */
	@Autowired
	private BalanceSheetService balanceSheetService;

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
		// 设置2022年1月到9月
		int year2022 = 2022;
		int januaryValue = 1;
		int februaryValue = 2;
		int marchValue = 3;
		int aprilValue = 4;
		int mayValue = 5;
		int juneValue = 6;
		int julyValue = 7;
		int augustValue = 8;
		int septemberValue = 9;
		int octoborValue = 10;
		int novemberValue = 11;
		int decemberValue = 12;
		// endregion

		// region 构造条件并发送
		Map<String, Object> params;
		ScheduleAuditLog scheduleAuditLog;
		// 1月
		// 设置回滚点,只回滚以下异常
		Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", januaryValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					januaryValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
		}

		// 2月
		// 设置回滚点,只回滚以下异常
		Object savePoint2 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", februaryValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					februaryValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint2
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint2);
		}

		// 3月
		// 设置回滚点,只回滚以下异常
		Object savePoint3 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", marchValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					marchValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint3
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint3);
		}
		
		// 4月
		// 设置回滚点,只回滚以下异常
		Object savePoint4 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", aprilValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					aprilValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint4
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint4);
		}

		// 5月
		// 设置回滚点,只回滚以下异常
		Object savePoint5 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", mayValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					mayValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint5
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint5);
		}

		// 6月
		// 设置回滚点,只回滚以下异常
		Object savePoint6 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", juneValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					juneValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint6
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint6);
		}
		
		// 7月
		// 设置回滚点,只回滚以下异常
		Object savePoint7 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", julyValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					julyValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint7
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint7);
		}

		// 8月
		// 设置回滚点,只回滚以下异常
		Object savePoint8 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", augustValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					augustValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint8
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint8);
		}

		// 9月
		// 设置回滚点,只回滚以下异常
		Object savePoint9 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		try
		{
			params = new HashMap<>();
			params.put("year", year2022);
			params.put("month", septemberValue);
			Map<String, Object> result = balanceSheetService.receiveData(params);
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
			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
					septemberValue, e.getMessage()));
			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
			// 手工回滚异常,回滚到savePoint9
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint9);
		}

		// 10月
		// 设置回滚点,只回滚以下异常
//		Object savePoint10 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//		try
//		{
//			params = new HashMap<>();
//			params.put("year", year2022);
//			params.put("month", octoborValue);
//			Map<String, Object> result = balanceSheetService.receiveData(params);
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
//			scheduleAuditLog = new ScheduleAuditLog();
//			scheduleAuditLog.setName("系统定时任务");
//			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
//					octoborValue, e.getMessage()));
//			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
//			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
//			// 手工回滚异常,回滚到savePoint10
//			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint10);
//		}

		// 11月
		// 设置回滚点,只回滚以下异常
//		Object savePoint11 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//		try
//		{
//			params = new HashMap<>();
//			params.put("year", year2022);
//			params.put("month", novemberValue);
//			Map<String, Object> result = balanceSheetService.receiveData(params);
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
//			scheduleAuditLog = new ScheduleAuditLog();
//			scheduleAuditLog.setName("系统定时任务");
//			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
//					novemberValue, e.getMessage()));
//			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
//			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
//			// 手工回滚异常,回滚到savePoint11
//			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint11);
//		}

		// 12月
		// 设置回滚点,只回滚以下异常
//		Object savePoint12 = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
//		try
//		{
//			params = new HashMap<>();
//			params.put("year", year2022);
//			params.put("month", decemberValue);
//			Map<String, Object> result = balanceSheetService.receiveData(params);
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
//			scheduleAuditLog = new ScheduleAuditLog();
//			scheduleAuditLog.setName("系统定时任务");
//			scheduleAuditLog.setNote(String.format("【%s】年，【%s】月【资产负债表】对接系统错误，错误原因：【%s】", year2022,
//					decemberValue, e.getMessage()));
//			scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
//			scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
//			// 手工回滚异常,回滚到savePoint12
//			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint12);
//		}
		// endregion
	}

}
