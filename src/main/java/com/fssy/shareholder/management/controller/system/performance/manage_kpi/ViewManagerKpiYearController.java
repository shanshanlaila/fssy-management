package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiYear;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiYear;
import com.fssy.shareholder.management.service.common.override.ViewManagerKpiYearSheetOutputService;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiYearService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * VIEW 经理人年度kpi指标 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-11-03
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/view-manager-kpi-year")
public class ViewManagerKpiYearController {
    @Autowired
    private ViewManagerKpiYearService viewManagerKpiYearService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;
    @Autowired
    private CompanyService companyService;

    /**
     * 经理人年度KPI管理页面
     * @param model
     * @return
     */
    @RequiredLog("经理人年度KPI管理")
    @RequiresPermissions("system:performance:manager_kpi:view-manager-kpi-year:index1")
    @GetMapping("index1")
    public String manageIndex(Model model){
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager_kpi/view-manager-kpi-year/view-manager-kpi-year-list";
    }
    /**
     * 返回经理人年度KPI管理数据
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String,Object> getViewManagerKpiYearDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        //获取前端公司查询的主键
        String companyIds = request.getParameter("companyIds");
        params.put("companyId",companyIds);
        Page<ViewManagerKpiYear> viewManagerKpiYearPage = viewManagerKpiYearService.findViewManagerKpiYearDataListPerPageByParams(params);
        if (viewManagerKpiYearPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", viewManagerKpiYearPage.getTotal());
            result.put("data", viewManagerKpiYearPage.getRecords());
        }
        return result;
    }
    /**
     * 修改经理人KPI信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        ViewManagerKpiYear kpiYearServiceById = viewManagerKpiYearService.getById(id);
        model.addAttribute("kpiYearServiceById", kpiYearServiceById);
        return "system/performance/manager_kpi/view-manager-kpi-year/view-manager-kpi-year-edit";
    }

    /**
     * 更新经理人KPI信息
     * @param managerKpiYear
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerKpiYear managerKpiYear) {

        boolean result = viewManagerKpiYearService.updateManagerKpiYearData(managerKpiYear);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数信息没有更新，请检查数据后重新尝试");
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
        if (!ObjectUtils.isEmpty(request.getParameter("projectDesc"))) {
            params.put("projectDesc", request.getParameter("projectDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("unit"))) {
            params.put("unit", request.getParameter("unit"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("dataSource"))) {
            params.put("dataSource", request.getParameter("dataSource"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkCompany"))) {
            params.put("benchmarkCompany", request.getParameter("benchmarkCompany"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkValue"))) {
            params.put("benchmarkValue", request.getParameter("benchmarkValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastThreeYearsActual"))) {
            params.put("pastThreeYearsActual", request.getParameter("pastThreeYearsActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastTwoYearsActual"))) {
            params.put("pastTwoYearsActual", request.getParameter("pastTwoYearsActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastOneYearActual"))) {
            params.put("pastOneYearActual", request.getParameter("pastOneYearActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("basicTarget"))) {
            params.put("basicTarget", request.getParameter("basicTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mustInputTarget"))) {
            params.put("mustInputTarget", request.getParameter("mustInputTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("reachTarget"))) {
            params.put("reachTarget", request.getParameter("reachTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("challengeTarget"))) {
            params.put("challengeTarget", request.getParameter("challengeTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("proportion"))) {
            params.put("proportion", request.getParameter("proportion"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("companyIds"))) {
            params.put("companyIds", request.getParameter("companyIds"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("companyId"))) {
            params.put("companyId", request.getParameter("companyId"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("companyList"))) {
            params.put("companyList", request.getParameter("companyList"));
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

        //Sql语句 companyName,year
        params.put("select", "id,projectDesc,unit,dataSource,projectType,benchmarkCompany,benchmarkValue," +
                "pastThreeYearsActual,pastTwoYearsActual,pastOneYearActual,basicTarget,mustInputTarget" +
                "reachTarget,challengeTarget");
        //查询
        List<Map<String,Object>> viewManagerKpiYearDataByParams = viewManagerKpiYearService.findViewManagerKpiYearDataByParams(params);

        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
//        fieldMap.put("companyName", "填报企业");
//        fieldMap.put("year", "年份");
        fieldMap.put("projectType", "项目类别");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("unit", "单位");
        fieldMap.put("dataSource", "数据来源部门");
        fieldMap.put("benchmarkCompany", "对标标杆公司名称");
        fieldMap.put("benchmarkValue", "标杆值");
        fieldMap.put("pastThreeYearsActual", "n-3年值");
        fieldMap.put("pastTwoYearsActual", "n-2年值");
        fieldMap.put("pastOneYearActual", "n-1年值");
        fieldMap.put("basicTarget", "基本目标");
        fieldMap.put("mustInputTarget", "必达目标");
        fieldMap.put("reachTarget", "达标目标");
        fieldMap.put("challengeTarget", "挑战目标");
        fieldMap.put("经理人姓名", "经理人姓名");
        fieldMap.put("是否总经理", "是否总经理");
        fieldMap.put("职位类别", "职位类别");
        fieldMap.put("权重", "权重");
        fieldMap.put("备注", "备注");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18);
        //选用导出模板
        ViewManagerKpiYearSheetOutputService viewManagerKpiYearSheetOutputService = new ViewManagerKpiYearSheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(viewManagerKpiYearDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        viewManagerKpiYearSheetOutputService.exportNum("经理人年度KPI", viewManagerKpiYearDataByParams, fieldMap, response, strList, null);
    }
    /**
     * 返回附件上传页面
     * @param model
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:performance:manager_kpi:view-manager-kpi-year:index")
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
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/performance/manager_kpi/view-manager-kpi-year/view-manager-kpi-year-attachment-list";
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
            Map<String, Object> resultMap = viewManagerKpiYearService.readViewManagerKpiYearDataSource(result,request);
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {// "failed" : true
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
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
