package com.fssy.shareholder.management.controller.system.company;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.LicenseWarranty;
import com.fssy.shareholder.management.service.system.company.LicenseWarrantyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
        Page<LicenseWarranty> licenseWarrantyPerPageByParams = licenseWarrantyService.findLicenseWarrantyPerPageByParams(params);
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
        return "system/company/license/license-detail-create";
    }


    /**
     * 新增企业营业执照信息
     * @param licenseWarranty
     * @return
     */
    @ResponseBody
    @PostMapping("store")
    public SysResult store(LicenseWarranty licenseWarranty){
        boolean result = licenseWarrantyService.insertLicenseWarrantyData(licenseWarranty);
        if (result){
            return SysResult.ok();
        }else {
            return SysResult.build(500,"添加失败,请检查后重试");
        }
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
        if (!ObjectUtils.isEmpty(request.getParameter("investorId"))) {
            params.put("investorId", request.getParameter("investorId"));
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
