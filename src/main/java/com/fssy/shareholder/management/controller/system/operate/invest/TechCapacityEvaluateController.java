package com.fssy.shareholder.management.controller.system.operate.invest;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluateRes;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageMonthScoreSheetOutputService;
import com.fssy.shareholder.management.service.common.override.TechCapacityEvaluateOutputService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.TechCapacityEvaluateService;
import com.fssy.shareholder.management.service.system.operate.invest.TechCapacityEvaluateResService;
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
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-12-02
 */
@Controller
@RequestMapping("/system/operate/invest/operate-tech-capacity-evaluate/")
public class TechCapacityEvaluateController {

    @Autowired
    private TechCapacityEvaluateService operateTechCapacityEvaluateService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private TechCapacityEvaluateResService techCapacityEvaluateResService;

    /**
     * 企业研发工艺能力评价管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:operate:invest:operate-tech-capacity-evaluate:index1")
    @RequiredLog("企业研发工艺能力评价")
    public String operateIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/operate/invest/operate-tech-capacity-evaluate/operate-tech-capacity-evaluate-list";
    }

    /**
     * 返回企业研发工艺能力评价数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getOperateTechCapacityEvaluateDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);

        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<TechCapacityEvaluate> operateTechCapacityEvaluateDataListPerPageByParams = operateTechCapacityEvaluateService.findOperateTechCapacityEvaluateDataListPerPageByParams(params);
        if (operateTechCapacityEvaluateDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", operateTechCapacityEvaluateDataListPerPageByParams.getTotal());
            result.put("data", operateTechCapacityEvaluateDataListPerPageByParams.getRecords());
        }
        return result;
    }


    /**
     * 以主键删除信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id) {

        boolean result = operateTechCapacityEvaluateService.deleteOperateTechCapacityEvaluateDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }

    /**
     * 更新企业研发工艺能力评价数据信息
     *
     * @param operateTechCapacityEvaluate
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(TechCapacityEvaluate operateTechCapacityEvaluate) {
        boolean result = operateTechCapacityEvaluateService.updateOperateTechCapacityEvaluateData(operateTechCapacityEvaluate);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "企业研发工艺能力评价数据信息更新，请检查数据后重新尝试");
    }


    /**
     * 修改企业研发工艺能力评价数据
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String projectName = request.getParameter("projectName");
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        params.put("companyName", companyName);
        params.put("projectName", projectName);
        TechCapacityEvaluate operateTechCapacityEvaluate = operateTechCapacityEvaluateService.findOperateTechCapacityEvaluateDataByParams(params).get(0);
        model.addAttribute("operateTechCapacityEvaluate", operateTechCapacityEvaluate);
        return "system/operate/invest/operate-tech-capacity-evaluate/operate-tech-capacity-evaluate-edit";
    }

    /**
     * 返回查看业研发工艺能力评价数据按年查询信息
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("search-detail")
    public String searchDetail(HttpServletRequest request, Model model) {
        String year = request.getParameter("year");

        String companyName = request.getParameter("companyName");
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        Map<String, Object> params = new HashMap<>();
        params.put("companyName", companyName);
        params.put("year", year);
        //Map<String, Object> operateTechCapacityEvaluate = operateTechCapacityEvaluateService.findOperateDataByParams(params).get(0);
        List<Map<String, Object>> techCapacityEvaluateServices = operateTechCapacityEvaluateService.findOperateDataByParams(params);
        List<TechCapacityEvaluateRes> techCapacityEvaluateRes = techCapacityEvaluateResService.findTechCapacityEvaluateResDataByParams(params);
        TechCapacityEvaluateRes capacityEvaluateRes;

        if (!ObjectUtils.isEmpty(techCapacityEvaluateRes)) {
            capacityEvaluateRes = techCapacityEvaluateRes.get(0);
            model.addAttribute("techCapacityEvaluateRes", capacityEvaluateRes);// 总结
            if (!ObjectUtils.isEmpty(techCapacityEvaluateServices)) {
                model.addAttribute("techCapacityEvaluateServices", techCapacityEvaluateServices);
            }else{
                List<Map<String, Object>> emptyTechCapacityEvaluateServices = new ArrayList<>();
                model.addAttribute("techCapacityEvaluateServices", emptyTechCapacityEvaluateServices);
            }
        } else {
            if (!ObjectUtils.isEmpty(techCapacityEvaluateServices)) {
                model.addAttribute("techCapacityEvaluateServices", techCapacityEvaluateServices);
            }else{
                List<Map<String, Object>> emptyTechCapacityEvaluateServices = new ArrayList<>();
                model.addAttribute("techCapacityEvaluateServices", emptyTechCapacityEvaluateServices);
            }
            TechCapacityEvaluateRes emptyTechCapacityEvaluateRes = new TechCapacityEvaluateRes();
            emptyTechCapacityEvaluateRes.setEvalRes("");
            model.addAttribute("techCapacityEvaluateRes", emptyTechCapacityEvaluateRes);
        }
        return "system/operate/invest/operate-tech-capacity-evaluate/operate-tech-capacity-evaluate-detail";
    }


    @GetMapping("getOperateData")
    @ResponseBody
    public Map<String, Object> getOperateData(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);

        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<TechCapacityEvaluate> operateTechCapacityEvaluateDataListPerPageByParams = operateTechCapacityEvaluateService.findOperateTechCapacityEvaluateDataListPerPageByParams(params);
        if (operateTechCapacityEvaluateDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", operateTechCapacityEvaluateDataListPerPageByParams.getTotal());
            result.put("data", operateTechCapacityEvaluateDataListPerPageByParams.getRecords());
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
    public String materialDataAttachmentIndex(Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "企业研发工艺能力评价表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "企业研发工艺能力评价表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/operate/invest/operate-tech-capacity-evaluate/operate-tech-capacity-evaluate-attachment-list";
    }


    /**
     * 附件上传
     *
     * @param file
     * @param attachment
     * @return
     */
    @PostMapping("uploadFile")
    @RequiredLog("企业研发工艺能力评价表附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");
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
            Map<String, Object> resultMap = operateTechCapacityEvaluateService.reaadOperateTechCapacityEvaluateDataSource(result, year, companyName);
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
     * 企业研发工艺能力评价 导出
     *
     * @param request
     * @param response
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        Map<String, Object> params1 = getParams(request);
        String year = (String) params.get("year");
        String companyName = (String) params.get("companyName");

