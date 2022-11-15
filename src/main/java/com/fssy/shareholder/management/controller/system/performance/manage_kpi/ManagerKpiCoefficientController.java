package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiCoefficient;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiYear;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiYear;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManagerKpiCoefficientSheetOutputService;
import com.fssy.shareholder.management.service.common.override.ViewManagerKpiYearSheetOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiCoefficientService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 经理人绩效系数表 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-11-07
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/manager-kpi-coefficient")
public class ManagerKpiCoefficientController {

    @Autowired
    private ManagerKpiCoefficientService managerKpiCoefficientService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 经理人项目难度系数
     * @param model
     * @return
     */
    @RequiredLog("经理人项目难度系数管理")
    @RequiresPermissions("system:performance:manager_kpi:manager-kpi-coefficient:index1")
    @GetMapping("index1")
    public String manageIndex(Model model){
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/manager-kpi-coefficient/manager-kpi-coefficient-list";
    }
    /**
     * 返回经理人项目难度系数管理数据
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String,Object> getManagerKpiCoefficientDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerKpiCoefficient> managerKpiCoefficientPage = managerKpiCoefficientService.findManagerKpiCoefficientDataListPerPageByParams(params);
        if (managerKpiCoefficientPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerKpiCoefficientPage.getTotal());
            result.put("data", managerKpiCoefficientPage.getRecords());
        }
        return result;
    }
    /**
     * 修改经理人系数信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        ManagerKpiCoefficient byId = managerKpiCoefficientService.getById(id);
        model.addAttribute("managerKpiCoefficient", byId);
        return "/system/performance/manager_kpi/manager-kpi-coefficient/manager-kpi-coefficient-edit";
    }

    /**
     * 更新经理人系数信息
     * @param managerKpiCoefficient
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerKpiCoefficient managerKpiCoefficient) {

        boolean result = managerKpiCoefficientService.updateManagerKpiCoefficientData(managerKpiCoefficient);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数信息没有更新，请检查数据后重新尝试");
    }
    /**
     * excel 导出
     * @param request 请求
     * @param response 响应
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> params = getParams(request);
        //Sql语句
        params.put("select","companyName,year,id,managerName,position,generalManager,projectDesc,difficultCoefficient,incentiveCoefficient");
        //查询
        List<Map<String,Object>> managerKpiCoefficientMapDataByParams = managerKpiCoefficientService.findManagerKpiCoefficientMapDataByParams(params);
        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<>();

        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("managerName", "经理人姓名");
        fieldMap.put("position", "职务");
        fieldMap.put("generalManager", "是否总经理");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("difficultCoefficient", "绩效难度系数");
        fieldMap.put("incentiveCoefficient", "绩效考核系数");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0,1,2,3,4,5,6);
        SheetOutputService sheetOutputService = new ManagerKpiCoefficientSheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(managerKpiCoefficientMapDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经理人项目难度系数表", managerKpiCoefficientMapDataByParams, fieldMap, response, strList, null);
    }
    /**
     * 返回附件上传页面
     * @param model
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:performance:manager_kpi:manager-kpi-coefficient:index")
    @GetMapping("index")
    public String materialDataAttachmentIndex(Model model){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "经理人项目难度系数表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules))
        {
            System.out.println(importModules);
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经理人项目难度系数表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "/system/performance/manager_kpi/manager-kpi-coefficient/manager-kpi-coefficient-attachment-list";
    }
    /**
     * 附件上传
     * @param file 前台传来的附件数据
     * @param attachment 附件表的实体类
     * @param request
     * @return 附件id
     */
    @RequiredLog("经理人项目难度系数报表附件上传")
    @PostMapping("uploadFile")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request){
        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");
        if (org.springframework.util.ObjectUtils.isEmpty(params.get("companyName"))) {
            throw new ServiceException("未选择公司，导入失败");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("未选择年份，导入失败");
        }
        //保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (org.springframework.util.ObjectUtils.isEmpty(module))
        {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        // 上传文件至数据库的类别，主要目的是分类展示
        Attachment result = fileAttachmentTool.storeFileToModule(file, module,attachment);
        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = managerKpiCoefficientService.readManagerKpiCoefficientDataSource(result,companyName,year);
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

    //获取数据库里的数据
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("upCoefficientCause"))) {
            params.put("upCoefficientCause", request.getParameter("upCoefficientCause"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("difficultCoefficient"))) {
            params.put("difficultCoefficient", request.getParameter("difficultCoefficient"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("incentiveCoefficient"))) {
            params.put("incentiveCoefficient", request.getParameter("incentiveCoefficient"));
        }
        return params;
    }
}

