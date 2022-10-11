/*
 * @Title: fssy-management
 * @Description: TODO
 * @author MI
 * @date 2022/10/9 9:32
 * @version
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.Module;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author MI
 * @ClassName: EventListAttachmentController
 * @Description: 事件清单附件管理控制器
 * @date 2022/10/9 9:32
 */
@Controller
@RequestMapping("/system/performance/employee/eventsListAttachment/")
public class EventListAttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private EventListService eventListService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    /**
     * @return 事件列表展示路径
     */
    @GetMapping("import")
    public String showImportPage(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);

        model.addAttribute("importDateStart", importDateStart);
        return "system/performance/employee/events-attachment-list";
    }


    /**
     * 事件列表附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("事件清单附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//设置时间
        Attachment result = fileAttachmentTool.storeFileToModule(file, Module.PERFORMANCE_EVENT_LIST,//上传文件至数据库的类别，主要目的是分类展示
                attachment);

        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = eventListService.readEventListDataSource(result);
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {// "failed" : true
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), String.valueOf(resultMap.get("content")));
                return SysResult.build(200, "部分数据导入成功，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
            } else {
                // 修改附件为导入成功
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), "导入成功");// 表格备注中的内容
                return SysResult.ok();
            }
        } catch (ServiceException e) {
            // 修改附件的导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString(), e.getMessage());
            throw new ServiceException("计划导入失败，失败原因请查看附件列表备注描述，更改后请重新导入数据");
        } catch (Exception e) {
            // 修改附件导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("withoutStandardUploadFile")
    @RequiredLog("无标准事件清单附件上传")
    @ResponseBody
    public SysResult withoutStandardUploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
        //保存附件
        //上传文件至数据库的类别，主要目的是分类展示
        Attachment result = fileAttachmentTool.storeFileToModule(file, Module.PERFORMANCE_EVENTS_LIST, attachment);
        try {
            //读取附件并保存数据
            Map<String, Object> resultMap = eventListService.readEventListWithoutStandardDataSource(result);
            //判断是否失败，实现类中的setFailedContent()
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS
                        , result.getId().toString(), String.valueOf(resultMap.get("content")));
                return SysResult.build(200, "部分数据导入成功，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
            } else {
                //修改附件为导入成功
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), "导入成功");
                return SysResult.ok();
            }
        } catch (ServiceException e) {
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString(), e.getMessage());
            throw new ServiceException("计划导入失败，失败原因请查看附件列表备注描述，更改后请重新导入数据");
        } catch (Exception e) {
            //修改附件导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED, result.getId().toString());
            e.printStackTrace();
            throw e;
        }
    }
}
