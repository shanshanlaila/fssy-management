package com.fssy.shareholder.management.controller.system.company;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.LicenseWarranty;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.company.LicenseWarrantyService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 基础	营业执照	 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2023-02-16
 */
@Controller
@RequestMapping("/system/company/license/")
public class LicenseWarrantyController {

    @Autowired
    private LicenseWarrantyService licenseWarrantyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;


    /**
     * 基础 营业执照信息 前端控制器
     * @param model
     * @return
     */
    @GetMapping("indexOne")
    @RequiresPermissions("system:company:license:indexOne")
    @RequiredLog("基础营业执照")
    public String  licenseWarrantyIndex(Model model){
        return "system/company/license/license-detail-list";
    }


    /**
     *基础营业执照信息
     * @param
     * @return
     */
    @GetMapping("getObject")
    @ResponseBody
    public Map<String, Object> getLicenseWarrantyDatas(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        Integer limit = Integer.valueOf(request.getParameter("limit"));
        Integer page = Integer.valueOf(request.getParameter("page"));
        params.put("limit",limit);
        params.put("page",page);
        Page<Map<String, Object>> licenseWarrantyPerPageByParams = licenseWarrantyService.findLicenseWarrantyPerPageByParams(params);
        if (licenseWarrantyPerPageByParams.getTotal() == 0){
            result.put("code",404);
            result.put("msg","未查出数据");
        }else {
            result.put("code",0);
            result.put("count",licenseWarrantyPerPageByParams.getTotal());
            result.put("data",licenseWarrantyPerPageByParams.getRecords());
        }
        return  result;
    }

    /**
     * 以主键删除基础企业营业执照信息
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id")Integer id){
        boolean result = licenseWarrantyService.delectLicenseWarrantyById(id);
        if (result){
            return SysResult.ok();
        }else {
            return SysResult.build(500,"删除数据失败");
        }
    }


    /**
     * 更新基础 营业执照明细
     * @param licenseWarranty
     * @return
     */
    @ResponseBody
    @PostMapping("update")
    public SysResult update(LicenseWarranty licenseWarranty){
        boolean result = licenseWarrantyService.updateById(licenseWarranty);
        if (result){
            return SysResult.ok();
        }else {
            return SysResult.build(500,"企业营业执照信息修改失败");
        }
    }


    /**
     * 修改基础 营业执照明细
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request,Model model){
        Integer id = Integer.valueOf(request.getParameter("id"));
        LicenseWarranty licenseWarranty = licenseWarrantyService.getById(id);

        Map<String, Object> companyParams = new HashMap<>();
        //业务判断，用于修改功能
        if (ObjectUtils.isEmpty(licenseWarranty.getCompanyId())){
            throw new ServiceException("企业ID为空");
        }
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        List<String> companyIds = new ArrayList<>();
        Long companyId = Long.valueOf(licenseWarranty.getCompanyId());
        companyIds.add(String.valueOf(companyId));
        model.addAttribute("companyIds", companyIds);
        model.addAttribute("companyNameList", companyNameList);

        model.addAttribute("licenseWarranty",licenseWarranty);
        return "system/company/license/license-detail-edit";
    }


    /**
     * 新增单条记录
     * @param model
     * @return
     */
    @GetMapping("create")
    public String cteateLicenseWarranty(Model model){
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/company/license/license-detail-create";
    }


    /**
     * 新增企业营业执照信息
     * @param licenseWarranty
     * @return
     */
    @ResponseBody
    @PostMapping("store")
    public SysResult store(LicenseWarranty licenseWarranty,HttpServletRequest request){
        boolean result = licenseWarrantyService.insertLicenseWarrantyData(licenseWarranty,request);
        if (result){
            return SysResult.ok();
        }else {
            return SysResult.build(500,"添加失败,请检查后重试");
        }
    }


    /**
     * 基础出资者信息附件上传
     * @param id
     * @param request
     * @param model
     * @return
     */
    @GetMapping("upload/{id}")
    @RequiredLog("基础出资者信息附件上传")
    public String showUploadPage(@PathVariable String id,HttpServletRequest request,Model model) {

        LicenseWarranty project = licenseWarrantyService.getById(id);
        if (ObjectUtils.isEmpty(project)) {
            throw new ServiceException("不存在对应的项目，无法上传附件");
        } else {
            model.addAttribute("project", project);
        }

        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "营业执照附件");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);

        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询","营业执照附件"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/company/license/license-detail-attachment-list";
    }


    /**
     * 提交基础投资者信息附件上传
     *
     * @return 结果
     */
    @PostMapping("submitUploadFile")
    @ResponseBody
    @RequiredLog("提交基础投资者信息附件上传")
    public SysResult submitUploadFile(LicenseWarranty licenseWarranty,HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        String attachmentIds = request.getParameter("attachmentId");
        param.put("attachmentIds", attachmentIds);
        boolean result = licenseWarrantyService.submitUploadFile(licenseWarranty,param);

        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败");
    }



    /**
     * 多附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFiles")
    @RequiredLog("基础投资者信息附件")
    @ResponseBody
    public SysResult uploadFiles(@RequestParam("file") MultipartFile file, Attachment attachment,
                                 HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());// 设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService.findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }

        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);

        return SysResult.ok(result.getId());
    }




    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("businessLicenseDocId"))) {
            params.put("businessLicenseDocId", request.getParameter("businessLicenseDocId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyId"))) {
            params.put("companyId", request.getParameter("companyId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("businessLicenseDocName"))) {
            params.put("businessLicenseDocName", request.getParameter("businessLicenseDocName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("changeProject"))) {
            params.put("changeProject", request.getParameter("changeProject"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("changeBefore"))) {
            params.put("changeBefore", request.getParameter("changeBefore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("changeAfter"))) {
            params.put("changeAfter", request.getParameter("changeAfter"));
        }

        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }

        //计划到账时间查询
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("changeTimeStart")))
        {
            params.put("changeTimeStart", request.getParameter("changeTimeStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("changeTimeEnd")))
        {
            params.put("changeTimeEnd", request.getParameter("changeTimeEnd"));
        }

        return params;
    }

}
