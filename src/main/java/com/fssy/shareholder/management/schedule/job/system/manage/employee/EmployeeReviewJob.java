/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.schedule.job.system.manage.employee;

import com.fssy.shareholder.management.pojo.manage.log.ScheduleAuditLog;
import com.fssy.shareholder.management.service.common.EnterpriseWeChatService;
import com.fssy.shareholder.management.service.manage.log.ScheduleAuditLogService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MI
 * @ClassName: EmployeeReviewJob
 * @Description: 总结的定时任务
 * @date 2023/2/21 14:28
 */
@Component
public class EmployeeReviewJob implements Job {

    @Autowired
    private EnterpriseWeChatService enterpriseWeChatService;

    @Autowired
    private EntryCasReviewDetailService entryCasReviewDetailService;

    @Autowired
    private ScheduleAuditLogService scheduleAuditLogService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long, Map<String, Object>> userInfoMap = entryCasReviewDetailService.findWeChatNoticeMap();
        if (ObjectUtils.isEmpty(userInfoMap)) {
            return;
        }
        StringBuffer successStringBuilder = new StringBuffer();
        StringBuffer failStringBuilder = new StringBuffer();
        for (Map<String, Object> chiMap : userInfoMap.values()) {
            List<String> wetChatId = Collections.singletonList(chiMap.get("weChat").toString());
            List<String> userName = Collections.singletonList(chiMap.get("userName").toString());

            String content = String.format("%s，您好，您填写的计划有%s条准备到总结时间，请及时到系统进行履职总结填报。", userName, chiMap.get("num"));
            try {
                enterpriseWeChatService.push(content, wetChatId, null, null);
                StringTool.setMsg(successStringBuilder, userName.toString());
            } catch (Exception e) {
                StringTool.setMsg(failStringBuilder, userName.toString());
                e.printStackTrace();
            }
        }
        ScheduleAuditLog scheduleAuditLog = new ScheduleAuditLog();
        scheduleAuditLog.setName("回顾填报定时任务");
        scheduleAuditLog.setNote("时间：" + new Date() + "通知成功的人：" + successStringBuilder.toString());
        scheduleAuditLog.setStatus(CommonConstant.COMMON_STATUS_STRING_SUCCESS);
        scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
        ScheduleAuditLog scheduleAuditLogs = new ScheduleAuditLog();
        scheduleAuditLogs.setName("回顾填报定时任务");
        scheduleAuditLogs.setNote("时间：" + new Date() + "通知失败的人：" + failStringBuilder.toString());
        scheduleAuditLogs.setStatus(CommonConstant.COMMON_STATUS_STRING_ERROR);
        scheduleAuditLogService.saveScheduleAuditLogWithoutTransaction(scheduleAuditLog);
    }
}
