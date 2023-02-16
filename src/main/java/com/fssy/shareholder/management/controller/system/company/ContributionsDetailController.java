package com.fssy.shareholder.management.controller.system.company;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.service.system.company.ContributionsDetailService;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
 * 基础	出资方明细	 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Controller
@RequestMapping("/system/company/contributor/")
public class ContributionsDetailController {

    @Autowired
    private ContributionsDetailService contributionsDetailService;


    /**
     * 基础 投资方明细 前端控制器
     * @param model
     * @return
     */
    @GetMapping("indexOne")
    @RequiresPermissions("system:company:contributor:indexOne")
    @RequiredLog("基础出资方明细")
    public String contributionsDetailIndex(Model model){
        return "system/company/contributor/contributions-detail-list";
    }


    /**
     * 基础 出资方明细
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getContributionsDatas(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        Integer limit = Integer.valueOf(request.getParameter("limit"));
        Integer page = Integer.valueOf(request.getParameter("page"));
        params.put("limit",limit);
        params.put("page",page);
        Page<ContributionsDetail> contributionsDetailPerPageByParams = contributionsDetailService.findContributionsDetailPerPageByParams(params);
        if (contributionsDetailPerPageByParams.getTotal()==0){
            result.put("code",404);
            result.put("msg","未查出数据");
        }else {
            result.put("code",0);
            result.put("count",contributionsDetailPerPageByParams.getTotal());
            result.put("data",contributionsDetailPerPageByParams.getRecords());
        }
        return result;
    }

    /**
     * 以主键删除基础投资方明细信息
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id")Integer id){
        boolean result = contributionsDetailService.delectContributionsDetailByid(id);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"删除数据失败");
    }


    /**
     * 更新基础 投资方明细
     * @param contributionsDetail
     * @return
     */
    @ResponseBody
    @PostMapping("update")
    public SysResult update(ContributionsDetail contributionsDetail){
        boolean result = contributionsDetailService.updateContributionsDetailData(contributionsDetail);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"企业基础投资方明细修改失败");
    }

    /**
     * 修改基础 投资方明细
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model){
        Integer id = Integer.valueOf(request.getParameter("id"));
        ContributionsDetail contributionsDetail = contributionsDetailService.getById(id);
        model.addAttribute("contributionsDetail",contributionsDetail);
        return  "system/company/contributor/contributions-detail-edit";
    }

    /**
     * 新增单条记录
     * @param model
     * @return
     */
    @GetMapping("create")
    public String cteateRightsDetail(Model model){
        return "system/company/contributor/contributions-detail-create";
    }


    //新增基础股权信息
    @PostMapping("store")
    @ResponseBody
    public SysResult store(ContributionsDetail contributionsDetail){
        boolean result = contributionsDetailService.insertContributionsDetailData(contributionsDetail);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"添加失败,请检查后重试");
    }




    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("contributionsListId"))) {
            params.put("contributionsListId", request.getParameter("contributionsListId"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("contributionsDocId"))) {
            params.put("contributionsDocId", request.getParameter("contributionsDocId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("contributionsDocName"))) {
            params.put("contributionsDocName", request.getParameter("contributionsDocName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("causeDesc"))) {
            params.put("causeDesc", request.getParameter("causeDesc"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("contributionsMode"))) {
            params.put("contributionsMode", request.getParameter("contributionsMode"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("registeredCapital"))) {
            params.put("registeredCapital", request.getParameter("registeredCapital"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("contributedCapital"))) {
            params.put("contributedCapital", request.getParameter("contributedCapital"));
        }
        //计划到账时间查询
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("planArrivalTimeStart")))
        {
            params.put("planArrivalTimeStart", request.getParameter("planArrivalTimeStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("planArrivalTimeEnd")))
        {
            params.put("planArrivalTimeEnd", request.getParameter("planArrivalTimeEnd"));
        }

        //实际到账时间查询
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("actualArrivalTimeStart")))
        {
            params.put("actualArrivalTimeStart", request.getParameter("actualArrivalTimeStart"));
        }
        if (!org.springframework.util.ObjectUtils.isEmpty(request.getParameter("actualArrivalTimeEnd")))
        {
            params.put("actualArrivalTimeEnd", request.getParameter("actualArrivalTimeEnd"));
        }


        if (!ObjectUtils.isEmpty(request.getParameter("actualArrivalTime"))) {
            params.put("actualArrivalTime", request.getParameter("actualArrivalTime"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("sign"))) {
            params.put("sign", request.getParameter("sign"));
        }

        return params;
    }
}
