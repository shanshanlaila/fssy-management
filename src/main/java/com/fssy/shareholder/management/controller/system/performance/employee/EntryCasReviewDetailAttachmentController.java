/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MI
 * @ClassName: EntryCasPlanDetailAttachmentController
 * @Description: 履职计划附件管理控制器
 * @date 2022/10/14 10:19
 */
@Controller
@RequestMapping("/system/entry-cas-review-detail/attachment/")
public class EntryCasReviewDetailAttachmentController {

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    @Autowired
    private EntryCasReviewDetailService entryCasReviewDetailService;


    /**
     * 履职计划回顾附件列表（导入履职回顾）
     *
     * @return 履职计划回顾附件列表html路径
     */
    @RequiredLog("履职计划附件列表")
    @GetMapping("import")
    @RequiresPermissions("system:performance:entryCasReviewDetail:import")
    public String showImportPage(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);

        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "履职计划回顾");
        List<ImportModule> importModules = importModuleService.findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "履职计划回顾"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/performance/employee/entry-cas-plan-review-detail-attachment-list";
    }

    /**
     * 履职回顾附件上传导入
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("履职计划回顾上传导入")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());// 设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService.findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);

        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap =entryCasReviewDetailService.readEntryCasReviewDetailDataSource(result);
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
}
