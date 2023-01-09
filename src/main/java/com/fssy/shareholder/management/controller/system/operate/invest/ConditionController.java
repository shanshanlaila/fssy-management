package com.fssy.shareholder.management.controller.system.operate.invest;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.Condition;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.ConditionService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资执行情况表		*****数据表名：	bs_operate_invest_condition		*****数据表作用：	各企业公司的非权益投资执行情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2023-01-03
 */
@Controller
@RequestMapping("/system/operate/invest/Condition/")
public class ConditionController {
    @Autowired
    private ConditionService conditionService;
    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    @Autowired
    private ImportModuleService importModuleService;
    @Autowired
    private CompanyService companyService;

    /**
     * 年度投资项目执行情况管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiresPermissions("system:operate:invest:Condition:index")
    @RequiredLog("年度投资项目执行情况管理页面")
    public String investProjectListIndex(Model model) {
        //1、查询公司列表，用于companyName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/operate/invest/invest-condition/invest-condition-list";
    }

    /**
     * 返回 对象列表
     *
     * @param request 前端请求参数
     * @return Map集合
     */
    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<Condition> handlersItemPage = conditionService.findDataListByParams(params);
        if (handlersItemPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            // 查出数据，返回分页数据
            result.put("code", 0);
            result.put("count", handlersItemPage.getTotal());
            result.put("data", handlersItemPage.getRecords());
        }

        return result;
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyCode"))) {
            params.put("companyCode", request.getParameter("companyCode"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyId"))) {
            params.put("companyId", request.getParameter("companyId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("lineNumber"))) {
            params.put("lineNumber", request.getParameter("lineNumber"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("firstType"))) {
            params.put("firstType", request.getParameter("firstType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("item"))) {
            params.put("item", request.getParameter("item"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("budget"))) {
            params.put("budget", request.getParameter("budget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulation"))) {
            params.put("accumulation", request.getParameter("accumulation"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("unit"))) {
            params.put("unit", request.getParameter("unit"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("describe"))) {
            params.put("describe", request.getParameter("describe"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("proportion"))) {
            params.put("proportion", request.getParameter("proportion"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("evaluate"))) {
            params.put("evaluate", request.getParameter("evaluate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdAt"))) {
            params.put("createdAt", request.getParameter("createdAt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdId"))) {
            params.put("createdId", request.getParameter("createdId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdName"))) {
            params.put("createdName", request.getParameter("createdName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedAt"))) {
            params.put("updatedAt", request.getParameter("updatedAt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedId"))) {
            params.put("updatedId", request.getParameter("updatedId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedName"))) {
            params.put("updatedName", request.getParameter("updatedName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyShortName"))) {
            params.put("companyShortName", request.getParameter("companyShortName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyCode"))) {
            params.put("companyCode", request.getParameter("companyCode"));
        }
        return params;
    }
    /**
     * 返回年度投资项目执行情况附件管理页面
     *
     * @param model model对象
     * @return
     */
    @RequiredLog("年度投资项目执行情况附件管理")
    @GetMapping("import")
    @RequiresPermissions("system:operate:invest:Condition:import")
    public String showImportPage(Model model,HttpServletRequest request)
    {
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "年度投资项目执行情况附件");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules))
        {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "完整年度投资项目执行情况附件"));
        }
        model.addAttribute("module", importModules.get(0).getId());

        //1、查询公司列表，用于companyName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);

        String month = request.getParameter("month");
        String companyId = request.getParameter("companyId");
        model.addAttribute("moth",month);
        model.addAttribute("companyId", companyId);

        return "system/operate/invest/invest-condition/invest-condition-attachment-list";
    }

    @PostMapping("uploadFile")
    @RequiredLog("年度投资项目执行情况附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {
        // 保存附件
        // 查询导入场景对象
        ImportModule module = importModuleService
                .findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module))
        {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result= fileAttachmentTool.storeFileToModule(file, module, attachment);
        try {
            //读取附件并保存数据
            Map<String, Object> resultMap = conditionService.readConditionDataSource(result,request);
            //判断是否失败，实现类中的setFailedContent()
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS
                        , result.getId().toString(), String.valueOf(resultMap.get("content")));
                return SysResult.build(200, "部分数据导入成功，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
            } else {
                //修改附件为导入成功
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), "导入成功");
                return SysResult.ok();
            }
        } catch (ServiceException e) {
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString(), e.getMessage());
            throw new ServiceException("计划导入失败，失败原因请查看附件列表备注描述，更改后请重新导入数据");
        } catch (Exception e) {
            //修改附件导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED, result.getId().toString());
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * 展示修改页面
     *
     * @param id    年度投资项目清单id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    @RequiresPermissions("system:operate:invest:Condition:edit")
    public String showEditPage(@PathVariable String id, Model model) {
        Condition condition = conditionService.getById(id);
        model.addAttribute("condition",condition);//把condition传到前端
        //1、查询公司列表，用于companyName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<String> selectedList = new ArrayList<>();
        //业务判断，用于修改功能
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(condition.getCompanyId())){
            throw new ServiceException("企业ID为空");
        }
        selectedList.add(condition.getCompanyId().toString());//xm-select默认选择控件
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, selectedList);
        model.addAttribute("companyNameList", companyNameList);
        return "system/operate/invest/invest-condition/invest-condition-edit";

    }

    /**
     * 更新非权益投资情况
     *
     * @param Condition 年度项目执行情况
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(Condition Condition, HttpServletRequest request) {
        Map<String,Object> params=new HashMap<>();
        boolean result = conditionService.updateInvestConditionData(Condition,request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "非权益投资情况更新失败");
    }

    /**
     * 删除非权益投资情况，后期开放
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
     * 返回新增单条基础事件页面
     *
     * @param request
     * @return
     */
    @GetMapping("create")
    @RequiresPermissions("system:operate:invest:Condition:create")
    public String createInvestCondition(HttpServletRequest request, Model model) {
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
//        String year = request.getParameter("year");
//        String companyId = request.getParameter("companyId");
//        String month = request.getParameter("month");
//        String projectName = request.getParameter("projectName");
//        model.addAttribute("year", year);
//        model.addAttribute("companyId", companyId);
//        model.addAttribute("month", month);
//        model.addAttribute("projectName", projectName);
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/operate/invest/invest-condition/invest-condition-create";
    }
    /**
     * 保存非权益投资情况表
     *
     * @param condition
     * @return
     */
    @PostMapping("store")
    @RequiredLog("保存新增单条非权益投资情况")
    @ResponseBody
    public SysResult Store(Condition condition, HttpServletRequest request) {
        boolean result = conditionService.insertInvestCondition(condition,request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "新增失败");
    }

}
