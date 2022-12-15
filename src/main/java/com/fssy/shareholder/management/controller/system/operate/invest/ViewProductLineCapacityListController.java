package com.fssy.shareholder.management.controller.system.operate.invest;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityMonth;
import com.fssy.shareholder.management.pojo.system.operate.invest.ViewProductLineCapacityList;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.operate.invest.ViewProductLineCapacityListService;
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
 * VIEW 前端控制器 重点产线产能
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 */
@Controller
@RequestMapping("/system/operate/invest/view-product-line-capacity")
public class ViewProductLineCapacityListController {

    @Autowired
    private ViewProductLineCapacityListService viewProductLineCapacityListService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private ImportModuleService importModuleService;

    /**
     * 重点产线产能展示界面
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:operate:invest:view-product-line-capacity:index1")
    @RequiredLog("重点产线产能")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/operate/invest/view-product-line-capacity/view-product-line-capacity-list";
    }

    /**
     * 返回 重点产线产能展示界面 数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getViewProductLineCapacityListDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ViewProductLineCapacityList> viewProductLineCapacityListPage = viewProductLineCapacityListService.findViewProductLineCapacityListDataListPerPageByParams(params);
        if (viewProductLineCapacityListPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", viewProductLineCapacityListPage.getTotal());
            result.put("data", viewProductLineCapacityListPage.getRecords());
        }
        return result;
    }

    /**
     * 返回导入 附件上传页面
     * @param model model对象
     * @return 页面
     */
    @RequiredLog("附件上传")
    @RequiresPermissions("system:operate:invest:view-product-line-capacity:index")
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
        params.put("noteEq", "重点产线产能季度数据");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (org.springframework.util.ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "重点产线产能季度数据"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/operate/invest/view-product-line-capacity/view-product-line-capacity-attachment-list";
    }

    /**
     * 导入
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("重点产线产能附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment, HttpServletRequest request) {

        //判断是否选择对应的时间
        Map<String, Object> params = getParams(request);
        String year = (String) params.get("year");
        if (ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("未选择年份，导入失败");
        }
        String quarter = (String) params.get("quarter");
        if (ObjectUtils.isEmpty(params.get("quarter"))) {
            throw new ServiceException("未选择季度，导入失败");
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
            Map<String, Object> resultMap = viewProductLineCapacityListService.readViewProductLineCapacityListDataSource(result,year,quarter);
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
     * 填写存在问题及对策
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String productTypeName = request.getParameter("productTypeName");
        String productLineName = request.getParameter("productLineName");
        String year = request.getParameter("year");
        String quarter = request.getParameter("quarter");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("productTypeName", productTypeName);
        params.put("productLineName", productLineName);
        params.put("year", year);
        params.put("quarter", quarter);
        List<Map<String, Object>> listDataByParams = viewProductLineCapacityListService.findViewProductLineCapacityListDataByParams(params);
        if (!ObjectUtils.isEmpty(listDataByParams)){
            Map<String, Object> viewProductLineCapacityList = listDataByParams.get(0);
            model.addAttribute("viewProductLineCapacityList", viewProductLineCapacityList);
        }else {
            Map<String,Object> map=new HashMap<>();
            model.addAttribute("viewProductLineCapacityList",map);
        }
        return "system/operate/invest/view-product-line-capacity/view-product-line-capacity-edit";
    }

    /**
     * 更新存在问题及对策
     * @param productLineCapacityMonth
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ProductLineCapacityMonth productLineCapacityMonth) {

        boolean result = viewProductLineCapacityListService.updateViewProductLineCapacityListData(productLineCapacityMonth);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "在问题及对策信息没有更新，请检查数据后重新尝试");
    }

    /**
     * 以主键删除以主键删除 选中的重点产线产能记录
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id) {
        boolean result = viewProductLineCapacityListService.deleteViewProductLineCapacityListDataById(id);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }

    /**
     * 返回查看指定重点产线产能的季度构成<br/>
     * @param request 请求实体
     * @param model   model对象
     * @return 页面
     */
    @GetMapping("search-detail")
    public String searchByAssignFromBtn(HttpServletRequest request, Model model)
    {
        String quarterMark = request.getParameter("quarterMark");
        model.addAttribute("quarterMark", quarterMark);
        return "system/operate/invest/view-product-line-capacity/view-product-line-capacity-detail";
    }

    /**
     * 返回指定重点产线产能的指定季度表格数据
     * @param request
     * @return
     */
    @GetMapping("getQuarterMarkData")
    @ResponseBody
    public Map<String, Object> getManagerScoreData(HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        String quarterMark = request.getParameter("quarterMark");
        params.put("limit", limit);
        params.put("page", page);
        Page<Map<String, Object>> viewProductLineCapacityList = viewProductLineCapacityListService.findViewProductLineCapacityListDataMapListPerPageByParams(params,quarterMark);
        if (viewProductLineCapacityList.getTotal() == 0)
        {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        }
        else
        {
            result.put("code", 0);
            result.put("count", viewProductLineCapacityList.getTotal());
            result.put("data", viewProductLineCapacityList.getRecords());
        }
        return result;
    }

    //获取数据库里的数据,展示数据
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("quarter"))) {
            params.put("quarter", request.getParameter("quarter"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("productLineCapacityId"))) {
            params.put("productLineCapacityId", request.getParameter("productLineCapacityId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("productTypeName"))) {
            params.put("productTypeName", request.getParameter("productTypeName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("productLineName"))) {
            params.put("productLineName", request.getParameter("productLineName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("productLineTypeName"))) {
            params.put("productLineTypeName", request.getParameter("productLineTypeName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("sopDate"))) {
            params.put("sopDate", request.getParameter("sopDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("designProductionTakt"))) {
            params.put("designProductionTakt", request.getParameter("designProductionTakt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("yeildYearAccumulate"))) {
            params.put("yeildYearAccumulate", request.getParameter("yeildYearAccumulate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("designCapacityPerYear"))) {
            params.put("designCapacityPerYear", request.getParameter("designCapacityPerYear"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("marketShares"))) {
            params.put("marketShares", request.getParameter("marketShares"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("designActualTakt"))) {
            params.put("designActualTakt", request.getParameter("designActualTakt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("designCapacityPerMonth"))) {
            params.put("designCapacityPerMonth", request.getParameter("designCapacityPerMonth"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("yearAim"))) {
            params.put("yearAim", request.getParameter("yearAim"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("lineLoadRateQuarter"))) {
            params.put("lineLoadRateQuarter", request.getParameter("lineLoadRateQuarter"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("issueQuarter"))) {
            params.put("issueQuarter", request.getParameter("issueQuarter"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("evaluate"))) {
            params.put("evaluate", request.getParameter("evaluate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("improveAction"))) {
            params.put("improveAction", request.getParameter("improveAction"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("quarterMark"))) {
            params.put("quarterMark", request.getParameter("quarterMark"));
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
