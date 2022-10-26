package com.fssy.shareholder.management.controller.system.manage;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiMonthAim;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageKpiMonthAnalyzeSheetOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.manage.ManageKpiMonthAnalyzeService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 经营管理月度风险分析 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-10-24
 */
@Controller
@RequestMapping("/system/manager/manage-kpi-month-analyze/")
public class ManageKpiMonthAnalyzeController {

    @Autowired
    private ManageKpiMonthAnalyzeService manageKpiMonthAnalyzeService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 经营管理月度风险分析管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/manager/manage-kpi-month-analyze/manage-kpi-month-analyze-list";
    }

    /**
     * 返回经营管理月度风险分析数据
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
        Page<ManageKpiMonthAim> manageKpiMonthPage = manageKpiMonthAnalyzeService.findManageKpiMonthDataListPerPageByParams(params);
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
    public String materialDataAttachmentIndex(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "经营管理月度分析数据");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "经营管理月度分析数据"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/manager/manage-kpi-month-analyze/manage-kpi-month-analyze-attachment-list";
    }

    /**
     * 附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表的实体类
     * @param request
     * @return 附件id
     */
    @RequiredLog("经营管理项目指标附件上传")
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
            Map<String, Object> resultMap = manageKpiMonthAnalyzeService.readManageKpiMonthDataSource(result);
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
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        //内循环准备，决定列的数据
        Map<String, Object> params = getParams(request);
        //外循环数据准备，决定行的数据
        Map<String, Object> outParams = getParams(request);
        //以外循环，年、项目名称、公司名称为行，内循环以月展开每行数据，月为列
        outParams.put("select", "year,companyName,projectDesc");
        outParams.put("yearIsNotNull", true);
        outParams.put("group", "year,companyName,projectDesc");
        //查询外循环数据
        List<Map<String, Object>> outList = manageKpiMonthAnalyzeService.findManageKpiMonthMapDataByParams(outParams);
