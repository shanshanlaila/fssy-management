/**
 * 
 */
package com.fssy.shareholder.management.schedule.scheduler;

import com.fssy.shareholder.management.schedule.job.system.AttachmentScheduleJob;
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
	 * @param scheduler
	 * @throws SchedulerException
	 */
	private void attachmentScheduleJob(Scheduler scheduler) throws SchedulerException
	{
		JobDetail jobDetail = JobBuilder.newJob(AttachmentScheduleJob.class)
				.withIdentity("attachmentJob", "clean").build();
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 2 * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger1", "systemCleanTrigger")
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

	}
}
