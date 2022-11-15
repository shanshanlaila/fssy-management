package com.fssy.shareholder.management.controller.system.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthAimMapper;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageKpiYearSheetOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthAimService;
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
 * 经营管理月度指标数据表 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-10-24
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/manage-kpi-month-aim/")
public class ManageKpiMonthAimController {

    @Autowired
    private ManageKpiMonthAimService manageKpiMonthAimService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private ManageKpiMonthAimMapper manageKpiMonthAimMapper;

    /**
     * 经营管理月度项目指标 管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager_kpi:manage-kpi-month-aim:index1")
    @RequiredLog("经营管理月度项目指标管理")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/manage-kpi-month-aim/manage-kpi-month-aim-list";
    }

    /**
     * 返回经营管理月度项目指标数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManageKpiYearDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<Map<String,Object>> manageKpiMonthPage = manageKpiMonthAimService.findManageKpiMonthDataMapListPerPageByParams(params);
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
     * 附件上传页面
     *
     * @param model
     * @return 页面
     */
    @RequiredLog("附件上传")
    @GetMapping("index")
    @RequiresPermissions("system:performance:manager_kpi:manage-kpi-month-aim:index")
    public String materialDataAttachmentIndex(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "经营管理月度目标数据");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经营管理月度目标数据"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "/system/performance/manager_kpi/manage-kpi-month-aim/manage-kpi-month-aim-attachment-list";
    }

    /**
     * 附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表的实体类
     * @param request
     * @return 附件id
     */
    @RequiredLog("经营管理月度目标附件上传")
    @PostMapping("uploadFile")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");
        if (ObjectUtils.isEmpty(params.get("companyName"))) {
            throw new ServiceException("未选择公司，导入失败");
        }
        if (ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("未选择年份，导入失败");
        }
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
            Map<String, Object> resultMap = manageKpiMonthAimService.readManageKpiMonthDataSource(result,companyName,year);
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
     * 导出
     *
     * @param request
     * @param response
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        QueryWrapper<ManageKpiMonthAim> manageKpiMonthAimQueryWrapper = new QueryWrapper<>();
        int month = 1;
        // sql字符串
        StringBuilder selectStr1 = new StringBuilder("id,companyName,projectType,projectDesc,unit,benchmarkCompany," +
                "benchmarkValue,monitorDepartment,monitorUser,year,month,basicTarget,mustInputTarget,reachTarget,dataSource," +
                "challengeTarget,proportion,pastOneYearActual,pastTwoYearsActual,pastThreeYearsActual,kpiDefinition,kpiDecomposeMode,evaluateMode,performanceMark");
        do
        //12个月目标值的循环
        {
            selectStr1.append(", sum(if(MONTH =" +  month + ",monthTarget,0)) AS 'month" + month + "'");
            month++;
        } while (month <= 12);
        //querywrapper查询，分组
        manageKpiMonthAimQueryWrapper.select(selectStr1.toString()).eq("companyName",request.getParameter("companyName"))
                .eq("year",request.getParameter("year"))
                .groupBy("manageKpiYearId");
        List<Map<String, Object>> manageKpiMonthAims = manageKpiMonthAimMapper.selectMaps(manageKpiMonthAimQueryWrapper);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("projectType", "指标类别");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("kpiDefinition","管理项目定义（公式）");
        fieldMap.put("unit", "单位");
        fieldMap.put("dataSource", "数据来源部门");
        fieldMap.put("benchmarkCompany", "对标标杆公司名称");
        fieldMap.put("benchmarkValue", "标杆值");
        fieldMap.put("pastOneYearActual", "过去第一年值");
        fieldMap.put("pastTwoYearsActual", "过去第二年值");
        fieldMap.put("pastThreeYearsActual", "过去第三年值");
        fieldMap.put("basicTarget", "基本目标");
        fieldMap.put("mustInputTarget", "必达目标");
        fieldMap.put("reachTarget", "达标目标");
        fieldMap.put("challengeTarget", "挑战目标");
        fieldMap.put("performanceMark", "经理人绩效指标标识");
        fieldMap.put("evaluateMode","评分模式");
        fieldMap.put("month1", "目标值");
        fieldMap.put("month2", "目标值");
        fieldMap.put("month3", "目标值");;
        fieldMap.put("month4", "目标值");
        fieldMap.put("month5", "目标值");;
        fieldMap.put("month6", "目标值");
        fieldMap.put("month7", "目标值");
        fieldMap.put("month8", "目标值");
        fieldMap.put("month9", "目标值");
        fieldMap.put("month10", "目标值");
        fieldMap.put("month11", "目标值");
        fieldMap.put("month12", "目标值");
        fieldMap.put("monitorDepartment", "监控部门名称");
        fieldMap.put("monitorUser", "监控人姓名");



        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25,26,27,28,29);
        SheetOutputService sheetOutputService = new ManageKpiYearSheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(manageKpiMonthAims)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经营管理月度目标指标数据报表", manageKpiMonthAims, fieldMap, response, strList, null);
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
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
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectDesc"))) {
            params.put("projectDesc", request.getParameter("projectDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))){
            params.put("manageKpiYearId",request.getParameter("manageKpiYearId"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("evaluateMode"))) {
            params.put("evaluateMode", request.getParameter("evaluateMode"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateTarget"))) {
            params.put("accumulateTarget", request.getParameter("accumulateTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateActual"))) {
            params.put("accumulateActual", request.getParameter("accumulateActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("analyzeDesc"))) {
            params.put("analyzeDesc", request.getParameter("analyzeDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month1"))) {
            params.put("month1", request.getParameter("month1"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month2"))) {
            params.put("month2", request.getParameter("month2"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month3"))) {
            params.put("month3", request.getParameter("month3"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month4"))) {
            params.put("month4", request.getParameter("month4"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month5"))) {
            params.put("month5", request.getParameter("month5"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month6"))) {
            params.put("month6", request.getParameter("month6"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month7"))) {
            params.put("month7", request.getParameter("month7"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month8"))) {
            params.put("month8", request.getParameter("month8"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month9"))) {
            params.put("month9", request.getParameter("month9"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month10"))) {
            params.put("month10", request.getParameter("month10"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month11"))) {
            params.put("month11", request.getParameter("month11"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month12"))) {
            params.put("month12", request.getParameter("month12"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("performanceMark"))) {
            params.put("performanceMark", request.getParameter("performanceMark"));
        }
        return params;


    }
}