//        for (Map<String, Object> temp : outList) {
//            System.out.println(temp);//{year=2022, companyName=方盛车桥（柳州）有限公司, projectDesc=主营业务收入}
//            System.out.println(temp.get("year"));
//        }
        //sql语句
        params.put("select", "id,companyName,status,projectType,projectDesc,unit,dataSource,benchmarkCompany," +
                "benchmarkValue,monitorDepartment,monitorUser,year,basicTarget,mustInputTarget,reachTarget,challengeTarget," +
                "proportion,pastOneYearActual,pastTwoYearsActual,pastThreeYearsActual,setPolicy,source," +
                "monthTarget,monthActualValue,accumulateTarget,accumulateActual,analyzeDesc,analyzeRes");
        //查询内循环数据
        List<Map<String, Object>> manageKpiMonthMapDataByParams = manageKpiMonthAnalyzeService.findManageKpiMonthMapDataByParams(params);
        ArrayList<Map<String, Object>> objects = new ArrayList<>();  //一年的数据数据
        Map<String, Object> objectObjectHashMap = new HashMap<>();   //单月的数据
        System.out.println("外循環數據" + outList);
        System.out.println("内循環數據 = " + manageKpiMonthMapDataByParams);
        int currentMonth = 0;
        for (Map<String, Object> temp : outList) {
//            System.out.println(temp);// {year=2022, companyName=方盛车桥（柳州）有限公司, projectDesc=经营性利润}
            Integer outYear = (Integer) temp.get("year");// 外循环年
            String outCompanyName = (String) temp.get("companyName");// 外循环公司名称
            String outProjectDesc = (String) temp.get("projectDesc");// 外循环项目名称
//            manageKpiMonthMapDataByParams.get(currentMonth).get("year").equals(outYear);
            //内循环、列
            for (int i = 1; i <= 12; i++) {

                System.out.println(i);
                //判断一月份
                //判断内外循环关联，以年、项目名称、公司名称，按一月、二月的顺序放入
                Integer innerYear = (Integer) manageKpiMonthMapDataByParams.get(i).get("year");// 内循环年
                String innerCompanyName = (String) manageKpiMonthMapDataByParams.get(i).get("companyName");// 内循环公司名称
                String innerProjectDesc = (String) manageKpiMonthMapDataByParams.get(i).get("projectDesc");// 内循环公司名称
                if (Objects.equals(innerYear, outYear) && innerCompanyName.equals(outCompanyName) && innerProjectDesc.equals(outProjectDesc)) {
                    if (i == 1) {

                        objectObjectHashMap.put("目标值1", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值1", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份1", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 2) {
                        objectObjectHashMap.put("目标值2", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值2", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份2", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 3) {
                        objectObjectHashMap.put("目标值3", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值3", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份3", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 4) {
                        objectObjectHashMap.put("目标值4", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值4", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份4", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 5) {
                        objectObjectHashMap.put("目标值5", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值5", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份5", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 6) {
                        objectObjectHashMap.put("目标值6", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值6", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份6", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 7) {
                        objectObjectHashMap.put("目标值7", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值7", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份7", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 8) {
                        objectObjectHashMap.put("目标值8", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值8", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份8", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 9) {
                        objectObjectHashMap.put("目标值9", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值9", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份9", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 10) {
                        objectObjectHashMap.put("目标值10", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值10", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份10", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 11) {
                        objectObjectHashMap.put("目标值11", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值11", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份11", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                    if (i == 12) {
                        objectObjectHashMap.put("目标值12", manageKpiMonthMapDataByParams.get(i).get("monthTarget"));
                        objectObjectHashMap.put("实绩值12", manageKpiMonthMapDataByParams.get(i).get("monthActualValue"));
                        objectObjectHashMap.put("年份12", manageKpiMonthMapDataByParams.get(i).get("year"));
                    }
                }
            }
            currentMonth++;
            objects.add(objectObjectHashMap);
        }
        for (Map<String, Object> innerTemp : objects) {
            System.out.println(innerTemp);

        }
        System.out.println("*********************************");
        for (Map<String, Object> monthTemp : outList) {
            System.out.println(monthTemp);

        }

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("companyName", "企业名称");
        fieldMap.put("status", "项目状态");
        fieldMap.put("projectType", "指标类别");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("unit", "单位");
        fieldMap.put("dataSource", "数据来源部门");
        fieldMap.put("benchmarkCompany", "对标标杆公司名称");
        fieldMap.put("benchmarkValue", "标杆值");
        fieldMap.put("monitorDepartment", "监控部门名称");
        fieldMap.put("monitorUser", "监控人姓名");
        fieldMap.put("year", "年份");
        fieldMap.put("basicTarget", "基本目标");
        fieldMap.put("mustInputTarget", "必达目标");
        fieldMap.put("reachTarget", "达标目标");
        fieldMap.put("challengeTarget", "挑战目标");
        fieldMap.put("proportion", "权重");
        fieldMap.put("pastOneYearActual", "过去第一年值");
        fieldMap.put("pastTwoYearsActual", "过去第二年值");
        fieldMap.put("pastThreeYearsActual", "过去第三年值");
        fieldMap.put("setPolicy", "选取原则");
        fieldMap.put("source", "KPI来源");
        fieldMap.put("accumulateTarget", "本年同期目标累计值");
        fieldMap.put("accumulateActual", "本年同期实际累计值");
        fieldMap.put("目标值1", "目标值");
        fieldMap.put("实绩值1", "实绩值");
        fieldMap.put("目标值2", "目标值");
        fieldMap.put("实绩值2", "实绩值");
        fieldMap.put("目标值3", "目标值");
        fieldMap.put("实绩值3", "实绩值");
        fieldMap.put("目标值4", "目标值");
        fieldMap.put("实绩值4", "实绩值");
        fieldMap.put("目标值5", "目标值");
        fieldMap.put("实绩值5", "实绩值");
        fieldMap.put("目标值6", "目标值");
        fieldMap.put("实绩值6", "实绩值");
        fieldMap.put("目标值7", "目标值");
        fieldMap.put("实绩值7", "实绩值");
        fieldMap.put("目标值8", "目标值");
        fieldMap.put("实绩值8", "实绩值");
        fieldMap.put("目标值9", "目标值");
        fieldMap.put("实绩值9", "实绩值");
        fieldMap.put("目标值10", "目标值");
        fieldMap.put("实绩值10", "实绩值");
        fieldMap.put("目标值11", "目标值");
        fieldMap.put("实绩值11", "实绩值");
        fieldMap.put("目标值12", "目标值");
        fieldMap.put("实绩值12", "实绩值");
        fieldMap.put("analyzeDesc", "企业分析");
        fieldMap.put("岗位分析", "岗位分析");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
                27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49
        );
        SheetOutputService sheetOutputService = new ManageKpiMonthAnalyzeSheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(manageKpiMonthMapDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经营管理月度风险分析报表", manageKpiMonthMapDataByParams, fieldMap, response, strList, null);


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
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateTarget"))) {
            params.put("accumulateTarget", request.getParameter("accumulateTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateActual"))) {
            params.put("accumulateActual", request.getParameter("accumulateActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("analyzeDesc"))) {
            params.put("analyzeDesc", request.getParameter("analyzeDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("analyzeRes"))) {
            params.put("analyzeRes", request.getParameter("analyzeRes"));
        }
        return params;


    }
}
