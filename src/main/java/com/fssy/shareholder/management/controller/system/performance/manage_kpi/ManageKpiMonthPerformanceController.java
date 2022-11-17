package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthPerformanceMapper;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthPerformance;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiCoefficient;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageKpiMonthPerformanceSheetOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthPerformanceService;
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
 * 经营管理月度实绩管理 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-10-24
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/manage-kpi-month-performance")
public class ManageKpiMonthPerformanceController {
    @Autowired
    private ManageKpiMonthPerformanceService manageKpiMonthPerformanceService;
    @Autowired
    private ManageKpiMonthPerformanceMapper manageKpiMonthPerformanceMapper;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 经营管理月度实绩管理页面
     *
     * @param model
     * @return
     */
    @RequiredLog("经营管理月度实绩管理")
    @RequiresPermissions("system:performance:manager_kpi:manage-kpi-month-performance:index1")
    @GetMapping("index1")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/manage-kpi-month-performance/manage-kpi-month-performance-list";
    }

    /**
     * 返回经营管理月度实绩管理数据
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManageKpiMonthDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<Map<String, Object>> manageKpiMonthPage = manageKpiMonthPerformanceService.findManageKpiMonthDataMapListPerPageByParams(params);
        if (manageKpiMonthPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", manageKpiMonthPage.getTotal());
            result.put("data", manageKpiMonthPage.getRecords());
        }
        return result;
    }

    /**
     * 修改经营管理指标月度实绩信息
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        //前端获取年度id
        String manageKpiYearId = request.getParameter("manageKpiYearId");
        //System.out.println("***************"+manageKpiYearId);
//        String month = request.getParameter("month");
//        //通过年度id、月份查询出写入月份的id
//        QueryWrapper<ManageKpiMonthPerformance> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("manageKpiYearId",id).eq("month",month);
//        List<ManageKpiMonthPerformance> manageKpiMonthPerformances = manageKpiMonthPerformanceMapper.selectList(queryWrapper);
//        //id代入实现类中
//        Map<String, Object> params = getParams(request);
//        Page<Map<String, Object>> mapListPerPageByParams = manageKpiMonthPerformanceMapper.selectMapsPage(params);
        ManageKpiMonthPerformance byId = manageKpiMonthPerformanceService.getById(manageKpiYearId);
        model.addAttribute("manageKpiMonthPerformance", byId);
        return "/system/performance/manager_kpi/manage-kpi-month-performance/manage-kpi-month-performance-edit";
    }

    /**
     * 更新经营管理指标月度实绩信息
     *
     * @param manageKpiMonthPerformance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManageKpiMonthPerformance manageKpiMonthPerformance) {

        boolean result = manageKpiMonthPerformanceService.updateManageKpiMonthPerformanceData(manageKpiMonthPerformance);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数信息没有更新，请检查数据后重新尝试");
    }

    /**
     * 与数据库进行匹配
     *
     * @param request
     * @return
     */
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthATarget"))) {
            params.put("monthATarget", request.getParameter("monthATarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthAActual"))) {
            params.put("monthAActual", request.getParameter("monthAActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiDefinition"))) {
            params.put("kpiDefinition", request.getParameter("kpiDefinition"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectDesc"))) {
            params.put("projectDesc", request.getParameter("projectDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiFormula"))) {
            params.put("kpiFormula", request.getParameter("kpiFormula"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("performanceMark"))) {
            params.put("performanceMark", request.getParameter("performanceMark"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("evaluateMode"))) {
            params.put("evaluateMode", request.getParameter("evaluateMode"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("dataSource"))) {
            params.put("dataSource", request.getParameter("dataSource"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("unit"))) {
            params.put("unit", request.getParameter("unit"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget"))) {
            params.put("monthTarget", request.getParameter("monthTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActualValue"))) {
            params.put("monthActualValue", request.getParameter("monthActualValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateTarget"))) {
            params.put("accumulateTarget", request.getParameter("accumulateTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateActual"))) {
            params.put("accumulateActual", request.getParameter("accumulateActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("analyzeRes"))) {
            params.put("analyzeRes", request.getParameter("analyzeRes"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget1"))) {
            params.put("monthTarget1", request.getParameter("monthTarget1"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget2"))) {
            params.put("monthTarget2", request.getParameter("monthTarget2"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget3"))) {
            params.put("monthTarget3", request.getParameter("monthTarget3"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget4"))) {
            params.put("monthTarget4", request.getParameter("monthTarget4"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget5"))) {
            params.put("monthTarget5", request.getParameter("monthTarget5"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget6"))) {
            params.put("monthTarget6", request.getParameter("monthTarget6"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget7"))) {
            params.put("monthTarget7", request.getParameter("monthTarget7"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget8"))) {
            params.put("monthTarget8", request.getParameter("monthTarget8"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget9"))) {
            params.put("monthTarget9", request.getParameter("monthTarget9"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget10"))) {
            params.put("monthTarget10", request.getParameter("monthTarget10"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget11"))) {
            params.put("monthTarget11", request.getParameter("monthTarget11"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget12"))) {
            params.put("monthTarget12", request.getParameter("monthTarget12"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual1"))) {
            params.put("monthActual1", request.getParameter("monthActual1"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual2"))) {
            params.put("monthActual2", request.getParameter("monthActual2"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual3"))) {
            params.put("monthActual3", request.getParameter("monthActual3"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual4"))) {
            params.put("monthActual4", request.getParameter("monthActual4"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual5"))) {
            params.put("monthActual5", request.getParameter("monthActual5"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual6"))) {
            params.put("monthActual6", request.getParameter("monthActual6"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual7"))) {
            params.put("monthActual7", request.getParameter("monthActual7"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual8"))) {
            params.put("monthActual8", request.getParameter("monthActual8"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual9"))) {
            params.put("monthActual9", request.getParameter("monthActual9"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual10"))) {
            params.put("monthActual10", request.getParameter("monthActual10"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual11"))) {
            params.put("monthActual11", request.getParameter("monthActual11"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActual12"))) {
            params.put("monthActual12", request.getParameter("monthActual12"));
        }
        return params;
    }

    /**
     * excel 导出
     *
     * @param request  请求
     * @param response 响应
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        //Sql语句
        params.put("select", "id,projectType,projectDesc,kpiFormula,dataSource,unit,benchmarkCompany," +
                "benchmarkValue,pastThreeYearsActual,pastTwoYearsActual,pastOneYearActual,basicTarget,mustInputTarget,reachTarget,challengeTarget," +
                "monthTarget,monthActualValue,accumulateTarget,accumulateActual,analyzeRes,performanceMark,evaluateMode");
        //查询
        List<Map<String, Object>> manageKpiMonthMapDataByParams = manageKpiMonthPerformanceService.findManageKpiMonthDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("projectType", "重点工作");
        fieldMap.put("projectDesc", "管理项目");
        fieldMap.put("kpiFormula", "管理项目定义");
        fieldMap.put("dataSource", "数据来源部门");
        fieldMap.put("unit", "单位");
        fieldMap.put("benchmarkCompany", "对标企业名称");
        fieldMap.put("benchmarkValue", "标杆值");
        fieldMap.put("pastThreeYearsActual", "过去第三年值");
        fieldMap.put("pastTwoYearsActual", "过去第二年值");
        fieldMap.put("pastOneYearActual", "过去第一年值");
        fieldMap.put("basicTarget", "基本目标");
        fieldMap.put("mustInputTarget", "必达目标");
        fieldMap.put("reachTarget", "达标目标");
        fieldMap.put("challengeTarget", "挑战目标");
        fieldMap.put("performanceMark", "经理人绩效指标标识");
        fieldMap.put("evaluateMode", "评分模式");
        fieldMap.put("monthTarget", "月度目标值");
        fieldMap.put("monthActualValue", "月度实绩值");
        fieldMap.put("accumulateTarget", "目标值(累计)");
        fieldMap.put("accumulateActual", "实际值(累计)");
        fieldMap.put("analyzeRes", "未达成或劣化原因分析");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21);
        SheetOutputService sheetOutputService = new ManageKpiMonthPerformanceSheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(manageKpiMonthMapDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经营管理月度实绩项目", manageKpiMonthMapDataByParams, fieldMap, response, strList, null);
    }

    /**
     * 返回附件上传页面
     *
     * @param model
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:performance:manager_kpi:manage-kpi-month-performance:index")
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
        params.put("noteEq", "经营管理月度实绩项目");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            System.out.println(importModules);
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经营管理月度实绩项目"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "/system/performance/manager_kpi/manage-kpi-month-performance/manage-kpi-month-performance-attachment-list";
    }

    /**
     * 附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表的实体类
     * @param request
     * @return 附件id
     */
    @RequiredLog("经营管理月度实绩项目指标附件上传")
    @PostMapping("uploadFile")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
        //判断是否选择对应公司、年份
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");
        String month = (String) params.get("month");
        if (ObjectUtils.isEmpty(params.get("companyName"))) {
            throw new ServiceException("未选择公司，导入失败");
        }
        if (ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("未选择年份，导入失败");
        }
        if (ObjectUtils.isEmpty(params.get("month"))) {
            throw new ServiceException("未选择月份，导入失败");
        }
        //保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        // 上传文件至数据库的类别，主要目的是分类展示
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);
        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = manageKpiMonthPerformanceService.readManageKpiMonthDataSource(result, companyName, year, month);
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