package com.fssy.shareholder.management.controller.system.operate.company;


import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.company.FinanceDataService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_attachment_list	**数据表中文名：	非权益投资项目管理附件清单	**业务部门：	经营管理部	**数据表作用：	非权益投资项目管理附件清单	**创建人创建日期：	TerryZeng 2022-12-2 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2022-12-05
 */
@Controller
@RequestMapping("/system/operate/company/FinanceDataAttachment/")
public class FinanceDataAttachmentController {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private FinanceDataService financeDataService;
    /**
     * 导入场景服务实现类
     */
    @Autowired
    private ImportModuleService importModuleService;
    /**
     * 返回企业财务基础数据附件页面
     *
     * @param model model对象
     * @return
     */
    @RequiredLog("企业财务基础数据附件管理")
    @GetMapping("import")
    @RequiresPermissions("system:operate:invest:ProjectAttachmentList:import")
    public String showImportPage(Model model)
    {
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "企业财务基础数据附件");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules))
        {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "企业财务基础数据附件"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/operate/company/finance-data-attachment-list";
    }

    @PostMapping("uploadFile")
    @RequiredLog("企业财务基础数据附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
        // 保存附件
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module))
        {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);
        try {
            //读取附件并保存数据
            Map<String, Object> resultMap = financeDataService.readFinanceDataDataSource(result);
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
