package com.fssy.shareholder.management.controller.system.operate.invest;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectMonth;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectMonthService;
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
 * **数据表名：	bs_operate_invest_project_month	**数据表中文名：	项目月度进展表	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目月度状态	**创建人创建日期：	TerryZeng 2022-12-2 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-12-05
 */
@Controller
@RequestMapping("/system/operate/invest/invest-project-month")
public class InvestProjectMonthController {

    @Autowired
    private InvestProjectMonthService investProjectMonthService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 项目月度进展界面
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:operate:invest:invest-project-month:index1")
    @RequiredLog("项目月度进展")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/operate/invest/invest-project-month/invest-project-month-list";
    }

    /**
     * 返回 项目月度进展界面 数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getInvestProjectMonthDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<Map<String, Object>> investProjectMonthPage = investProjectMonthService.findInvestProjectMonthDataMapListPerPageByParams(params);
        if (investProjectMonthPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", investProjectMonthPage.getTotal());
            result.put("data", investProjectMonthPage.getRecords());
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
    @RequiredLog("项目月度进展附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {

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
            Map<String, Object> resultMap = investProjectMonthService.readInvestProjectMonthDataSource(result,year);
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
     * 返回导入 附件上传页面
     * @param model model对象
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:operate:invest:invest-project-month:index")
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
        params.put("noteEq", "项目月度进展表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "项目月度进展表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/operate/invest/invest-project-month/invest-project-month-attachment-list";
    }

    /**
     * 以主键删除项目月度进展记录
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id) {
        boolean result = investProjectMonthService.deleteInvestProjectMonthDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }

    //未实现
    /**
     * 修改项目月度进展信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String projectName = request.getParameter("projectName");
        String companyName = request.getParameter("companyName");
        String year = request.getParameter("year");
        Map<String, Object> params = new HashMap<>();
        params.put("projectName", projectName);
        params.put("companyName", companyName);
        params.put("year", year);
        Map<String, Object> investProjectMonth = investProjectMonthService.findInvestProjectMonthDataByParams(params).get(0);
        model.addAttribute("investProjectMonth", investProjectMonth);
        return "system/operate/invest/invest-project-month/invest-project-month-edit";
    }

    /**
     * 更新项目月度进展信息
     * @param investProjectMonth
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(InvestProjectMonth investProjectMonth) {

        boolean result = investProjectMonthService.updateInvestProjectMonthData(investProjectMonth);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "项目月度进展信息没有更新，请检查数据后重新尝试");
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
        if (!ObjectUtils.isEmpty(request.getParameter("projectName"))) {
            params.put("projectName", request.getParameter("projectName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investmentVolumeMonth"))) {
            params.put("investmentVolumeMonth", request.getParameter("investmentVolumeMonth"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investmentVolumeAccumulate"))) {
            params.put("investmentVolumeAccumulate", request.getParameter("investmentVolumeAccumulate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectStatusMonth"))) {
            params.put("projectStatusMonth", request.getParameter("projectStatusMonth"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectRiskMonth"))) {
            params.put("projectRiskMonth ", request.getParameter("projectRiskMonth"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthIssue"))) {
            params.put("monthIssue", request.getParameter("monthIssue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("improveAction"))) {
            params.put("improveAction", request.getParameter("improveAction"));
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
        return params;
    }
}
