package com.fssy.shareholder.management.controller.system.manage;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiLib;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageKpiYearSheetOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.manage.ManageKpiLibService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 经营管理指标库 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-10-12
 */
@Controller
@RequestMapping("/system/manager/manage-kpi-lib/")
public class ManageKpiLibController {

    @Autowired
    private ManageKpiLibService manageKpiLibService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 页面列表展示
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    public String managerIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/manager/manage-kpi-lib/manage-kpi-lib-list";
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        //车型系列表主键查询
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectDesc"))) {
            params.put("projectDesc", request.getParameter("projectDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("isCommon"))) {
            params.put("isCommon", request.getParameter("isCommon"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("unit"))) {
            params.put("unit", request.getParameter("unit"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiDefinition"))) {
            params.put("kpiDefinition", request.getParameter("kpiDefinition"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiFormula"))) {
            params.put("kpiFormula", request.getParameter("kpiFormula"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiYear"))) {
            params.put("kpiYear", request.getParameter("kpiYear"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerKpi"))) {
            params.put("managerKpi", request.getParameter("managerKpi"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("cfoKpi"))) {
            params.put("cfoKpi", request.getParameter("cfoKpi"));
        }
        return params;
    }

    /**
     * 返回的经营管理指标库数据
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getVehicleSeriesDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = getParams(request);
        // 获取limit和page
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManageKpiLib> managerKpiLibPage = manageKpiLibService.findManagerKpiLibDataListPerPageByParams(params);

        if (managerKpiLibPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");

        } else {
            result.put("code", 0);
            result.put("count", managerKpiLibPage.getTotal());
            result.put("data", managerKpiLibPage.getRecords());
        }
        return result;
    }

    /**
     * 返回附件上传页面
     *
     * @param model model对象
     * @return 页面
     */
    @RequiredLog("附件上传")
    @GetMapping("index")
    public String materialDataAttachmentIndex(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "经理人KPI指标库");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经理人KPI指标库"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/manager/manage-kpi-lib/manage-kpi-lib-attachment-list";
    }

    /**
     * 附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("经理人年度KPI指标库附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, // 上传文件至数据库的类别，主要目的是分类展示
                attachment);


        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = manageKpiLibService.readManagerKpiLibDataSource(result);
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {// "failed" : true
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), String.valueOf(resultMap.get("content")));
                return SysResult.build(200, "表中有空值，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
            } else {
                // 修改附件为导入成功
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), "导入成功");// 表格备注中的内容
                return SysResult.ok();
            }
        } catch (ServiceException e) {
            // 修改附件的导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED, result.getId().toString(), e.getMessage());
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
     * excel 导出
     *
     * @param request  请求
     * @param response 相应
     */
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        params.put("select", "id,projectDesc,status,isCommon,unit,kpiDefinition,kpiFormula,kpiYear,managerKpi,cfoKpi,note");
        List<Map<String, Object>> managerKpiLibList = manageKpiLibService.findManagerKpiLibDataSource(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

        //需要改变背景色的格子
        fieldMap.put("id", "序号");
        fieldMap.put("projectDesc", "重点KPI");
        fieldMap.put("status", "状态");
        fieldMap.put("isCommon", "项目类型");
        fieldMap.put("unit", "单位");
        fieldMap.put("kpiDefinition", "指标定义");
        fieldMap.put("kpiFormule", "指标公式");
        fieldMap.put("kpiYear", "创建年份");
        fieldMap.put("managerKpi", "是否总经理kpi");
        fieldMap.put("cfoKpi", "是否财务总监KPI");
        fieldMap.put("note", "备注");

        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(managerKpiLibList)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经营管理指标库", managerKpiLibList, fieldMap, response, strList, null);

    }

}