        //sql语句
        params.put("select", "companyName,year,kpiDesc,kpiFormula,pastOneYearActual,pastTwoYearsActual," +
                "pastThreeYearsActual,issue,responsible,endDate,improveActionSelf,projectName,manageMethod");

        params1.put("select", "companyName,year,evalRes");


        //查询
        List<Map<String, Object>> operateDataByParams = operateTechCapacityEvaluateService.findOperateDataByParams(params);
        List<Map<String, Object>> techCapacityEvaluateRes = techCapacityEvaluateResService.findTechDataByParams(params1);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();


        //需要改变背景的格子
        //fieldMap.put("evalRes", "总结评价");
        fieldMap.put("projectName", "项目");
        fieldMap.put("manageMethod", "管理方法");
        fieldMap.put("kpiDesc", "关键指标");
        fieldMap.put("kpiFormula", "公式");
        fieldMap.put("qiye", "企业");
        fieldMap.put("number", "数值");
        fieldMap.put("pastOneYearActual", "2019年");
        fieldMap.put("pastTwoYearsActual", "2020年");
        fieldMap.put("pastThreeYearsActual", "2021年");
        fieldMap.put("1", "1-8月");
        fieldMap.put("11", "1-8月 ");
        fieldMap.put("111", "涨幅");
        fieldMap.put("1111", "评价");
        fieldMap.put("issue", "存在问题");
        fieldMap.put("improveActionSelf", "企业自提对策");
        fieldMap.put("responsible", "负责人");
        fieldMap.put("endDate", "计划完成时间");

        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        SheetOutputService sheetOutputService = new TechCapacityEvaluateOutputService();
        if (org.springframework.util.ObjectUtils.isEmpty(operateDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        if (org.springframework.util.ObjectUtils.isEmpty(techCapacityEvaluateRes)) {
            throw new ServiceException("未查出数据");
        }


        for (Map<String, Object> operateDataByParam : operateDataByParams) {
            operateDataByParam.put("evalRes", techCapacityEvaluateRes.get(0).get("evalRes"));
        }


        sheetOutputService.exportNum("企业研发工艺能力评价表", operateDataByParams, fieldMap, response, strList, null);

    }


    /**
     * 添加企业研发工艺能力评价数据
     *
     * @param model
     * @return
     */
    @GetMapping("create")
    public String create(Model model) {
        return "system/operate/invest/operate-tech-capacity-evaluate/operate-tech-capacity-evaluate-create";
    }

    /**
     * 添加企业研发工艺能力评价数据信息
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping("store")
    @ResponseBody
    public SysResult storeoperateTechCapacityEvaluate(TechCapacityEvaluate TechCapacityEvaluate, HttpServletRequest request) {
        boolean result = operateTechCapacityEvaluateService.insertOperateTechCapacityEvaluate(TechCapacityEvaluate);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "定量、定性评价占比要为100%，请检查后重试");

    }


    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("manageMethod"))) {
            params.put("manageMethod", request.getParameter("manageMethod"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectName"))) {
            params.put("projectName", request.getParameter("projectName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiDesc"))) {
            params.put("kpiDesc", request.getParameter("kpiDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkCompany"))) {
            params.put("benchmarkCompany", request.getParameter("benchmarkCompany"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmark"))) {
            params.put("benchmark", request.getParameter("benchmark"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("kpiFormula"))) {
            params.put("kpiFormula", request.getParameter("kpiFormula"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyShortName"))) {
            params.put("companyShortName", request.getParameter("companyShortName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("issue"))) {
            params.put("issue", request.getParameter("issue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("improveActionSelf"))) {
            params.put("improveActionSelf", request.getParameter("improveActionSelf"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("responsible"))) {
            params.put("responsible", request.getParameter("responsible"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("endDate"))) {
            params.put("endDate", request.getParameter("endDate"));
        }


        return params;
    }
}
