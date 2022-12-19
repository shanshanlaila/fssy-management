package com.fssy.shareholder.management.controller.system.operat.invest;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProcessAbilityList;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.ProcessAbilityListService;
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
 * **数据表名：	bs_operate_process_ability_list	**数据表中文名：	工艺基础能力台账	**业务部门：	经营管理部	**数据表作用：	管理工艺基础能力对比情况台账	**创建人创建日期：	TerryZeng 2022-12-2 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-12-08
 */
@Controller
@RequestMapping("/system/operate/invest/operate-process-ability-list/")
public class ProcessAbilityListController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private ProcessAbilityListService processAbilityListService;


    /**
     * 工艺基础能力台账管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:operate:invest:operate:process:ability:list:index1")
    @RequiredLog("企业研发工艺能力评价")
    public String operateIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/operate/invest/operate-process-ability-list/operate-process-ability-list";
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

        boolean result = processAbilityListService.deleteProcessAbilityListDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }


    /**
     * 更新工艺基础能力台账管理信息
     *
     * @param
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ProcessAbilityList processAbilityList) {
        boolean result = processAbilityListService.updateProcessAbilityListData(processAbilityList);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "企业研发工艺能力评价数据信息更新，请检查数据后重新尝试");
    }



    /**
     * 返回工艺基础能力台账管理
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getProcessAbilityListDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);

        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ProcessAbilityList> processAbilityListDataListPerPageByParams = processAbilityListService.findProcessAbilityListDataListPerPageByParams(params);
        if (processAbilityListDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", processAbilityListDataListPerPageByParams.getTotal());
            result.put("data", processAbilityListDataListPerPageByParams.getRecords());
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
        params.put("noteEq", "工艺基础能力表达台账表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "工艺基础能力表达台账表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/operate/invest/operate-process-ability-list/operate-process-ability-attachment-list";
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


//        try {
//            // 读取附件并保存数据
//            Map<String, Object> resultMap = processAbilityListService.reaadProcessAbilityListDataSource(result, year, companyName);
//            if (Boolean.parseBoolean(resultMap.get("failed").toString())) {// "failed" : true
//                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
//                        result.getId().toString(), String.valueOf(resultMap.get("content")));
//                return SysResult.build(200, "表中有空值，未导入成功的数据请看附件导入列表页面！请重新导入失败的数据");
//            } else {
//                // 修改附件为导入成功
//                attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_SUCCESS,
//                        result.getId().toString(), "导入成功");// 表格备注中的内容
//                return SysResult.ok();
//            }
//        } catch (ServiceException e) {
//            // 修改附件的导入状态为失败
//            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED, result.getId().toString(), e.getMessage());
//            throw new ServiceException("计划导入失败，失败原因请查看附件列表备注描述，更改后请重新导入数据");
//        } catch (Exception e) {
//            // 修改附件导入状态为失败
//            attachmentService.changeImportStatus(CommonConstant.IMPORT_RESULT_FAILED,
//                    result.getId().toString());
//            e.printStackTrace();
//            throw e;
//        }
        return SysResult.ok();
    }




    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();

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
        if (!ObjectUtils.isEmpty(request.getParameter("productLineTypeName"))) {
            params.put("productLineTypeName", request.getParameter("productLineTypeName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("competeCompanyName"))) {
            params.put("competeCompanyName", request.getParameter("competeCompanyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkCompanyName"))) {
            params.put("benchmarkCompanyName", request.getParameter("benchmarkCompanyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("processAbilityStatus"))) {
            params.put("processAbilityStatus", request.getParameter("processAbilityStatus"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("productName"))) {
            params.put("productName", request.getParameter("productName"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("reportDate"))) {
            params.put("reportDate", request.getParameter("reportDate"));
        }

        return params;
    }
}
