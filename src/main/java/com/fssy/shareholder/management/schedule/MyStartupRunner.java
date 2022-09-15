/**
 * 
 */
package com.fssy.shareholder.management.schedule;

import com.fssy.shareholder.management.schedule.scheduler.CronSchedulerJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动时自动启动定时任务
 * 
 * @author Solomon
 */
@Component
public class MyStartupRunner implements CommandLineRunner
{
	@Autowired
    public CronSchedulerJob scheduleJobs;

	@Override
	public void run(String... args) throws Exception
	{
		scheduleJobs.scheduleJobs();
        System.out.println(">>>>>>>>>>>>>>>定时任务开始执行<<<<<<<<<<<<<");
	}

}
