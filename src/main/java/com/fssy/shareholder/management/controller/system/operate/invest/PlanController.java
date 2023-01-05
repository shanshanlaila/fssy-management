package com.fssy.shareholder.management.controller.system.operate.invest;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestPlan;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestPlanService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资计划表(N+3)		*****数据表名：	bs_operate_invest_plan		*****数据表作用：	各企业公司的非权益投资计划情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 前端控制器
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Controller
@RequestMapping("/system/operate/invest/plan/")
public class PlanController {

    @Autowired
    private InvestPlanService investPlanService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    /**
     * 访问首页
     *
     * @return 首页路径
     */
    @GetMapping("index")
    @RequiresPermissions("system:operate:invest:plan:index")
    public String index(Model model) {
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/operate/invest/invest-plan/invest-plan-list";
    }

    /**
     * 首页数据显示
     *
     * @param request 请求
     * @return map
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("limit", Integer.parseInt(request.getParameter("limit")));
        params.put("page", Integer.parseInt(request.getParameter("page")));
        Page<Map<String, Object>> page = investPlanService.findPlanDataByPageMap(params);
        if (page.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", page.getTotal());
            result.put("data", page.getRecords());
        }
        return result;
    }

    /**
     * 封装请求参数map
     *
     * @param request 请求
     * @return 参数map
     */
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("firstType"))) {
            params.put("firstType", request.getParameter("firstType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("secondType"))) {
            params.put("secondType", request.getParameter("secondType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("thirdType"))) {
            params.put("thirdType", request.getParameter("thirdType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("thirdType"))) {
            params.put("thirdType", request.getParameter("thirdType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyIds"))) {
            params.put("companyIds", request.getParameter("companyIds"));
        }
        return params;
    }

    /**
     * 导入页面
     *
     * @param model 模型
     * @return 路径
     */
    @GetMapping("import")
    public String imports(Model model) {
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        String desc = "投资计划";
        params.put("noteEq", desc);
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", desc));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        model.addAttribute("module", importModules.get(0).getId());
        model.addAttribute("importDateStart", simpleDateFormat.format(new Date()));
        model.addAttribute("moduleName", importModules.get(0).getName());
        return "system/operate/invest/invest-plan/plan-attachment-list";
    }

    /**
     * 附件上传
     *
     * @return 上传结果
     */
    @PostMapping("uploadFile")
    @RequiredLog("投资计划附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());// 设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService.findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(
                    String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);

        try {
            // 读取附件并保存数据
            Map<String, Object> resultMap = investPlanService.readInvestPlanDataSource(result, request);
            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {// "failed" : true
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), String.valueOf(resultMap.get("content")));
                return SysResult.build(200, "部分数据导入成功，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
            } else {
                // 修改附件为导入成功
                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
                        result.getId().toString(), "导入成功");// 表格备注中的内容
                return SysResult.ok();
            }
        } catch (ServiceException e) {
            // 修改附件的导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString(), e.getMessage());
            throw new ServiceException("计划导入失败，失败原因请查看附件列表备注描述，更改后请重新导入数据");
        } catch (Exception e) {
            // 修改附件导入状态为失败
            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
                    result.getId().toString());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        InvestPlan investPlan = investPlanService.getById(id);
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        List<String> companyIds = new ArrayList<>();
        Long companyId = investPlan.getCompanyId();
        companyIds.add(String.valueOf(companyId));
        model.addAttribute("companyIds", companyIds);
        model.addAttribute("investPlan", investPlan);
        return "/system/operate/invest/invest-plan/invest-plan-edit";
    }


}
