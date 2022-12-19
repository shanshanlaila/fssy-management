/**
 * 
 */
package com.fssy.shareholder.management.schedule.scheduler;

import com.fssy.shareholder.management.schedule.job.system.AttachmentScheduleJob;
import com.fssy.shareholder.management.schedule.job.system.manage.BalanceSheetTransmitJob;
import com.fssy.shareholder.management.schedule.job.system.manage.ProfitStatementTransmitJob;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

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
//				.cronSchedule("0/6 * * * * ?");
				.cronSchedule("0 15 2 1-15 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("profitStatementTransmitTrigger", "profitStatementTransmitTrigger")
				.withSchedule(cronScheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 对账财务系统利润表定时任务
	 * 
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void balanceSheetTransmitJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(BalanceSheetTransmitJob.class)
				.withIdentity("balanceSheetTransmitJob", "transmit2").build();
		// 每个月1号到15号凌晨2点45分调度
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
				.cronSchedule("0 45 2 1-15 * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("balanceSheetTransmitTrigger", "balanceSheetTransmitTrigger")
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
	}
}
