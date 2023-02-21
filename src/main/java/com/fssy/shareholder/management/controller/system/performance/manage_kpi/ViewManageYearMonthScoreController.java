package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManageYearMonthScore;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageMonthScoreSheetOutputService;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthAimService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManageYearMonthScoreService;
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
 * 计算经营管理月度分数视图 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-11-10
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/view-manage-month-score")
public class ViewManageYearMonthScoreController {
    @Autowired
    private ViewManageYearMonthScoreService viewManageYearMonthScoreService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;
    @Autowired
    private ManageKpiMonthAimService manageKpiMonthAimService;
    @Autowired
    private CompanyService companyService;
    /**
     * 计算经营管理月度分数视图 页面
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager_kpi:view-manage-month-score:index1")
    @RequiredLog("经营管理月度项目分数管理")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager_kpi/view-manage-month-score/view-manage-month-score-list";
    }

    /**
     * 返回 计算经营管理月度分数视图  表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManageKpiMonthScoreData(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        //获取前端年月查询
        String yearMonth = request.getParameter("yearMonth");
        params.put("yearMonth",yearMonth);
        //获取前端公司查询的主键
        Page<ViewManageYearMonthScore> scoreDataListPerPageByParams
                = viewManageYearMonthScoreService.findViewManageYearMonthScoreDataListPerPageByParams(params);
        if (scoreDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", scoreDataListPerPageByParams.getTotal());
            result.put("data", scoreDataListPerPageByParams.getRecords());
        }
        return result;
    }

    /**
     * 自动生成分数
     *
     * @param
     * @return 结果
     */
    @RequiredLog("自动生成分数")
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        boolean result = viewManageYearMonthScoreService.createScore(params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
    }

    /**
     * 附件上传页面
     *
     * @param model
     * @return 页面
     */
    @RequiredLog("附件上传")
    @GetMapping("index")
    @RequiresPermissions("system:performance:manager_kpi:view-manage-month-score:index")
    public String materialDataAttachmentIndex(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "激励约束项目评分表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "激励约束项目评分表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/performance/manager_kpi/view-manage-month-score/view-manage-month-score-attachment-list";
    }

    /**
     * 附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表的实体类
     * @param request
     * @return 附件id
     */
    @RequiredLog("激励约束项目评分表附件上传")
    @PostMapping("uploadFile")
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
            Map<String, Object> resultMap = viewManageYearMonthScoreService.readViewManageYearMonthScoreDataSource(result,request);
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
    /**
     * 激励约束项目评分表 导出
     *
     * @param request
     * @param response
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        //sql语句
        params.put("select", "id,companyName,projectType,projectDesc,unit,year,basicTarget,mustInputTarget," +
                "reachTarget,challengeTarget,month,monthTarget,monthActualValue,monthTarget,monthActualValue," +
                "accumulateTarget,accumulateActual,scoreAuto,scoreAdjust");
        //查询
        List<Map<String, Object>> scoreMapDataByParams = viewManageYearMonthScoreService.findViewManageYearMonthScoreMapDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("projectType", "指标类别");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("unit", "单位");
        fieldMap.put("basicTarget", "基本目标");
        fieldMap.put("mustInputTarget", "必达目标");
        fieldMap.put("reachTarget", "达标目标");
        fieldMap.put("challengeTarget", "挑战目标");
        fieldMap.put("monthTarget", "月份目标");
        fieldMap.put("monthActualValue", "月份实绩");
        fieldMap.put("accumulateTarget", "目标累计值");
        fieldMap.put("accumulateActual", "实绩累计值");
        fieldMap.put("scoreAdjust", "人工评分");


        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        SheetOutputService sheetOutputService = new ManageMonthScoreSheetOutputService();
        if (ObjectUtils.isEmpty(scoreMapDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("激励约束项目分数表", scoreMapDataByParams, fieldMap, response, strList, null);

    }

    /**
     * 修改分数信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        ManageKpiMonthAim manageKpiMonthAim = manageKpiMonthAimService.getById(id);
        model.addAttribute("map", manageKpiMonthAim);
        return "system/performance/manager_kpi/view-manage-month-score/view-manage-month-score-edit";
    }

    /**
     * 更新分数信息
     * @param manageKpiMonthAim
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManageKpiMonthAim manageKpiMonthAim) {

        boolean result = viewManageYearMonthScoreService.updateViewManageYearMonthScoreData(manageKpiMonthAim);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数信息没有更新，请检查数据后重新尝试");
    }
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("yearMonth"))) {
            params.put("yearMonth", request.getParameter("yearMonth"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyIds"))) {
            params.put("companyIds", request.getParameter("companyIds"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAdjustCause"))) {
            params.put("scoreAdjustCause", request.getParameter("scoreAdjustCause"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("managerKpiMark"))) {
            params.put("managerKpiMark", request.getParameter("managerKpiMark"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkCompany"))) {
            params.put("benchmarkCompany", request.getParameter("benchmarkCompany"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkValue"))) {
            params.put("benchmarkValue", request.getParameter("benchmarkValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monitorDepartment"))) {
            params.put("monitorDepartment", request.getParameter("monitorDepartment"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monitorUser"))) {
            params.put("monitorUser", request.getParameter("monitorUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("proportion"))) {
            params.put("proportion", request.getParameter("proportion"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastOneYearActual"))) {
            params.put("pastOneYearActual", request.getParameter("pastOneYearActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastTwoYearsActual"))) {
            params.put("pastTwoYearsActual", request.getParameter("pastTwoYearsActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastThreeYearsActual"))) {
            params.put("pastThreeYearsActual", request.getParameter("pastThreeYearsActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("setPolicy"))) {
            params.put("setPolicy", request.getParameter("setPolicy"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("source"))) {
            params.put("source", request.getParameter("source"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget"))) {
            params.put("monthTarget", request.getParameter("monthTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActualValue"))) {
            params.put("monthActualValue", request.getParameter("monthActualValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthAnalyzeRes"))) {
            params.put("monthAnalyzeRes", request.getParameter("monthAnalyzeRes"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateTarget"))) {
            params.put("accumulateTarget", request.getParameter("accumulateTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateActual"))) {
            params.put("accumulateActual", request.getParameter("accumulateActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("analyzeDesc"))) {
            params.put("analyzeDesc", request.getParameter("analyzeDesc"));
        }
        if(!ObjectUtils.isEmpty(request.getParameter("analyzeRes"))){
            params.put("analyzeRes",request.getParameter("analyzeRes"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAuto"))) {
            params.put("scoreAuto", request.getParameter("scoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAdjust"))) {
            params.put("scoreAdjust", request.getParameter("scoreAdjust"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("evaluateMode"))){
            params.put("evaluateMode",request.getParameter("evaluateMode"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("targetLevelActual"))){
            params.put("targetLevelActual",request.getParameter("targetLevelActual"));
        }
        return params;


    }
}
