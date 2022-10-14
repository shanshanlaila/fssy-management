/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import java.util.Calendar;
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
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleAttachmentService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @Title: PerformanceEventsRelationRoleAttachmentController.java
 * @Description: 事件清单岗位关系附件管理控制器
 * @author Solomon
 * @date 2022年10月11日 上午12:40:55
 */
@Controller
@RequestMapping("/system/performance/employee/events-role-attachment/")
public class PerformanceEventsRelationRoleAttachmentController
{
	/**
	 * 导入场景服务实现类
	 */
	@Autowired
	private ImportModuleService importModuleService;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private FileAttachmentTool fileAttachmentTool;

	/**
	 * 事件清单岗位关系附件功能业务实现类
	 */
	@Autowired
	private EventsRelationRoleAttachmentService eventsRelationRoleAttachmentService;

	/**
	 * 事件岗位关系附件管理
	 * 
	 * @param model model对象
	 * @return
	 */
	@RequiredLog("事件岗位关系附件管理")
	@GetMapping("show")
	@RequiresPermissions("system:performance:employee:event:role:attachment:show")
	public String index(Model model)
	{
		// 查询导入场景
		Map<String, Object> params = new HashMap<>();
		String desc = "事件岗位关系";
		params.put("noteEq", desc);
		List<ImportModule> importModules = importModuleService
				.findImportModuleDataListByParams(params);
		if (ObjectUtils.isEmpty(importModules))
		{
			throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", desc));
		}
		model.addAttribute("module", importModules.get(0).getId());
		return "system/performance/employee/event-relation-role-attachment-show";
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
			HttpServletRequest request)
	{
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

		try
		{
			// 读取附件并保存数据
			Map<String, Object> resultMap = eventsRelationRoleAttachmentService
					.readAttachmentToData(result);
			if (Boolean.parseBoolean(resultMap.get("failed").toString()))
			{// "failed" : true
				attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
						result.getId().toString(), String.valueOf(resultMap.get("content")));
				return SysResult.build(200, "部分数据导入成功，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
			}
			else
			{
				// 修改附件为导入成功
				attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
						result.getId().toString(), "导入成功");// 表格备注中的内容
				return SysResult.ok();
			}
		}
		catch (ServiceException e)
		{
			// 修改附件的导入状态为失败
			attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
					result.getId().toString(), e.getMessage());
			throw new ServiceException("计划导入失败，失败原因请查看附件列表备注描述，更改后请重新导入数据");
		}
		catch (Exception e)
		{
			// 修改附件导入状态为失败
			attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
					result.getId().toString());
			e.printStackTrace();
			throw e;
		}
	}
}
