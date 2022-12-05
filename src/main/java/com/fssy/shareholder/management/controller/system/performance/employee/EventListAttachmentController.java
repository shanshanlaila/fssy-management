/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

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
	 * 导入场景服务实现类
	 */
	@Autowired
	private ImportModuleService importModuleService;

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
		attachment.setImportDate(calendar.getTime());// 设置时间
		// 查询导入场景对象
		ImportModule module = importModuleService
				.findById(InstandTool.integerToLong(attachment.getModule()));
		if (ObjectUtils.isEmpty(module))
		{
			throw new ServiceException(
					String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
		}
		Attachment result = fileAttachmentTool.storeFileToModule(file, module, // 上传文件至数据库的类别，主要目的是分类展示
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

	/**
	 * 返回无标准事件清单附件页面
	 *
	 * @param model model对象
	 * @return
	 */
    @RequiredLog("无标准事件附件管理")
	@GetMapping("withoutStandardImport")
	@RequiresPermissions("system:performance:employee:event:attachment:withoutStandardImport")
	public String showWithoutStandardImportPage(Model model)
	{
		// 查询导入场景
		Map<String, Object> params = new HashMap<>();
		params.put("noteEq", "无标准事件清单");
		List<ImportModule> importModules = importModuleService
				.findImportModuleDataListByParams(params);
		if (ObjectUtils.isEmpty(importModules))
		{
			throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "完整事件清单"));
		}
		model.addAttribute("module", importModules.get(0).getId());
		return "system/performance/events-list-attachment-list";
	}

    @PostMapping("withoutStandardUploadFile")
    @RequiredLog("无标准事件清单附件上传")
    @ResponseBody
    public SysResult withoutStandardUploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
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
