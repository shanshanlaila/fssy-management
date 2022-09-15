/**
 * 
 */
package com.fssy.shareholder.management.controller.system.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件管理控制器
 * 
 * @author Solomon
 */
@Controller
@RequestMapping("/system/config/attachment/")
public class AttachmentController
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AttachmentController.class);

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private FileAttachmentTool fileAttachmentTool;

	/**
	 * 根据条件返回附件列表页面数据
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getObjects")
	@ResponseBody
	public Map<String, Object> getAttachmentDatas(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		Map<String, Object> result = new HashMap<>();
		// 获取limit和page
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		params.put("limit", limit);
		params.put("page", page);

		LOGGER.debug(String.format("查询参数为:%s", params.toString()));

		Page<Attachment> attachmentPage = attachmentService.findAttachmentDataListPerPageByParams(params);
		if (attachmentPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");

		}
		else
		{
			result.put("code", 0);
			result.put("count", attachmentPage.getTotal());
			result.put("data", attachmentPage.getRecords());
		}
		return result;
	}

	/**
	 * 下载附件
	 * 
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> downloadFile(HttpServletRequest request)
	{
		String id = String.valueOf(request.getParameter("id"));
		String md5Path = String.valueOf(request.getParameter("md5Path"));

		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("md5Path", md5Path);

		LOGGER.debug(String.format("查询参数为:%s", params.toString()));
		List<Attachment> attachmentList = attachmentService
				.findAttachmentDataListByParams(params);

		if (ObjectUtils.isEmpty(attachmentList))
		{
			throw new ServiceException("附件无法找到");
		}

		if (attachmentList.size() > 1)
		{
			throw new ServiceException("找到附件不唯一，请联系管理员");
		}

		// Load file as Resource
		Attachment attachment = attachmentList.get(0);
		Resource resource = fileAttachmentTool.loadFileAsResource(
				attachment.getPath(), attachment.getFilename());

		// 分析附件的content type
		String contentType = null;
		try
		{
			contentType = request.getServletContext()
					.getMimeType(resource.getFile().getAbsolutePath());
		}
		catch (IOException ex)
		{
			throw new ServiceException("找不到附件类型，请联系管理员");
		}

		// 设置默认的content type
		if (contentType == null)
		{
			contentType = "application/octet-stream";
		}

		try
		{
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\""
									+ URLEncoder.encode(
											attachment.getFilename(), "utf-8")
									+ "\"")
					.body(resource);
		}
		catch (UnsupportedEncodingException e)
		{
			LOGGER.error(e.toString());
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + attachment.getFilename()
									+ "\"")
					.body(resource);
		}
	}

	@DeleteMapping("{id}")
	@ResponseBody
	public SysResult destroy(@PathVariable(value = "id") Integer id)
	{
		boolean result = attachmentService.deleteAttachmentById(id);
		if (result)
		{
			return SysResult.ok();
		}
		return SysResult.build(500, "附件没有删除，请确认操作后重新尝试");
	}

	/**
	 * 根据条件返回附件列表页面数据
	 *
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("getMapObjects")
	@ResponseBody
	public Map<String, Object> getAttachmentMapList(HttpServletRequest request)
	{
		Map<String, Object> params = getParams(request);
		Map<String, Object> result = new HashMap<>();
		// 获取limit和page
		int limit = Integer.parseInt(request.getParameter("limit"));
		int page = Integer.parseInt(request.getParameter("page"));
		params.put("limit", limit);
		params.put("page", page);

		LOGGER.debug(String.format("查询参数为:%s", params.toString()));

		Page<Map<String, Object>> attachmentPage = attachmentService.findAttachmentDataListMapByParams(params);
		if (attachmentPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");

		}
		else
		{
			result.put("code", 0);
			result.put("count", attachmentPage.getTotal());
			result.put("data", attachmentPage.getRecords());
		}
		return result;
	}

	/**
	 * 查询条件
	 *
	 * @param request
	 * @return
	 */
	public Map<String, Object> getParams(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<>();
		// 构建参数查找表
		if (!ObjectUtils.isEmpty(request.getParameter("id")))
		{
			params.put("id", request.getParameter("id"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("md5Path")))
		{
			params.put("md5Path", request.getParameter("md5Path"));
		}
		if(!ObjectUtils.isEmpty(request.getParameter("createdName")))
		{
			params.put("createdName",request.getParameter("createdName"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("filename")))
		{
			params.put("filename", request.getParameter("filename"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("status")))
		{
			params.put("status", request.getParameter("status"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("importStatus")))
		{
			params.put("importStatus", request.getParameter("importStatus"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("importDateStart")))
		{
			params.put("importDateStart",
					request.getParameter("importDateStart"));
		}
		if (!ObjectUtils.isEmpty(request.getParameter("importDateEnd")))
		{
			params.put("importDateEnd", request.getParameter("importDateEnd"));
		}
		if(!ObjectUtils.isEmpty(request.getParameter("module")))
		{
			params.put("module",request.getParameter("module"));
		}
		// 2022-3-15新增，查找合同附件
		if(!ObjectUtils.isEmpty(request.getParameter("contractAttachmentIds")))
		{
			params.put("contractAttachmentIds",request.getParameter("contractAttachmentIds"));
		}
		if(!ObjectUtils.isEmpty(request.getParameter("createdId")))
		{
			params.put("createdId",request.getParameter("createdId"));
		}
		// 2022-5-9新增，用于区分定时获取的文件和从前端导入的文件
		if(!ObjectUtils.isEmpty(request.getParameter("dataSources")))
		{
			params.put("dataSources",request.getParameter("dataSources"));
		}
		if(!ObjectUtils.isEmpty(request.getParameter("moduleList")))
		{
			params.put("moduleList",request.getParameter("moduleList"));
		}
		return params;
	}

	/**
	 * 下载模板
	 *
	 * @param request 请求实体
	 * @return
	 */
	@GetMapping("/downloadModelFile")
	public ResponseEntity<Resource> downloadModelFile(HttpServletRequest request)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("conclusionEq", request.getParameter("conclusionEq"));
		params.put("module", request.getParameter("module"));
		params.put("orderByDesc", "id");

		LOGGER.debug(String.format("查询参数为:%s", params.toString()));
		List<Attachment> attachmentList = attachmentService
				.findAttachmentDataListByParams(params);

		if (ObjectUtils.isEmpty(attachmentList))
		{
			throw new ServiceException("附件无法找到");
		}

		// Load file as Resource
		Attachment attachment = attachmentList.get(0);
		Resource resource = fileAttachmentTool.loadFileAsResource(
				attachment.getPath(), attachment.getFilename());

		// 分析附件的content type
		String contentType = null;
		try
		{
			contentType = request.getServletContext()
					.getMimeType(resource.getFile().getAbsolutePath());
		}
		catch (IOException ex)
		{
			throw new ServiceException("找不到附件类型，请联系管理员");
		}

		// 设置默认的content type
		if (contentType == null)
		{
			contentType = "application/octet-stream";
		}

		try
		{
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\""
									+ URLEncoder.encode(
									attachment.getFilename(), "utf-8")
									+ "\"")
					.body(resource);
		}
		catch (UnsupportedEncodingException e)
		{
			LOGGER.error(e.toString());
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + attachment.getFilename()
									+ "\"")
					.body(resource);
		}
	}
}
