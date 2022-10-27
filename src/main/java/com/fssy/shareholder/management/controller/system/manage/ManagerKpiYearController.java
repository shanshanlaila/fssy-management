package com.fssy.shareholder.management.controller.system.manage;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.manage.ManagerKpiYear;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.manage.ManagerKpiYearService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 经理人年度kpi指标 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-10-26
 */
@Controller
@RequestMapping("/system/manager/manager-kpi-year")
public class ManagerKpiYearController {
    @Autowired
    private ManagerKpiYearService managerKpiYearService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 经理人年度KPI管理页面
     * @param model
     * @return
     */
    @RequiredLog("经理人年度KPI管理")
    @RequiresPermissions("system:manager:manager-kpi-year:index1")
    @GetMapping("index1")
    public String manageIndex(Model model){
        Map<String, Object> params = new HashMap<>();
        return "system/manager/manager-kpi-year/manager-kpi-year-list";
    }

    /**
     * 返回经理人年度KPI管理数据
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String,Object> getManagerKpiYearDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerKpiYear> managerKpiYearPage = managerKpiYearService.findManagerKpiYearDataListPerPageByParams(params);
        if (managerKpiYearPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerKpiYearPage.getTotal());
            result.put("data", managerKpiYearPage.getRecords());
        }
        return result;
    }
    /**
     * 与数据库进行匹配
     * @param request
     * @return
     */
    private Map<String,Object> getParams(HttpServletRequest request){
        Map<String,Object> params =new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectDesc"))) {
            params.put("projectDesc", request.getParameter("projectDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        return params;
    }

    /**
     * excel 导出
     * @param request 请求
     * @param response 响应
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);

        //Sql语句
        params.put("select", "id,companyName,status,managerName,generalManager,position,note");
        //查询
        List<Map<String,Object>> managerKpiYearDataByParams = managerKpiYearService.findManagerKpiYearDataByParams(params);
//        List<Map<String,Object>> manageKpiYearMapDataByParams = manageKpiYearService.findManageKpiYearMapDataByParams(params1);
//        //将两张表查询出的内容合在一个List<map<String,Object>>中
//        for(int i=0;i<managerKpiYearDataByParams.size();i++){
//            Map<String, Object> params1 = userList.get(request);
//            map.put("age", "新加的参数");
//        }
        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("companyName", "企业名称");
        fieldMap.put("status", "项目状态");
        fieldMap.put("managerName", "经理人姓名");
        fieldMap.put("generalManager", "是否总经理");
        fieldMap.put("position", "职务");
        fieldMap.put("year", "年份");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("note", "备注");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0,1,2,3,4,5,6,7,8);
        //选用导出模板
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(managerKpiYearDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经理人年度KPI项目指标", managerKpiYearDataByParams, fieldMap, response, strList, null);
    }
    /**
     * 返回附件上传页面
     * @param model
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:manager:manager-kpi-year:index")
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
        params.put("noteEq", "经理人年度KPI");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules))
        {
            System.out.println(importModules);
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经理人年度KPI"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/manager/manager-kpi-year/manager-kpi-year-attachment-list";
    }
    /**
     * 附件上传
     * @param file 前台传来的附件数据
     * @param attachment 附件表的实体类
     * @param request
     * @return 附件id
     */
    @RequiredLog("经理人年度KPI项目指标附件上传")
    @PostMapping("uploadFile")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request){
        //保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module))
        {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        // 上传文件至数据库的类别，主要目的是分类展示
        Attachment result = fileAttachmentTool.storeFileToModule(file, module,attachment);
        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = managerKpiYearService.readManagerKpiYearDataSource(result);
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
}
