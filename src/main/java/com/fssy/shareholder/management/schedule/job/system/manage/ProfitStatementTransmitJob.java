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

import com.fssy.shareholder.management.service.system.operate.analysis.ProfitStatementService;

/**
 * @Title: ProfitStatementTransmitJob.java
 * @Description: 利润表对接任务
 * @author Solomon
 * @date 2022年12月15日 下午5:42:55
 */
public class ProfitStatementTransmitJob implements Job
{
	/**
	 * 利润表数据业务实现类
	 */
	@Autowired
	private ProfitStatementService profitStatementService;

	@Override
	@Transactional
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
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
		LocalDateTime localDateTime = LocalDateTime.now();
		// 获取年、月、日
		int theMonthYear = localDateTime.getYear();
		int theMonthValue = localDateTime.getMonthValue();
		// endregion

		// region 构造条件并发送
		// 两个月前
		Map<String, Object> params = new HashMap<>();
		params.put("year", twoAgoYear);
		params.put("month", twoAgoMonth);
		profitStatementService.receiveData(params);

		// 上月
		params = new HashMap<>();
		params.put("year", lastMonthYear);
		params.put("month", lastMonthValue);
		profitStatementService.receiveData(params);

		// 当月
		params = new HashMap<>();
		params.put("year", theMonthYear);
		params.put("month", theMonthValue);
		profitStatementService.receiveData(params);
		// endregion
	}

}
