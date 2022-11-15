package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiMonth;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiMonthService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * VIEW 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/view-manager-kpi-month-score")
public class ViewManagerKpiMonthController {

    @Autowired
    private ViewManagerKpiMonthService viewManagerKpiMonthService;
    @Autowired
    private ManagerKpiScoreServiceOld managerKpiScoreService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 返回附件上传页面
     *
     * @param model model对象
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:performance:manager_kpi:view-manager-kpi-month-score:index")
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
        params.put("noteEq", "经理人KPI分数表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经理人KPI分数表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "/system/performance/manager_kpi/view-manager-kpi-month-score/view-manager-kpi-month-score-attachment-list";
    }

    /**
     * 附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("经理人KPI分数附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment) {
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
            Map<String, Object> resultMap = managerKpiScoreService.readManagerKpiScoreOldDataSource(result);
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
     * 经理人月度KPI分数管理界面
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager_kpi:view-manager-kpi-month-score:index1")
    @RequiredLog("经理人月度KPI分数")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/view-manager-kpi-month-score/view-manager-kpi-month-score-list";
    }

    /**
     * 返回 经理人月度KPI分数数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getViewManagerKpiMonthDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerKpiScoreOld> managerKpiScorePage = managerKpiScoreService.findViewManagerKpiMonthDataListPerPageByParams(params);
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
     *  自动生成分数
     * @param
     * @return 结果
     */
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request){
        Map<String, Object> params = getParams(request);

        if (ObjectUtils.isEmpty(params.get("companyName"))){
            throw new ServiceException("未填写企业名称，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("year"))){
            throw new ServiceException("未选择年份，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("month"))){
            throw new ServiceException("未选择年份，生成失败");
        }
        boolean result = viewManagerKpiMonthService.createScore(params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
    }

    /**
     * 以主键删除分数信息
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id) {
        boolean result = managerKpiScoreService.deleteManagerKpiScoreOldDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
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
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Map<String, Object> managerKpiScoreOld = managerKpiScoreService.findManagerKpiScoreOldDataByParams(params).get(0);
        model.addAttribute("managerKpiScoreOld", managerKpiScoreOld);
        return "/system/performance/manager_kpi/view-manager-kpi-month-score/view-manager-kpi-month-score-edit";
    }

    /**
     * 更新分数信息
     * @param managerKpiScoreOld
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerKpiScoreOld managerKpiScoreOld) {

        boolean result = managerKpiScoreService.updateManagerKpiScoreOldData(managerKpiScoreOld);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数信息没有更新，请检查数据后重新尝试");
    }


    /**
     * 返回查看指定经理人的下级分数构成数据页面<br/>
     * 添加根据给出姓名、公司名称、年份、月份查看所有其对应的分数数据
     *
     * @param request 请求实体
     * @param model   model对象
     * @return 页面
     */
    @GetMapping("search-detail")
    public String searchByAssignFromBtn(HttpServletRequest request, Model model)
    {
        //②将获取到的id返回到前端弹出层页面
        String id = request.getParameter("id");
        String month = request.getParameter("month");
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String managerName = request.getParameter("managerName");
        model.addAttribute("id", id);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("managerName", managerName);
        return "/system/performance/manager_kpi/view-manager-kpi-month-score/view-manager-kpi-month-score-detail";
    }

    /**
     * 返回指定经理人分数明细表格数据
     */
    @GetMapping("getManagerScoreData")
    @ResponseBody
    public Map<String, Object> getManagerScoreData(HttpServletRequest request)
    {
        //④将传进来的参数交给数据库查，然后返回页面
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<>();
        String managerScoreDataId = request.getParameter("id");
        System.out.println("managerScoreDataId = " + managerScoreDataId);
        String managerScoreDataMonth = request.getParameter("month");
        String managerScoreDataYear = request.getParameter("year");
        String managerScoreDataCompanyName = request.getParameter("companyName");
        String managerScoreDataManagerName = request.getParameter("managerName");
        params.put("id",managerScoreDataId);
        params.put("month",managerScoreDataMonth);
        params.put("year",managerScoreDataYear);
        params.put("companyName",managerScoreDataCompanyName);
        params.put("managerName",managerScoreDataManagerName);
        List<Map<String, Object>> managerScoreDataIdList = viewManagerKpiMonthService
                .findViewManagerKpiMonthMapDataByParams(params);
        System.out.println(managerScoreDataIdList);

        if (managerScoreDataIdList.size() == 0)
        {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        }
        else
        {
            result.put("code", 0);
            result.put("count", managerScoreDataIdList.size());
            result.put("data", managerScoreDataIdList);
        }
        return result;
    }



    //获取数据库里的数据,展示数据
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("proportion"))) {
            params.put("proportion", request.getParameter("proportion"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthScoreAuto"))) {
            params.put("monthScoreAuto", request.getParameter("monthScoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthScoreAdjust"))) {
            params.put("monthScoreAdjust", request.getParameter("monthScoreAdjust"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAdjust"))) {
            params.put("scoreAdjust", request.getParameter("scoreAdjust"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiFormula"))) {
            params.put("kpiFormula", request.getParameter("kpiFormula"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAuto"))) {
            params.put("scoreAuto", request.getParameter("scoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("businessScore"))) {
            params.put("businessScore", request.getParameter("businessScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("incentiveScore"))) {
            params.put("incentiveScore", request.getParameter("incentiveScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("difficultyCoefficient"))) {
            params.put("difficultyCoefficient", request.getParameter("difficultyCoefficient"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("generalManagerScore"))) {
            params.put("generalManagerScore", request.getParameter("generalManagerScore"));
        }
        return params;
    }
}
