/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.schedule.job.system.manage.employee;

import com.fssy.shareholder.management.service.common.EnterpriseWeChatService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author MI
 * @ClassName: EmployeeReviewJob
 * @Description: TODO
 * @date 2023/2/21 14:28
 */
@Component
public class EmployeeReviewJob implements Job {

    @Autowired
    private EnterpriseWeChatService enterpriseWeChatService;

    @Autowired
    private EntryCasReviewDetailService entryCasReviewDetailService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long, Map<String, Object>> userInfoMap = entryCasReviewDetailService.findWeChatNoticeMap();
        if (ObjectUtils.isEmpty(userInfoMap)) {
            return;
        }
        for (Map<String, Object> chiMap : userInfoMap.values()) {
            List<String> wetChatId = Collections.singletonList(chiMap.get("weChat").toString());
            List<String> userName = Collections.singletonList(chiMap.get("userName").toString());
            String content = String.format("%s，你好，你填写的计划有%s条准备到总结时间，请及时到系统进行履职总结填报。", userName, chiMap.get("num"));
            try {
                enterpriseWeChatService.push(content, wetChatId, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
