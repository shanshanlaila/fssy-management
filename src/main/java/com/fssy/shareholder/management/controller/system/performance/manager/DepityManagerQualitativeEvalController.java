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
import com.fssy.shareholder.management.service.system.performance.manager.DepityManagerQualitativeEvalService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalStdService;
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
 * **???????????????	bs_manager_qualitative_eval	**?????????????????????	????????????????????????????????????	**???????????????	???????????????	**??????????????????	??????????????????????????????????????????????????????????????? ???????????????
 * </p>
 *
 * @author zzp
 * @since 2022-11-28
 */
@Controller
@RequestMapping("/system/performance/manager/depity-manager-qualitative-eval")
public class DepityManagerQualitativeEvalController {

    @Autowired
    private DepityManagerQualitativeEvalService depityManagerQualitativeEvalService;
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
     * ????????????????????????????????????
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager:depity-manager-qualitative-eval:index1")
    @RequiredLog("????????????????????????")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/performance/manager/depity-manager-qualitative-eval/depity-manager-qualitative-eval-list";
    }

    /**
     * ?????? ???????????????????????????????????? ????????????
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
        Page<ManagerQualitativeEval> managerKpiScorePage = depityManagerQualitativeEvalService.findManagerQualitativeEvalDataListPerPageByParams(params);
        if (managerKpiScorePage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "???????????????");
        } else {
            result.put("code", 0);
            result.put("count", managerKpiScorePage.getTotal());
            result.put("data", managerKpiScorePage.getRecords());
        }
        return result;
    }

    /**
     * ??????
     *
     * @param file       ???????????????????????????
     * @param attachment ??????????????????
     * @return ??????ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("????????????????????????????????????")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {

        //?????????????????????????????????
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        if (org.springframework.util.ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("??????????????????????????????");
        }
        // ????????????
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());//????????????
        // ????????????????????????
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (org.springframework.util.ObjectUtils.isEmpty(module)) {
            throw new ServiceException(
                    String.format("????????????%s?????????????????????????????????????????????", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, // ???????????????????????????????????????????????????????????????
                attachment);
        try {
            // ???????????????????????????
            Map<String, Object> resultMap = depityManagerQualitativeEvalService.readManagerQualitativeEvalDataSource(result, year);
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {// "failed" : true
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                        result.getId().toString(), String.valueOf(resultMap.get("content")));
                return SysResult.build(200, "?????????????????????????????????????????????????????????????????????????????????????????????????????????");
            } else {
                // ???????????????????????????
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), "????????????");// ????????????????????????
                return SysResult.ok();
            }
        } catch (ServiceException e) {
            // ????????????????????????????????????
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED, result.getId().toString(), e.getMessage());
            throw new ServiceException("???????????????????????????????????????????????????????????????????????????????????????????????????");
        } catch (Exception e) {
            // ?????????????????????????????????
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * ???????????? ??????????????????
     *
     * @param model model??????
     * @return ??????
     */
    @RequiredLog("????????????")
    @RequiresPermissions("system:performance:manager:depity-manager-qualitative-eval:index")
    @GetMapping("index")
    public String materialDataAttachmentIndex(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // ??????????????????
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "????????????????????????");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("????????????%s?????????????????????????????????????????????", "???????????????????????????"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/performance/manager/depity-manager-qualitative-eval/depity-manager-qualitative-eval-attachment-list";
    }

    /**
     * excel ??????
     *
     * @param request  ??????
     * @param response ??????
     */
    @RequiredLog("????????????")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        //Sql??????
        params.put("select", "id,managerName,companyName,position,year,skillScore,democraticEvalScore," +
                "superiorEvalScore,qualitativeEvalScoreAuto,qualitativeEvalScoreAdjust,adjustCause,yearImportantEvents," +
                "eventsTrackSemiannual,eventsTrackAnnual,status");
        //??????
        List<Map<String, Object>> managerQualitativeEvalDataByParams = depityManagerQualitativeEvalService.findManagerQualitativeEvalDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

        //???????????????????????????
        fieldMap.put("id", "??????");
        fieldMap.put("managerName", "??????");
        fieldMap.put("companyName", "????????????");
        fieldMap.put("position", "????????????");
        fieldMap.put("year", "??????");
        fieldMap.put("skillScore", "??????");
        fieldMap.put("democraticEvalScore", "????????????");
        fieldMap.put("superiorEvalScore", "????????????");
        fieldMap.put("qualitativeEvalScoreAuto", "??????????????????(??????)");
        fieldMap.put("qualitativeEvalScoreAdjust", "??????????????????(??????)");
        fieldMap.put("adjustCause", "????????????");
        fieldMap.put("yearImportantEvents", "????????????????????????");
        fieldMap.put("eventsTrackSemiannual", "?????????");
        fieldMap.put("eventsTrackAnnual", "??????");
        //?????????????????????
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(managerQualitativeEvalDataByParams)) {
            throw new ServiceException("???????????????");
        }
        sheetOutputService.exportNum("????????????????????????", managerQualitativeEvalDataByParams, fieldMap, response, strList, null);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param
     * @return ??????
     */
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        if (ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("??????????????????????????????");
        }
        boolean result = depityManagerQualitativeEvalService.createScore(params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "??????????????????");
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id) {
        boolean result = depityManagerQualitativeEvalService.deleteManagerQualitativeEvalDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "??????????????????");
    }

    /**
     * ????????????????????????????????????
     *
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
        Map<String, Object> managerQualitativeEval = depityManagerQualitativeEvalService.findManagerQualitativeEvalDataByParams(params).get(0);
        model.addAttribute("managerQualitativeEval", managerQualitativeEval);
        return "system/performance/manager/depity-manager-qualitative-eval/depity-manager-qualitative-eval-edit";
    }

    /**
     * ????????????????????????????????????
     *
     * @param managerQualitativeEval
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerQualitativeEval managerQualitativeEval) {

        boolean result = depityManagerQualitativeEvalService.updateManagerQualitativeEvalData(managerQualitativeEval);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "???????????????????????????????????????????????????????????????????????????");
    }

    /**
     * ????????????????????????????????????????????????????????????<br/>
     * ????????????????????????????????????????????????????????????????????????????????????
     *
     * @param request ????????????
     * @param model   model??????
     * @return ??????
     */
    @GetMapping("search-detail")
    public String searchByAssignFromBtn(HttpServletRequest request, Model model) {
        //??????????????????????????????????????????????????????????????????????????????
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String managerName = request.getParameter("managerName");
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("managerName", managerName);
        //???ManagerQualitativeEval???????????????
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("companyName", companyName);
        params.put("managerName", managerName);
        Map<String, Object> managerQualitativeEval = depityManagerQualitativeEvalService.findManagerQualitativeEvalDataByParams(params).get(0);
        model.addAttribute("managerQualitativeEval", managerQualitativeEval);
        //??????????????????--?????????,????????????????????????
        double skillScore = Double.parseDouble(managerQualitativeEval.get("skillScore").toString());
        double democraticEvalScore = Double.parseDouble(managerQualitativeEval.get("democraticEvalScore").toString());
        double superiorEvalScore = Double.parseDouble(managerQualitativeEval.get("superiorEvalScore").toString());
        //???ManagerQualitativeEvalStd???????????????
        Map<String, Object> paramStd = new HashMap<>();
        paramStd.put("year", year);
        ManagerQualitativeEvalStd managerQualitativeEvalStd = managerQualitativeEvalStdService.findManagerQualitativeEvalStdDataByParams(paramStd).get(0);
        model.addAttribute("managerQualitativeEvalStd", managerQualitativeEvalStd);
        //??????????????????--????????????????????????,????????????????????????
        double skillScoreR = managerQualitativeEvalStd.getSkillScoreR();
        double democraticEvalScoreR = managerQualitativeEvalStd.getDemocraticEvalScoreR();
        double superiorEvalScoreR = managerQualitativeEvalStd.getSuperiorEvalScoreR();
        model.addAttribute("skillScoreR", skillScoreR);
        model.addAttribute("democraticEvalScoreR", democraticEvalScoreR);
        model.addAttribute("superiorEvalScoreR", superiorEvalScoreR);
        //???????????????????????????????????????
        double skillScoreOld = skillScore / skillScoreR;
        double democraticEvalScoreOld = democraticEvalScore / democraticEvalScoreR;
        double superiorEvalScoreOld = superiorEvalScore / superiorEvalScoreR;
        model.addAttribute("skillScoreOld", skillScoreOld);
        model.addAttribute("democraticEvalScoreOld", democraticEvalScoreOld);
        model.addAttribute("superiorEvalScoreOld", superiorEvalScoreOld);
        return "system/performance/manager/depity-manager-qualitative-eval/depity-manager-qualitative-eval-detail";
    }

    /**
     * ??????????????????????????? ????????????
     *
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
