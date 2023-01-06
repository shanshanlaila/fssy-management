package com.fssy.shareholder.management.controller.system.operate.invest;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.InverstProjectPlanTraceOutputService;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectListService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectPlanTraceDetailService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectPlanTraceService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_list	**数据表中文名：	年度投资项目清单	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目清单	**创建人创建日期：	TerryZeng 2022-12-2 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Controller
@RequestMapping("/system/operate/invest/invest-project-year/")
public class InvestProjectListController {

    @Autowired
    private InvestProjectListService investProjectListService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private InvestProjectPlanTraceService investProjectPlanTraceService;

    @Autowired
    private InvestProjectPlanTraceDetailService investProjectPlanTraceDetailService;

    @Autowired
    private CompanyService companyService;


    /**
     * 年度投资项目清单管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("indexYear")
    @RequiresPermissions("system:operate:invest:invest-project-year:indexYear")
    @RequiredLog("年度投资项目清单管理")
    public String investProjectListIndex(Model model) {
        //1、查询公司列表，用于companyName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);

        return "system/operate/invest/invest-project-year/invest-project-list";
    }


    /**
     * 查询年度投资项目清单数据
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getInvestProjectListDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);

        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<Map<String, Object>> investProjectListDataListPerPageByParams = investProjectListService.findInvestProjectListDataMapListPerPageByParams(params);
        if (investProjectListDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", investProjectListDataListPerPageByParams.getTotal());
            result.put("data", investProjectListDataListPerPageByParams.getRecords());
        }
        return result;
    }


    /**
     * 以主键删除分数信息，后期开放
     *
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id, Map<String, Object> params) {
//        boolean result = investProjectListService.deleteInvestProjectListDataById(id);
//        if (result) {
//            return SysResult.ok();
//        }
//        return SysResult.build(500, "删除数据失败");
        return SysResult.build(500, "删除功能暂不开放");
    }


    /**
     * 返回查看项目跟踪计划表数据页面
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("search-detail")
    @RequiresPermissions("system:operate:invest:invest-project-year:indexYear")
    public String searchByAssignFromBtn(HttpServletRequest request, Model model) {
        String projectListId = request.getParameter("id");
        String companyName = request.getParameter("companyName");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String projectName = request.getParameter("projectName");
        model.addAttribute("month", month);
        model.addAttribute("projectName", projectName);
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("projectListId", projectListId);
        return "system/operate/invest/invest-project-year/invest-project-list-detail";
    }

    /**
     * 项目跟踪计划表数据
     *
     * @param request
     * @return
     */
    @GetMapping("getTraceObjects")
    @ResponseBody
    public Map<String, Object> getInvestProjectPlanTraceDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        String companyName = request.getParameter("companyName");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String projectName = request.getParameter("projectName");
        String projectListId = request.getParameter("projectListId");
        params.put("companyName", companyName);
        params.put("year", year);
        params.put("month", month);
        params.put("projectName", projectName);
        params.put("projectListId", projectListId);
        List<Map<String, Object>> investProjectPlanTracejectDataByParams = investProjectPlanTraceService.findInvestProjectPlanTracejectDataByParams(params);
        if (investProjectPlanTracejectDataByParams.size() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", investProjectPlanTracejectDataByParams.size());
            result.put("data", investProjectPlanTracejectDataByParams);
        }
        return result;
    }

    /**
     * 返回附件上传页面
     *
     * @param model
     * @return
     */
    @RequiredLog("附件上传")
    @GetMapping("index")
    @RequiresPermissions("system:operate:invest:invest-project-year:indexYear")
    public String materialDataAttachmentIndex(Model model, HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "项目进度计划跟踪表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "项目进度计划跟踪表"));
        }
        model.addAttribute("module", importModules.get(0).getId());

        String month = request.getParameter("month");
        String projectName = request.getParameter("projectName");
        String projectListId = request.getParameter("projectListId");
        model.addAttribute("month", month);
        model.addAttribute("projectName", projectName);
        model.addAttribute("projectListId", projectListId);
        return "system/operate/invest/invest-project-year/invest-project-year-attachment-list";
    }

    /**
     * 附件上传
     * 上传跟踪清单
     *
     * @param file
     * @param attachment
     * @return
     */
    @PostMapping("uploadFile")
    @RequiredLog("项目进度计划跟踪表附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");
        String month = (String) params.get("month");
        String projectName = (String) params.get("projectName");
        //String projectListId = (String) params.get("projectListId");
        String projectListId = request.getParameter("projectListId");
        if (org.springframework.util.ObjectUtils.isEmpty(params.get("companyName"))) {
            throw new ServiceException("未选择公司，导入失败");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("未选择年份，导入失败");
        }
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (org.springframework.util.ObjectUtils.isEmpty(module)) {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, // 上传文件至数据库的类别，主要目的是分类展示
                attachment);


        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = investProjectPlanTraceService.reaadInvestProjectPlanTraceDataSource(result, year, companyName, month, projectName, projectListId);
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
     * @param response 响应
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        Map<String, Object> paramsTwo = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");
        String month = (String) params.get("month");
        String projectName = (String) params.get("projectName");
        String projectListId = (String) params.get("projectListId");

        //Sql语句
        params.put("select", "projectListId,companyName,year,month,projectName,serial,projectPhase,projectContent,projectIndicators,projectTarget,feasibilityDate," +
                "contractDate,actualEndDate,inspectionDate,responsePerson,Inspectedby,inspectionResult,evaluate,note,abstracte,evaluateSum");
        paramsTwo.put("select", "companyName,year,month,projectName,evaluate,abstracte");
        //查询
        List<Map<String, Object>> investProjectPlanTracejectDataByParams = investProjectPlanTraceService.findInvestProjectPlanTracejectDataByParams(params);
        //List<Map<String, Object>> investProjectDataByParams = investProjectPlanTraceDetailService.findInvestProjectDataByParams(paramsTwo);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

        //需要改变背景的格子
        fieldMap.put("serial", "序号");
        fieldMap.put("projectPhase", "项目阶段");
        fieldMap.put("projectContent", "课题内容");
        fieldMap.put("projectIndicators", "具体事项指标");
        fieldMap.put("projectTarget", "项目目标");
        fieldMap.put("feasibilityDate", "可研报告计划完成时间");
        fieldMap.put("contractDate", "合同计划完成时间");
        fieldMap.put("actualEndDate", "实际完成时间");
        fieldMap.put("inspectionDate", "检查时间/频次");
        fieldMap.put("responsePerson", "负责人");
        fieldMap.put("Inspectedby", "检查人");
        fieldMap.put("inspectionResult", "检查结果");
        fieldMap.put("evaluate", "评价");
        fieldMap.put("note", "备注");


        for (Map<String, Object> investProjectPlanTracejectDataByParam : investProjectPlanTracejectDataByParams) {
            investProjectPlanTracejectDataByParam.put("abstracte", investProjectPlanTracejectDataByParams.get(0).get("abstracte"));
            investProjectPlanTracejectDataByParam.put("evaluateSum", investProjectPlanTracejectDataByParams.get(0).get("evaluateSum"));
        }
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
        SheetOutputService sheetOutputService = new InverstProjectPlanTraceOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(investProjectPlanTracejectDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("项目进度计划跟踪表", investProjectPlanTracejectDataByParams, fieldMap, response, strList, null);
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("projectName"))) {
            params.put("projectName", request.getParameter("projectName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyId"))) {
            params.put("companyId", request.getParameter("companyId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyShortName"))) {
            params.put("companyShortName", request.getParameter("companyShortName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectSource"))) {
            params.put("projectSource", request.getParameter("projectSource"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectClass"))) {
            params.put("projectClass", request.getParameter("projectClass"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectAbstract"))) {
            params.put("projectAbstract", request.getParameter("projectAbstract"));
        }

        /*if (!ObjectUtils.isEmpty(request.getParameter("investmentVolumePlan"))) {
            params.put("investmentVolumePlan", request.getParameter("investmentVolumePlan"));
        }*/
        //计划投资金额管理
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("investmentVolumePlanStart")))
        {
            params.put("investmentVolumePlanStart", request.getParameter("investmentVolumePlanStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("investmentVolumePlanEnd")))
        {
            params.put("investmentVolumePlanEnd", request.getParameter("investmentVolumePlanEnd"));
        }

        //实际投资金额管理
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("investmentVolumeActualStart")))
        {
            params.put("investmentVolumeActualStart", request.getParameter("investmentVolumeActualStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("investmentVolumeActualEnd")))
        {
            params.put("investmentVolumeActualEnd", request.getParameter("investmentVolumeActualEnd"));
        }


        if (!ObjectUtils.isEmpty(request.getParameter("projectContact"))) {
            params.put("projectContact", request.getParameter("projectContact"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("respManager"))) {
            params.put("respManager", request.getParameter("respManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectSrartDatePlan"))) {
            params.put("projectSrartDatePlan", request.getParameter("projectSrartDatePlan"));
        }
        //项目开始实际时间
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("projectEndDateActualStart")))
        {
            params.put("projectEndDateActualStart", request.getParameter("projectEndDateActualStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("projectEndDateActualEnd")))
        {
            params.put("projectEndDateActualEnd", request.getParameter("projectEndDateActualEnd"));
        }

        //项目结束实际时间
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("projectSrartDateActualStart")))
        {
            params.put("projectSrartDateActualStart", request.getParameter("projectSrartDateActualStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("projectSrartDateActualEnd")))
        {
            params.put("projectSrartDateActualEnd", request.getParameter("projectSrartDateActualEnd"));
        }



        if (!ObjectUtils.isEmpty(request.getParameter("projectEndDatePlan"))) {
            params.put("projectEndDatePlan", request.getParameter("projectEndDatePlan"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("currentProjectStatus"))) {
            params.put("currentProjectStatus", request.getParameter("currentProjectStatus"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("projectReportDate"))) {
            params.put("projectReportDate", request.getParameter("projectReportDate"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("investmentVolumeActual"))) {
            params.put("investmentVolumeActual", request.getParameter("investmentVolumeActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectContact"))) {
            params.put("projectContact", request.getParameter("projectContact"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("respManager"))) {
            params.put("respManager", request.getParameter("respManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectSrartDatePlan"))) {
            params.put("projectSrartDatePlan", request.getParameter("projectSrartDatePlan"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("projectSrartDateActual"))) {
            params.put("projectSrartDateActual", request.getParameter("projectSrartDateActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectEndDatePlan"))) {
            params.put("projectEndDatePlan", request.getParameter("projectEndDatePlan"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("currentProjectStatus"))) {
            params.put("currentProjectStatus", request.getParameter("currentProjectStatus"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("projectReportDate"))) {
            params.put("projectReportDate", request.getParameter("projectReportDate"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("projectFeasibilityStudyReport"))) {
            params.put("projectFeasibilityStudyReport", request.getParameter("projectFeasibilityStudyReport"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("committeeAuditStatus"))) {
            params.put("committeeAuditStatus", request.getParameter("committeeAuditStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("boardAuditStatus"))) {
            params.put("boardAuditStatus", request.getParameter("boardAuditStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectFinishStatus"))) {
            params.put("projectFinishStatus", request.getParameter("projectFinishStatus"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("projectRiskStatus"))) {
            params.put("projectRiskStatus", request.getParameter("projectRiskStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectEvalStatus"))) {
            params.put("projectEvalStatus", request.getParameter("projectEvalStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectFeasibilityStudyReportDate"))) {
            params.put("projectFeasibilityStudyReportDate", request.getParameter("projectFeasibilityStudyReportDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectFeasibilityStudyReportPlan"))) {
            params.put("projectFeasibilityStudyReportPlan", request.getParameter("projectFeasibilityStudyReportPlan"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("projectSelfEvalStatus"))) {
            params.put("projectSelfEvalStatus", request.getParameter("projectSelfEvalStatus"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("businessUnit"))) {
            params.put("businessUnit", request.getParameter("businessUnit"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("productLineName"))) {
            params.put("productLineName", request.getParameter("productLineName"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("productLineId"))) {
            params.put("productLineId", request.getParameter("productLineId"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }

        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("idDesc")))
        {
            params.put("idDesc", request.getParameter("idDesc"));
        }

        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("select")))
        {
            params.put("select", request.getParameter("select"));
        }

        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("groupBy")))
        {
            params.put("groupBy", request.getParameter("groupBy"));
        }

        return params;
    }

    /**
     * 展示修改页面
     *
     * @param id    年度投资项目清单id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        InvestProjectList investProjectList = investProjectListService.getById(id);
        model.addAttribute("investProjectList", investProjectList);//investProjectList传到前端
        //1、查询公司列表，用于companyName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<String> selectedList = new ArrayList<>();
        //业务判断，用于修改功能
        if (ObjectUtils.isEmpty(investProjectList.getCompanyId())){
            throw new ServiceException("企业ID为空");
        }
        selectedList.add(investProjectList.getCompanyId().toString());//xm-select默认选择控件
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, selectedList);
        model.addAttribute("companyNameList", companyNameList);
        return "system/operate/invest/invest-project-year/invest-project-list-edit";

    }
    /**
     * 更新年度投资项目清单
     *
     * @param investProjectList 年度投资项目清单
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(InvestProjectList investProjectList, HttpServletRequest request) {
        Map<String,Object> params=new HashMap<>();
        boolean result = investProjectListService.updateInvestProjectListData(investProjectList,request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "年度投资项目更新失败");
    }
    /**
     * 返回新增单条基础事件页面
     *
     * @param request
     * @return
     */
    @GetMapping("create")
    public String createInvestProjectList(HttpServletRequest request, Model model) {
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        String year = request.getParameter("year");
        String companyId = request.getParameter("companyId");
        String month = request.getParameter("month");
        String projectName = request.getParameter("projectName");
        model.addAttribute("year", year);
        model.addAttribute("companyId", companyId);
        model.addAttribute("month", month);
        model.addAttribute("projectName", projectName);
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/operate/invest/invest-project-year/invest-project-list-create";
    }

    /**
     * 保存InvestProjectList表
     *
     * @param investProjectList
     * @return
     */
    @PostMapping("store")
    @RequiredLog("保存新增单条基础事件")
    @ResponseBody
    public SysResult Store(InvestProjectList investProjectList, HttpServletRequest request) {
        boolean result = investProjectListService.insertInvestProjectList(investProjectList,request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "新增失败");
    }


    /**
     * 导出年度投资项目清单
     *
     * @param request  请求
     * @param response 响应
     */
    @RequiredLog("导出年度投资项目清单")
    @RequiresPermissions("system:operate:invest:invest-project-year:downloadInvestProjectList")
    @GetMapping("downloadInvestProjectList")
    public void downloadInvestProjectList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        params.put("select",
                "id," +
                        "year," +
                        "month,"+
                        "companyName," +
                        "projectName," +
                        "projectSource,projectType," +
                        "projectClass,projectAbstract,investmentVolumePlan,respManager," +
                        "projectContact,projectSrartDatePlan,projectEndDatePlan,businessUnit");
        List<Map<String, Object>> investProjectLists = investProjectListService.findInvestProjectDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
//        fieldMap.put("id", "年度投资项目清单序号");// A
        fieldMap.put("year", "年度");// B
        fieldMap.put("month", "月份");// C
        fieldMap.put("companyName", "企业名称");// C
        fieldMap.put("projectName", "项目名称");// D
        fieldMap.put("projectSource", "项目来源");// E
        fieldMap.put("projectType", "项目类别");//  F
        fieldMap.put("projectClass", "项目投资类型");//  G
        fieldMap.put("projectAbstract","项目简介");// H
        fieldMap.put("investmentVolumePlan", "计划投资金额"); // I
        fieldMap.put("respManager", "分管经理人"); // J
        fieldMap.put("projectContact", "项目联络人");// K
        fieldMap.put("projectSrartDatePlan", "计划项目开始时间");// L
        fieldMap.put("projectEndDatePlan", "计划项目结束时间");//M
        fieldMap.put("businessUnit", "投资所属实业部");//N
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(0,1, 2, 3, 4, 5,6, 7,8,9,10,11,12,13,14);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (org.springframework.util.ObjectUtils.isEmpty(investProjectLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("投资分析表", investProjectLists, fieldMap, response, strList, null);
    }

    @GetMapping("upload/{id}")
    @RequiresPermissions("system:operate:invest:invest-project-year:indexYear")
    @RequiredLog("年度投资项目附件上传页面")
    public String showUploadPage(@PathVariable String id, Model model) {
        InvestProjectList project = investProjectListService.getById(id);
        if (ObjectUtils.isEmpty(project)) {
            throw new ServiceException("不存在对应的项目，无法上传附件");
        } else {
            model.addAttribute("project", project);
        }
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "年度投资项目附件上传");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "项目月度进展表"));
        }
        model.addAttribute("module", importModules.get(0).getId());

        return "system/operate/invest/invest-project-year/invest-project-upload";
    }

    /**
     * 多附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFiles")
    @RequiredLog("年度投资项目附件上传")
    @ResponseBody
    public SysResult uploadFiles(@RequestParam("file") MultipartFile file, Attachment attachment,
                                 HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());// 设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService.findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);
        return SysResult.ok(result.getId());
    }

    /**
     * 提交年度投资项目附件上传
     *
     * @return 结果
     */
    @PostMapping("submitUploadFile")
    @ResponseBody
    @RequiredLog("提交年度投资项目附件上传")
    public SysResult submitUploadFile(InvestProjectList investProjectList,HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        String attachmentIds = request.getParameter("attachmentId");
        param.put("attachmentIds", attachmentIds);
        boolean result = investProjectListService.submitUploadFile(investProjectList,param);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败");
    }

    /**
     * 修改年度投资项目清单
     * @param request
     * @param model
     * @return
     */
    @GetMapping("editToProject")
    @RequiresPermissions("system:operate:invest:invest-project-year:indexYear")
    public String editToProject(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
//        String companyName = request.getParameter("companyName");
//        String month = request.getParameter("month");
//        String projectName = request.getParameter("projectName");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
//        params.put("companyName", companyName);
//        params.put("month", month);
//        params.put("projectName", projectName);
        InvestProjectList investProjectList = investProjectListService.findInvestProjectListDataByParams(params).get(0);
        model.addAttribute("investProjectList", investProjectList);
        return "system/operate/invest/invest-project-year/invest-project-list-project-edit";
    }

    /**
     * 修改年度投资项目清单项目
     * @param investProjectList
     * @return
     */
    @PostMapping("updateToProject")
    @ResponseBody
    public SysResult updateToProject(InvestProjectList investProjectList){
        Map<String, Object> params = new HashMap<>();
        boolean result = investProjectListService.updateInvestProjectListData(investProjectList);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "年度投资项目更新失败");
    }





}
