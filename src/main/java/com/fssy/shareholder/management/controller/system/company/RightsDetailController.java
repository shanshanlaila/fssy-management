package com.fssy.shareholder.management.controller.system.company;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.company.ContributionsDetailService;
import com.fssy.shareholder.management.service.system.company.RightsDetailService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private ContributionsDetailService contributionsDetailService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ImportModuleService importModuleService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;


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
        Page<Map<String,Object>> rightsDetailDataListPerPageByParams = rightsDetailService.findRightsDetailDataListPerPageByParams(params);

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
    public SysResult update(RightsDetail rightsDetail,HttpServletRequest request){
        boolean result = rightsDetailService.updateRightsDetailData(rightsDetail,request);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"企业基础股权信息更新失败");
    }

    /**
     * 修改 基础 股权信息
     * @param
     * @return
     */
    @GetMapping("edit/{id}")
    public String edit(Model model, @PathVariable String id){
        RightsDetail rightsDetail = rightsDetailService.getById(id);

        Map<String, Object> companyParams = new HashMap<>();
        //业务判断，用于修改功能
        if (ObjectUtils.isEmpty(rightsDetail.getCompanyId())){
            throw new ServiceException("企业ID为空");
        }
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        List<String> companyIds = new ArrayList<>();
        List<String> investorIds = new ArrayList<>();
        Long companyId = Long.valueOf(rightsDetail.getCompanyId());
        Long investorId = Long.valueOf(rightsDetail.getInvestorId());
        companyIds.add(String.valueOf(companyId));
        investorIds.add(String.valueOf(investorId));
        model.addAttribute("companyIds", companyIds);
        model.addAttribute("investorIds", investorIds);

        model.addAttribute("companyNameList", companyNameList);
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
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        return "system/company/stock/stock-rights-detail-create";
    }




    /**
     * 新增基础股权信息
     * @param rightsDetail
     * @return
     */
    @PostMapping("store")
    @ResponseBody
    public SysResult store(RightsDetail rightsDetail,HttpServletRequest request){
        boolean result = rightsDetailService.insertRightsDetailData(rightsDetail,request);
        if (result){
            return SysResult.ok();
        }
        return SysResult.build(500,"添加失败,请检查后重试");
    }


    @GetMapping("upload/{id}")
    @RequiredLog("基础股权附件上传页面")
    public String showUploadPage(@PathVariable String id,HttpServletRequest request,Model model) {
        RightsDetail project = rightsDetailService.getById(id);
        if (ObjectUtils.isEmpty(project)) {
            throw new ServiceException("不存在对应的项目，无法上传附件");
        } else {
            model.addAttribute("project", project);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "股权文件附件上传表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);

        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询","股权文件附件上传表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/company/stock/stock-rights-detail-attachment-list";
    }



    /**//**//**//**
     * 附件上传
     *
     * @return 上传结果
     *//**//**//**/
    @PostMapping("uploadFiles")
    @RequiredLog("基础股权附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());// 设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService.findById(InstandTool.integerToLong(attachment.getModule()));
        if (org.springframework.util.ObjectUtils.isEmpty(module)) {
            throw new ServiceException(String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);


        return SysResult.ok(result.getId());


    }

    /**
     * 提交年度投资项目附件上传
     *
     * @return 结果
     */
    @PostMapping("submitUploadFile")
    @ResponseBody
    @RequiredLog("提交年度投资项目附件上传")
    public SysResult submitUploadFile(ContributionsDetail contributionsDetail,HttpServletRequest request) {
        Map<String, Object> param = new HashMap<>();
        String attachmentIds = request.getParameter("attachmentId");
        System.out.println(attachmentIds);
        param.put("attachmentIds", attachmentIds);
        boolean result = contributionsDetailService.submitUploadFile(contributionsDetail,param);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败");
    }



   /* @RequiredLog("附件上传")
    @GetMapping("upload/{id}")
    public String materialDataAttachmentIndex(@PathVariable String id,Model model) {
        RightsDetail project = rightsDetailService.getById(id);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        model.addAttribute("project",project);
        // 查询导入场景
        Map<String, Object> params = new HashMap<>();
        params.put("noteEq", "股权文件附件上传表");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "股权文件附件上传表"));
        }
        model.addAttribute("module", importModules.get(0).getId());


        return "system/company/stock/stock-rights-detail-attachment-list";
    }


    @PostMapping("uploadFile")
    @RequiredLog("基础股权附件上传页面")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request,
                                RightsDetail rightsDetail) {
        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String companyName = (String) params.get("companyName");
        Integer id = (Integer) params.get("id");

        if (org.springframework.util.ObjectUtils.isEmpty(params.get("companyName"))) {
            throw new ServiceException("未选择公司，导入失败");
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

        Map<String, Object> param = new HashMap<>();
        String attachmentIds = request.getParameter("attachmentId");
        param.put("attachmentIds", attachmentIds);
        boolean resultTwo = rightsDetailService.submitUploadFile(rightsDetail,param);


        return SysResult.ok(result.getId());
    }
*/



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
