package com.fssy.shareholder.management.schedule.job.system.manage.manager;

import com.fssy.shareholder.management.service.common.EnterpriseWeChatService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import me.chanjar.weixin.common.error.WxErrorException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * @author Shizn
 * @Title: ProfitStatementInitJob.java
 * @Description: 经理人KPI分数生成定时风险分析
 * @date 2023年2月21日 上午9:27:18
 */
public class ManagerAnalysisJob implements Job {
    /**
     * 定时任务记录表功能业务实现类
     */
    @Autowired
    private EnterpriseWeChatService enterpriseWeChatService;

    /**
     * 经理人KPI绩效分析实现类
     */
    @Autowired
    private ManagerKpiScoreServiceOld managerKpiScoreServiceOld;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long, Map<String, Object>> kpiScoreParams = managerKpiScoreServiceOld.findManagerKpiScoreParams();
        if (ObjectUtils.isEmpty(kpiScoreParams)) {
            return;
        }
        for (Map<String, Object> analysis : kpiScoreParams.values()) {
            List<String> weChatId = Collections.singletonList(analysis.get("weChat").toString());
            List<String> userName = Collections.singletonList(analysis.get("userName").toString());
            List<String> year = Collections.singletonList(analysis.get("year").toString());
            List<String> month = Collections.singletonList(analysis.get("month").toString());
            List<String> anomalyType = Collections.singletonList(analysis.get("anomalyType").toString());
            //发送到企业微信的信息
            String content = String.format("%s，您好，您在 %s年%s月 的绩效成绩出现 %s 异常，请及时联系相关负责人进行处置信息！",
                    userName,year,month,anomalyType
                    );
            try {
                enterpriseWeChatService.push(content, weChatId, null, null);
            } catch (WxErrorException e1) {
                e1.printStackTrace();
            }

        }
    }
}
