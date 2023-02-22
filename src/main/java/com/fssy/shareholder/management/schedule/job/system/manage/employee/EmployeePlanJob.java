/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.schedule.job.system.manage.employee;

import com.fssy.shareholder.management.service.common.EnterpriseWeChatService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author MI
 * @ClassName: EmployeePlanJob
 * @Description: TODO
 * @date 2023/2/20 14:32
 */
@Component
public class EmployeePlanJob implements Job {

    @Autowired
    private EnterpriseWeChatService enterpriseWeChatService;

    @Autowired
    private EntryCasPlanDetailService entryCasPlanDetailService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long, Map<String, Object>> userInfoMap = entryCasPlanDetailService.findWeChatNoticeMap();
        if (ObjectUtils.isEmpty(userInfoMap)) {
            return;
        }
        for (Map<String, Object> chiMap : userInfoMap.values()) {
            List<String> wetChatId = Collections.singletonList(chiMap.get("weChat").toString());
            List<String> userName = Collections.singletonList(chiMap.get("userName").toString());
            String content = String.format("%s，您好，准备进入%s年%s月，您还剩%s条履职计划未填报，请及时填报%s年%s月的履职计划。",
                    userName,
                    LocalDateTime.now().getMonthValue() == 12 ? LocalDateTime.now().getYear() + 1 : LocalDateTime.now().getYear(),
                    LocalDateTime.now().getMonthValue() + 1,
                    chiMap.get("num"),
                    LocalDateTime.now().getYear(),
                    LocalDateTime.now().getMonthValue()
            );
            try {
                enterpriseWeChatService.push(content, wetChatId, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
