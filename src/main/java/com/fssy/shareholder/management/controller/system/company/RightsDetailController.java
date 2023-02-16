package com.fssy.shareholder.management.controller.system.company;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.service.system.company.RightsDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 基础	股权明细 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Controller
@RequestMapping("/system/company/stock/")
public class RightsDetailController {
    @Autowired
    private RightsDetailService rightsDetailService;


    /**
     * 基础 股权管理页面
     * @param model
     * @return
     */
    @GetMapping("indexOne")
    @RequiresPermissions("system:company:stock:indexOne")
    @RequiredLog("企业基础股权明细")
    public String rightsDetailIndex(Model model){
        return "system/company/stock/stock-rights-detail-list";
    }

    //返回 基础 股权信息表格
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getRightsDetailDatas(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);

        Integer limit = Integer.valueOf(request.getParameter("limit"));
        Integer page = Integer.valueOf(request.getParameter("page"));
        params.put("limit",limit);
        params.put("page",page);
        Page<RightsDetail> rightsDetailDataListPerPageByParams = rightsDetailService.findRightsDetailDataListPerPageByParams(params);

        if (rightsDetailDataListPerPageByParams.getTotal()==0){
            result.put("code",404);
            result.put("msg","未查出数据");
        }else {
            result.put("code",0);
            result.put("count",rightsDetailDataListPerPageByParams.getTotal());
            result.put("data",rightsDetailDataListPerPageByParams.getRecords());
        }
        return result;
    }

    /**
     * 以主键删除基础股权信息
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id")Integer id){
        boolean result = rightsDetailService.delectRightsDetailDataById(id);
        if (result){
            return  SysResult.ok();
        }
        return SysResult.build(500,"删除数据失败");
    }


    /**
     * 更新基础 股权信息
     * @param rightsDetail
     * @return
     */
    @ResponseBody
    @PostMapping("update")
    public SysResult update(RightsDetail rightsDetail){
        boolean result = rightsDetailService.updateRightsDetailData(rightsDetail);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"企业基础股权信息更新失败");
    }

    /**
     * 修改 基础 股权信息
     * @param request
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request,Model model){
        Integer id = Integer.valueOf(request.getParameter("id"));
        RightsDetail rightsDetail = rightsDetailService.getById(id);
        model.addAttribute("rightsDetail",rightsDetail);
        return "system/company/stock/stock-rights-detail-edit";
    }

    /**
     * 新增单条记录
     * @param model
     * @return
     */
    @GetMapping("create")
    public String cteateRightsDetail(Model model){
        return "system/company/stock/stock-rights-detail-create";
    }




    /**
     * 新增基础股权信息
     * @param rightsDetail
     * @return
     */
    @PostMapping("store")
    @ResponseBody
    public SysResult store(RightsDetail rightsDetail){
        boolean result = rightsDetailService.insertRightsDetailData(rightsDetail);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"添加失败,请检查后重试");
    }



    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("stockRightsListId"))) {
            params.put("stockRightsListId", request.getParameter("stockRightsListId"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("stockRightsDocId"))) {
            params.put("stockRightsDocId", request.getParameter("stockRightsDocId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("stockRightsDocName"))) {
            params.put("stockRightsDocName", request.getParameter("stockRightsDocName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("causeDesc"))) {
            params.put("causeDesc", request.getParameter("causeDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("stockRightsType"))) {
            params.put("stockRightsType", request.getParameter("stockRightsType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investorType"))) {
            params.put("investorType", request.getParameter("investorType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investorId"))) {
            params.put("investorId", request.getParameter("investorId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investorName"))) {
            params.put("investorName", request.getParameter("investorName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("sign"))) {
            params.put("sign", request.getParameter("sign"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }

        //变更时间开始查询
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
