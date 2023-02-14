package com.fssy.shareholder.management.controller.system.performance.manager;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalStdService;
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
 * **数据表名：	bs_manager_qualitative_eval	**数据表中文名：	经理人绩效定性评价分数表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性评价分数 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-11-28
 */
@Controller
@RequestMapping("/system/performance/manager/general-manager-qualitative-eval")
public class ManagerQualitativeEvalController {

    @Autowired
    private ManagerQualitativeEvalService managerQualitativeEvalService;
    @Autowired
    private ManagerQualitativeEvalStdService managerQualitativeEvalStdService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private ImportModuleService importModuleService;
    @Autowired
    private CompanyService companyService;


    /**
     * 总经理定性评价管理界面
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager:general-manager-qualitative-eval:index1")
    @RequiredLog("总经理定性评价")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager/general-manager-qualitative-eval/general-manager-qualitative-eval-list";
    }

    /**
     * 返回 总经理定性评价管理界面 数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManagerQualitativeEvalDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerQualitativeEval> managerKpiScorePage = managerQualitativeEvalService.findManagerQualitativeEvalDataListPerPageByParams(params);
        if (managerKpiScorePage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerKpiScorePage.getTotal());
            result.put("data", managerKpiScorePage.getRecords());
        }
        return result;
    }

    /**
     * 导入
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("总经理定性评价附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,HttpServletRequest request) {

        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
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
            Map<String, Object> resultMap = managerQualitativeEvalService.readManagerQualitativeEvalDataSource(result,year);
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
     * 返回导入 附件上传页面
     * @param model model对象
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:performance:manager:general-manager-qualitative-eval:index")
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
        params.put("noteEq", "总经理定性评价");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "总经理定性评价表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/performance/manager/general-manager-qualitative-eval/general-manager-qualitative-eval-attachment-list";
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
        params.put("select","id,managerName,companyName,position,year,auditEvalScore,financialAuditScore," +
                "operationScore,leadershipScore,investScore,workReportScore,qualitativeEvalScoreAuto," +
                "qualitativeEvalScoreAdjust,adjustCause,status");
        //查询
        List<Map<String,Object>> managerQualitativeEvalDataByParams = managerQualitativeEvalService.findManagerQualitativeEvalDataByParams(params);
        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<>();

        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("managerName", "姓名");
        fieldMap.put("companyName", "所在企业");
        fieldMap.put("position", "所任职务");
        fieldMap.put("year", "年份");
        fieldMap.put("auditEvalScore", "审计评价");
        fieldMap.put("financialAuditScore", "财务稽核");
        fieldMap.put("operationScore", "运营管理");
        fieldMap.put("leadershipScore", "组织领导力");
        fieldMap.put("investScore", "投资管理");
        fieldMap.put("workReportScore", "述职报告");
        fieldMap.put("qualitativeEvalScoreAuto", "合计（自动）");
        fieldMap.put("qualitativeEvalScoreAdjust", "合计（人工）");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(managerQualitativeEvalDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("总经理定性评价", managerQualitativeEvalDataByParams, fieldMap, response, strList, null);
    }

    /**
     *  自动生成总经理定性评价分数
     * @param
     * @return 结果
     */
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request){
        Map<String, Object> params = getParams(request);
        if (ObjectUtils.isEmpty(params.get("year"))){
            throw new ServiceException("未选择年份，生成失败");
        }
        boolean result = managerQualitativeEvalService.createScore(params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
    }

    /**
     * 以主键删除总经理定性评价记录
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id) {
        boolean result = managerQualitativeEvalService.deleteManagerQualitativeEvalDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }

    /**
     * 修改总经理定性评价信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String managerName = request.getParameter("managerName");
        String companyName = request.getParameter("companyName");
        String year = request.getParameter("year");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("managerName", managerName);
        params.put("companyName", companyName);
        params.put("year", year);
        Map<String, Object> managerQualitativeEval = managerQualitativeEvalService.findManagerQualitativeEvalDataByParams(params).get(0);
        model.addAttribute("managerQualitativeEval", managerQualitativeEval);
        return "system/performance/manager/general-manager-qualitative-eval/general-manager-qualitative-eval-edit";
    }

    /**
     * 更新总经理定性评价信息
     * @param managerQualitativeEval
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerQualitativeEval managerQualitativeEval) {

        boolean result = managerQualitativeEvalService.updateManagerQualitativeEvalData(managerQualitativeEval);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "总经理定性评价信息没有更新，请检查数据后重新尝试");
    }

    /**
     * 返回查看指定经理人的下级分数构成数据页面<br/>
     * 添加根据给出姓名、公司名称、年份查看所有其对应的分数数据
     *
     * @param request 请求实体
     * @param model   model对象
     * @return 页面
     */
    @GetMapping("search-detail")
    public String searchByAssignFromBtn(HttpServletRequest request, Model model)
    {
        //②将获取到的姓名、公司名称、年份返回到前端弹出层页面
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String managerName = request.getParameter("managerName");
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("managerName", managerName);
        //在ManagerQualitativeEval中查找指标
        Map<String, Object> params = new HashMap<>();
        params.put("year",year);
        params.put("companyName",companyName);
        params.put("managerName",managerName);
        Map<String, Object> managerQualitativeEval = managerQualitativeEvalService.findManagerQualitativeEvalDataByParams(params).get(0);
        model.addAttribute("managerQualitativeEval",managerQualitativeEval);
        //对应分数下钻--获取值,用来计算原始数据
        double auditEvalScore = Double.parseDouble(managerQualitativeEval.get("auditEvalScore").toString());
        double financialAuditScore = Double.parseDouble(managerQualitativeEval.get("financialAuditScore").toString());
        double operationScore = Double.parseDouble(managerQualitativeEval.get("operationScore").toString());
        double leadershipScore = Double.parseDouble(managerQualitativeEval.get("leadershipScore").toString());
        double investScore = Double.parseDouble(managerQualitativeEval.get("investScore").toString());
        double workReportScore = Double.parseDouble(managerQualitativeEval.get("workReportScore").toString());
        //在ManagerQualitativeEvalStd中查找指标
        Map<String, Object> paramStd = new HashMap<>();
        paramStd.put("year",year);
        ManagerQualitativeEvalStd managerQualitativeEvalStd = managerQualitativeEvalStdService.findManagerQualitativeEvalStdDataByParams(paramStd).get(0);
        model.addAttribute("managerQualitativeEvalStd",managerQualitativeEvalStd);
        //对应分数下钻--获取相关比例系数,进行原来值的计算
        double auditEvalScoreR = managerQualitativeEvalStd.getAuditEvalScoreR();
        double financialAuditScoreR = managerQualitativeEvalStd.getFinancialAuditScoreR();
        double operationScoreR = managerQualitativeEvalStd.getOperationScoreR();
        double leadershipScoreR = managerQualitativeEvalStd.getLeadershipScoreR();
        double investScoreR = managerQualitativeEvalStd.getInvestScoreR();
        double workReportScoreR = managerQualitativeEvalStd.getWorkReportScoreR();
        model.addAttribute("auditEvalScoreR",auditEvalScoreR);
        model.addAttribute("financialAuditScoreR",financialAuditScoreR);
        model.addAttribute("operationScoreR",operationScoreR);
        model.addAttribute("leadershipScoreR",leadershipScoreR);
        model.addAttribute("investScoreR",investScoreR);
        model.addAttribute("workReportScoreR",workReportScoreR);
        //计算原来的值并将值传给前端
        double auditEvalScoreOld = auditEvalScore / auditEvalScoreR;
        double financialAuditScoreOld = financialAuditScore / financialAuditScoreR;
        double operationScoreOld = operationScore / operationScoreR;
        double leadershipScoreOld = leadershipScore / leadershipScoreR;
        double investScoreOld = investScore / investScoreR;
        double workReportScoreOld = workReportScore / workReportScoreR;
        model.addAttribute("auditEvalScoreOld",auditEvalScoreOld);
        model.addAttribute("financialAuditScoreOld",financialAuditScoreOld);
        model.addAttribute("operationScoreOld",operationScoreOld);
        model.addAttribute("leadershipScoreOld",leadershipScoreOld);
        model.addAttribute("investScoreOld",investScoreOld);
        model.addAttribute("workReportScoreOld",workReportScoreOld);
        return "system/performance/manager/general-manager-qualitative-eval/general-manager-qualitative-eval-detail";
    }

    /**
     * 获取数据库里的数据 展示数据
     * @param request
     * @return
     */
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("skillScore"))) {
            params.put("skillScore ", request.getParameter("skillScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("democraticEvalScore"))) {
            params.put("democraticEvalScore", request.getParameter("democraticEvalScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("superiorEvalScore"))) {
            params.put("superiorEvalScore", request.getParameter("superiorEvalScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("yearImportantEvents"))) {
            params.put("yearImportantEvents", request.getParameter("yearImportantEvents"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsTrackAnnual"))) {
            params.put("eventsTrackAnnual", request.getParameter("eventsTrackAnnual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsTrackSemiannual"))) {
            params.put("eventsTrackSemiannual", request.getParameter("eventsTrackSemiannual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditEvalScore"))) {
            params.put("auditEvalScore", request.getParameter("auditEvalScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("financialAuditScore"))) {
            params.put("financialAuditScore", request.getParameter("financialAuditScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("operationScore"))) {
            params.put("operationScore", request.getParameter("operationScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("leadershipScore"))) {
            params.put("leadershipScore", request.getParameter("leadershipScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("qualitativeEvalScoreAuto"))) {
            params.put("qualitativeEvalScoreAuto", request.getParameter("qualitativeEvalScoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investScore"))) {
            params.put("investScore", request.getParameter("investScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workReportScore"))) {
            params.put("workReportScore", request.getParameter("workReportScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("qualitativeEvalScoreAdjust"))) {
            params.put("qualitativeEvalScoreAdjust", request.getParameter("qualitativeEvalScoreAdjust"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("adjustCause"))) {
            params.put("adjustCause", request.getParameter("adjustCause"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("evalStdId"))) {
            params.put("evalStdId", request.getParameter("evalStdId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyIds"))) {
            params.put("companyIds", request.getParameter("companyIds"));
        }
        return params;
    }

}
