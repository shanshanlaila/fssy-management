package com.fssy.shareholder.management.controller.system.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.AttachmentSecret;
import com.fssy.shareholder.management.service.system.config.AttachmentSecretService;
import com.fssy.shareholder.management.tools.common.FileAttachmentSecretTool;
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
 * @author TerryZeng
 * @title: AttachmentSecretController
 * @description: 销售等保密附件 控制器
 * @date 2021/10/30
 */
@Controller
@RequestMapping("/system/config/attachment-secret/")
public class AttachmentSecretController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AttachmentSecretController.class);

    @Autowired
    private AttachmentSecretService attachmentSecretService;

    @Autowired
    private FileAttachmentSecretTool fileAttachmentSecretTool;

    /**
     * 根据条件返回附件列表页面数据
     *
     * @param request 请求实体
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getAttachmentSecretDatas(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        // 获取limit和page
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        // 构建参数查找表
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("md5Path"))) {
            params.put("md5Path", request.getParameter("md5Path"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("filename"))) {
            params.put("filename", request.getParameter("filename"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("importStatus"))) {
            params.put("importStatus", request.getParameter("importStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("importDateStart"))) {
            params.put("importDateStart",
                    request.getParameter("importDateStart"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("importDateEnd"))) {
            params.put("importDateEnd", request.getParameter("importDateEnd"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("module"))) {
            params.put("module", request.getParameter("module"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdId"))) {
            params.put("createdId", request.getParameter("createdId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("contractAttachmentIds"))) {
            params.put("contractAttachmentIds", request.getParameter("contractAttachmentIds"));
        }
        LOGGER.debug(String.format("查询参数为:%s", params.toString()));

        Page<AttachmentSecret> attachmentSecretPage = attachmentSecretService.findAttachmentSecretDataListPerPageByParams(params);

        if (attachmentSecretPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");

        } else {
            result.put("code", 0);
            result.put("count", attachmentSecretPage.getTotal());
            result.put("data", attachmentSecretPage.getRecords());
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
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        String id = String.valueOf(request.getParameter("id"));
        String md5Path = String.valueOf(request.getParameter("md5Path"));

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("md5Path", md5Path);

        LOGGER.debug(String.format("查询参数为:%s", params.toString()));
        List<AttachmentSecret> attachmentSecretList = attachmentSecretService
                .findAttachmentSecretDataListByParams(params);

        if (ObjectUtils.isEmpty(attachmentSecretList)) {
            throw new ServiceException("附件无法找到");
        }

        if (attachmentSecretList.size() > 1) {
            throw new ServiceException("找到附件不唯一，请联系管理员");
        }

        // Load file as Resource
        AttachmentSecret attachmentSecret = attachmentSecretList.get(0);
        Resource resource = fileAttachmentSecretTool.loadFileAsResource(
                attachmentSecret.getPath(), attachmentSecret.getFilename());

        // 分析附件的content type
        String contentType = null;
        try {
            contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new ServiceException("找不到附件类型，请联系管理员");
        }

        // 设置默认的content type
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""
                                    + URLEncoder.encode(
                                    attachmentSecret.getFilename(), "utf-8")
                                    + "\"")
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.toString());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + attachmentSecret.getFilename()
                                    + "\"")
                    .body(resource);
        }
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult destroy(@PathVariable(value = "id") Integer id) {
        boolean result = attachmentSecretService.deleteAttachmentSecretById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "附件没有删除，请确认操作后重新尝试");
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
        List<AttachmentSecret> attachmentList = attachmentSecretService
                .findAttachmentSecretDataListByParams(params);

        if (ObjectUtils.isEmpty(attachmentList))
        {
            throw new ServiceException("附件无法找到");
        }

        // Load file as Resource
        AttachmentSecret attachment = attachmentList.get(0);
        Resource resource = fileAttachmentSecretTool.loadFileAsResource(
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
