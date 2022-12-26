/**
 * 
 */
package com.fssy.shareholder.management.schedule.scheduler;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.fssy.shareholder.management.schedule.job.system.AttachmentScheduleJob;
import com.fssy.shareholder.management.schedule.job.system.manage.BalanceSheetInitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.BalanceSheetTransmitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.CashFlowInitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.CashFlowTransmitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.ProfitAnalysisInitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.ProfitAnalysisTransmitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.ProfitStatementInitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.ProfitStatementTransmitJob;

/**
 * 测试定时器
 * 
 * @author Solomon
 *
 */
@Component
public class CronSchedulerJob
{
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	/**
	 * 清理系统的文件
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void attachmentScheduleJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(AttachmentScheduleJob.class)
				.withIdentity("attachmentJob", "clean").build();
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 2 * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "systemCleanTrigger").withSchedule(cronScheduleBuilder)
				.build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 对账财务系统利润表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void profitStatementTransmitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(ProfitStatementTransmitJob.class)
				.withIdentity("profitStatementTransmitJob", "transmit1").build();
		// 每个月1号到15号凌晨2点15分调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
//				.cronSchedule("0/30 * * * * ?");
				.cronSchedule("0 15 2 1-15 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("profitStatementTransmitTrigger", "profitStatementTransmitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 对账财务系统资产负债表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void balanceSheetTransmitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(BalanceSheetTransmitJob.class)
				.withIdentity("balanceSheetTransmitJob", "transmit2").build();
		// 每个月1号到15号凌晨2点25分调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule("0 25 2 1-15 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("balanceSheetTransmitTrigger", "balanceSheetTransmitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 对账财务系统现金流量表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void cashFlowTransmitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(CashFlowTransmitJob.class)
				.withIdentity("cashFlowTransmitJob", "transmit3").build();
		// 每个月1号到15号凌晨2点35分调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
//				.cronSchedule("0/30 * * * * ?");
				.cronSchedule("0 35 2 1-15 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("cashFlowTransmitTrigger", "cashFlowTransmitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 对账财务系统变动分析表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void profitAnalysisTransmitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(ProfitAnalysisTransmitJob.class)
				.withIdentity("profitAnalysisTransmitJob", "transmit4").build();
		// 每个月1号到15号凌晨2点45分调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
//				.cronSchedule("0/30 * * * * ?");
				.cronSchedule("0 45 2 1-15 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("profitAnalysisTransmitTrigger", "profitAnalysisTransmitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 同时启动两个定时任务
	 * 
	 * @throws SchedulerException
	 */
	public void scheduleJobs() throws SchedulerException
	{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 对接财务系统利润表定时任务
//		profitStatementTransmitJob(scheduler);
		// 对接财务系统资产负债表定时任务
//		balanceSheetTransmitJob(scheduler);
		// 对接财务系统现金流量表定时任务
//		cashFlowTransmitJob(scheduler);
		// 对接财务系统变动分析表定时任务
//		profitAnalysisTransmitJob(scheduler);
		// 初始化财务系统利润表定时任务
//		profitStatementInitJob(scheduler);
		// 初始化财务系统资产负债表定时任务
//		balanceSheetInitJob(scheduler);
		// 初始化财务系统现金流量表定时任务
//		cashFlowInitJob(scheduler);
		// 初始化财务系统变动分析表定时任务
//		profitAnalysisInitJob(scheduler);
	}
	

	/**
	 * 初始化财务系统利润表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void profitStatementInitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(ProfitStatementInitJob.class)
				.withIdentity("profitStatementInitJob", "transmitInit1").build();
		// 每月26日10点5分
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule("0 5 10 26 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("profitStatementInitTrigger", "profitStatementInitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 初始化财务系统资产负债表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void balanceSheetInitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(BalanceSheetInitJob.class)
				.withIdentity("balanceSheetInitJob", "transmitInit2").build();
		// 每月26日10点30分
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule("0 30 10 26 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("balanceSheetInitTrigger", "balanceSheetInitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 初始化财务系统现金流量表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void cashFlowInitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(CashFlowInitJob.class)
				.withIdentity("cashFlowInitJob", "transmitInit3").build();
		// 每月26日11点00分
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule("0 0 11 26 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("cashFlowInitTrigger", "cashFlowInitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 初始化财务系统变动分析表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void profitAnalysisInitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(ProfitAnalysisInitJob.class)
				.withIdentity("profitAnalysisInitJob", "transmitInit4").build();
		// 每月26日11点45分
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule("0 45 11 26 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("profitAnalysisInitTrigger", "profitAnalysisInitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}
}
