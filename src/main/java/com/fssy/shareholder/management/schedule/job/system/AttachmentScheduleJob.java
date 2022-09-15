package com.fssy.shareholder.management.schedule.job.system;

import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 定时清理系统附件的类
 *
 * @author QinHui
 * @title: AttachmentScheduleJob
 * @description:
 * @date 2022/5/3
 */
public class AttachmentScheduleJob implements Job
{

    @Autowired
    private AttachmentService attachmentService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        // 删除附件文件，将附件表中的数据设置为失效，并且情况原备注。
        // 2个月前的数据才做清理
        System.out.println(">>>>>>>>>>>>>>>文件清理启动：" + LocalDateTime.now()+"<<<<<<<<<<<<<<<<");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date date = calendar.getTime();
        Map<String, Object> attachmentParams = new HashMap<>();
        attachmentParams.put("createdAtEnd", date);
        attachmentParams.put("status", 1);
        List<Attachment> attachmentList = attachmentService
                .findAttachmentDataListByParams(attachmentParams);
        for (Attachment attachment : attachmentList)
        {
            attachmentService.changeFileStatus(CommonConstant.FALSE,
                    attachment.getId().toString(), "文件过期");
            String str = "CRON ----> schedule attachmentJob is running ... + " + attachment.getFilename()
                    + "  文件上传时间---->  " + attachment.getCreatedAt() + "    当前清理时间----->" + LocalDateTime.now();
            System.out.println(str);
        }
        System.out.println(">>>>>>>>>>>>>>>文件清理结束：" + LocalDateTime.now()+"<<<<<<<<<<<<<<<<");
    }
}
