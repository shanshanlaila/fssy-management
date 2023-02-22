package com.fssy.shareholder.management.controller.system.company;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.company.ContributionsDetailService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

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

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;


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
        Page<Map<String, Object>> contributionsDetailPerPageByParams = contributionsDetailService.findContributionsDetailPerPageByParams(params);
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
    public SysResult update(ContributionsDetail contributionsDetail,HttpServletRequest request){
        boolean result = contributionsDetailService.updateContributionsDetailData(contributionsDetail,request);
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

        Map<String, Object> companyParams = new HashMap<>();
        //业务判断，用于修改功能
        if (ObjectUtils.isEmpty(contributionsDetail.getCompanyId())){
            throw new ServiceException("企业ID为空");
        }
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        List<String> companyIds = new ArrayList<>();
        List<String> investorIds = new ArrayList<>();
        Long companyId = Long.valueOf(contributionsDetail.getCompanyId());
        Long investorId = Long.valueOf(contributionsDetail.getInvestorId());
        companyIds.add(String.valueOf(companyId));
        investorIds.add(String.valueOf(investorId));
        model.addAttribute("companyIds", companyIds);
        model.addAttribute("investorIds", investorIds);

        model.addAttribute("companyNameList", companyNameList);

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
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/company/contributor/contributions-detail-create";
    }


    /**
     * 新增基础股权信息
     * @param contributionsDetail
     * @param request
     * @return
     */
    @PostMapping("store")
    @ResponseBody
    public SysResult store(ContributionsDetail contributionsDetail,HttpServletRequest request){

        boolean result = contributionsDetailService.insertContributionsDetailData(contributionsDetail,request);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"添加失败,请检查后重试");
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
        ContributionsDetail project = contributionsDetailService.getById(id);
        if (ObjectUtils.isEmpty(project)) {
            throw new ServiceException("不存在对应的项目，无法上传附件");
        } else {
            model.addAttribute("project", project);
        }

        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "出资文件附件上传");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);

        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询","出资文件表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/company/contributor/contributions-detail-upload";
    }


    /**
     * 提交基础投资者信息附件上传
     *
     * @return 结果
     */
    @PostMapping("submitUploadFile")
    @ResponseBody
    @RequiredLog("提交基础投资者信息附件上传")
    public SysResult submitUploadFile(ContributionsDetail contributionsDetail,HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        String attachmentIds = request.getParameter("attachmentId");
        param.put("attachmentIds", attachmentIds);
        boolean result = contributionsDetailService.submitUploadFile(contributionsDetail,param);

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
