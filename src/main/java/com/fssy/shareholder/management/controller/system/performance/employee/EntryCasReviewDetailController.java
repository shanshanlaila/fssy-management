package com.fssy.shareholder.management.controller.system.performance.employee;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工履职回顾明细		*****数据表名：	bs_performance_employee_entry_cas_review_detail		*****数据表作用：	员工每月月末对月初计划的回顾		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2022-10-20
 */
@Controller
@RequestMapping("/system/entry-cas-review-detail/")
public class EntryCasReviewDetailController {
    @Autowired
    private EntryCasReviewDetailService entryCasReviewDetailService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    /**
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiredLog("履职明细情况计划")
    @RequiresPermissions("system:performance:entryCasReviewDetail")
    public String showEntryCasReviewDetail(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "/system/performance/employee/performance-entry-cas-review-detail-list";
    }

    /**
     * 返回 对象列表
     *
     * @param request 前端请求参数
     * @return Map集合
     */
    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<EntryCasReviewDetail> handlersItemPage = entryCasReviewDetailService.findDataListByParams(params);
        if (handlersItemPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            // 查出数据，返回分页数据
            result.put("code", 0);
            result.put("count", handlersItemPage.getTotal());
            result.put("data", handlersItemPage.getRecords());
        }

        return result;
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createDate"))) {
            params.put("createDate", request.getParameter("createDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsId"))) {
            params.put("eventsId", request.getParameter("eventsId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("jobName"))) {
            params.put("jobName", request.getParameter("jobName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workEvents"))) {
            params.put("workEvents", request.getParameter("workEvents"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsForm"))) {
            params.put("eventsForm", request.getParameter("eventsForm"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("standardValue"))) {
            params.put("standardValue", request.getParameter("standardValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("delowStandard"))) {
            params.put("delowStandard", request.getParameter("delowStandard"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("middleStandard"))) {
            params.put("middleStandard", request.getParameter("middleStandard"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("fineStandard"))) {
            params.put("fineStandard", request.getParameter("fineStandard"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("excellentStandard"))) {
            params.put("excellentStandard", request.getParameter("excellentStandard"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mainOrNext"))) {
            params.put("mainOrNext", request.getParameter("mainOrNext"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentName"))) {
            params.put("departmentName", request.getParameter("departmentName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentIds"))) {
            String departmentIds = request.getParameter("departmentIds");
            List<String> departmentIdList = Arrays.asList(departmentIds.split(","));
            params.put("departmentIdList", departmentIdList);
        }
        if (!ObjectUtils.isEmpty(request.getParameter("roleName"))) {
            params.put("roleName", request.getParameter("roleName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("roleIds"))) {
            String roleIds = request.getParameter("roleIds");
            List<String> roleIdList = Arrays.asList(roleIds.split(","));
            params.put("roleIdList", roleIdList);
        }
        if (!ObjectUtils.isEmpty(request.getParameter("roleId"))) {
            params.put("roleId", request.getParameter("roleId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("userName"))) {
            params.put("userName", request.getParameter("userName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("userId"))) {
            params.put("userId", request.getParameter("userId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("applyDate"))) {
            params.put("applyDate", request.getParameter("applyDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("planningWork"))) {
            params.put("planningWork", request.getParameter("planningWork"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("times"))) {
            params.put("times", request.getParameter("times"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workOutput"))) {
            params.put("workOutput", request.getParameter("workOutput"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("planOutput"))) {
            params.put("planOutput", request.getParameter("planOutput"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("planStartDate"))) {
            params.put("planStartDate", request.getParameter("planStartDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("planEndDate"))) {
            params.put("planEndDate", request.getParameter("planEndDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createDate"))) {
            params.put("createDate", request.getParameter("createDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createName"))) {
            params.put("createName", request.getParameter("createName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createId"))) {
            params.put("createId", request.getParameter("createId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditName"))) {
            params.put("auditName", request.getParameter("auditName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditId"))) {
            params.put("auditId", request.getParameter("auditId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditDate"))) {
            params.put("auditDate", request.getParameter("auditDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditNote"))) {
            params.put("auditNote", request.getParameter("auditNote"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("autoScore"))) {
            params.put("autoScore", request.getParameter("autoScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mergeNo"))) {
            params.put("mergeNo", request.getParameter("mergeNo"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mergeId"))) {
            params.put("mergeId", request.getParameter("mergeId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsFirstType"))) {
            params.put("eventsFirstType", request.getParameter("eventsFirstType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditStatus"))) {
            params.put("auditStatus", request.getParameter("auditStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("attachmentId"))) {
            params.put("attachmentId", request.getParameter("attachmentId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("statusCancel"))) {
            params.put("statusCancel", request.getParameter("statusCancel"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("chargeTransactionEvaluateLevel"))) {
            params.put("chargeTransactionEvaluateLevel", request.getParameter("chargeTransactionEvaluateLevel"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("chargeTransactionBelowType"))) {
            params.put("chargeTransactionBelowType", request.getParameter("chargeTransactionBelowType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("chargeNontransactionEvaluateLevel"))) {
            params.put("chargeNontransactionEvaluateLevel", request.getParameter("chargeNontransactionEvaluateLevel"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("chargeNontransactionBelowType"))) {
            params.put("chargeNontransactionBelowType", request.getParameter("chargeNontransactionBelowType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("finalNontransactionEvaluateLevel"))) {
            params.put("finalNontransactionEvaluateLevel", request.getParameter("finalNontransactionEvaluateLevel"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("ministerReview"))) {
            params.put("ministerReview", request.getParameter("ministerReview"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsFirstTypeNe"))) {
            params.put("eventsFirstTypeNe", request.getParameter("eventsFirstTypeNe"));
        }
        return params;
    }

    /**
     * 展示修改页面
     *
     * @param id    履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "system/performance/employee/performance-entry-cas-review-detail-edit";
    }

    /**
     * 更新履职明细
     *
     * @param entryCasReviewDetail 履职明细实体
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(EntryCasReviewDetail entryCasReviewDetail) {
        boolean result = entryCasReviewDetailService.updateEntryCasReviewDetail(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职明细更新失败");
    }

    /**
     * 取消履职明细
     *
     * @param id id
     * @return 取消结果
     */
    @PostMapping("cancel/{id}")
    @ResponseBody
    public SysResult cancel(@PathVariable String id) {
        LambdaUpdateWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        entryCasReviewDetailLambdaUpdateWrapper.eq(EntryCasReviewDetail::getId, id).set(EntryCasReviewDetail::getStatus, "取消");
        boolean result = entryCasReviewDetailService.update(entryCasReviewDetailLambdaUpdateWrapper);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "取消失败");
    }

    /**
     * 工作计划完成情况审核评价 （部 门 领导、非事务）
     *
     * @param model
     * @return
     */
    @GetMapping("MinisterIndex")
    @RequiredLog("履职计划部 门 领导、非事务审核评价管理")
    @RequiresPermissions("system:performance:entryCasReviewDetail")
    public String showEntryCasReviewDetailByMinster(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "/system/performance/employee/performance-entry-cas-review-detail-minister-list";
    }

    /**
     * 工作计划完成情况审核评价 （科长，事务类）
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiredLog("履职计划科长审核评价")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String showEntryCasPlanDetailListBySection_chief(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "/system/performance/employee/performance-entry-cas-review-detail-section-chief-list";
    }

    /**
     * 履职计划部 门 领导、非事务审核评价
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("MinisterEdit/{id}")
    public String showEditPageByMinister(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "/system/performance/employee/performance-entry-cas-review-detail-minister-edit";
    }

    /**
     * 工作计划完成情况审核（科长）-修改按钮
     *
     * @param id id
     * @return 路径
     */
    @GetMapping("sectionEdit/{id}")
    public String showEditPageBySection(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "/system/performance/employee/performance-entry-cas-review-detail-section-chief-edit";
    }

    /**
     * 筛选状态-提交审核
     *
     * @return 结果
     */
    @RequiredLog("提交审核")
    //@RequiresPermissions("system:performance:entryCasPlanDetail:indexStatus")
    @PostMapping("indexStatus")
    @ResponseBody
    public SysResult indexStatus(@RequestParam(value = "reviewDetailIds[]") List<String> reviewDetailIds) {
        boolean result = entryCasReviewDetailService.submitAudit(reviewDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "只能选择待提交审核状态的事件清单，提交审核失败，请重新刷新后选择提交");
    }

    /**
     * 撤销审核
     *
     * @param reviewDetailIds
     * @return
     */
    @RequiredLog("撤销审核")
    @PostMapping("retreat")
    @ResponseBody
    public SysResult retreat(@RequestParam(value = "reviewDetailIds[]") List<String> reviewDetailIds) {
        boolean result = entryCasReviewDetailService.retreat(reviewDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "撤销审核失败");
    }

    /**
     * 提交修改工作计划完成情况审核评价 （科长，事务类）
     *
     * @param entryCasReviewDetail 回顾履职
     * @return 提交结果
     */
    @PostMapping("sectionUpdate")
    @ResponseBody
    public SysResult SectionUpdate(EntryCasReviewDetail entryCasReviewDetail) {
        boolean result = entryCasReviewDetailService.sectionWorkAudit(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职明细更新失败");
    }

    @PostMapping("batchAudit")
    @ResponseBody
    @RequiredLog("批量审核")
    public SysResult batchAudit(@RequestParam(value = "entryReviewDetailIds[]") List<String> entryReviewDetailIds, HttpServletRequest request) {
        String ministerReview = request.getParameter("ministerReview");
        boolean result = entryCasReviewDetailService.batchAudit(entryReviewDetailIds, ministerReview);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "批量审核失败");
    }

    /**
     * 工作计划完成情况批量审核（科长，事物类）
     */
    @RequestMapping("sectionBatchAudit")
    @ResponseBody
    @RequiredLog("科长事物类批量审核")
    public SysResult sectionBatchAudit(@RequestParam(value = "entryReviewDetailIds[]") List<String> entryReviewDetailIds, HttpServletRequest request) {
        String chargeTransactionEvaluateLevel = request.getParameter("chargeTransactionEvaluateLevel");
        String chargeTransactionBelowType = request.getParameter("chargeTransactionBelowType");
        boolean result = entryCasReviewDetailService.batchAudit(entryReviewDetailIds, chargeTransactionEvaluateLevel, chargeTransactionBelowType);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "批量审核失败");
    }

    /**
     * 创建单条履职回顾
     *
     * @param entryCasReviewDetail 履职回顾
     * @return 结果
     */
    @PostMapping("save")
    @ResponseBody
    public SysResult create(EntryCasReviewDetail entryCasReviewDetail, HttpServletRequest request) {
        boolean result = entryCasReviewDetailService.saveOneReviewDetail(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "创建失败");
    }

    /**
     * 新增回顾-不需要根据计划创建
     *
     * @return 路径
     */
    @GetMapping("chooseEventRole")
    public String createReview(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);

        return "/system/performance/employee/entry-cas-review-detail-create-new";
    }

    @GetMapping("matchReview")
    public String matchReview(Model model){
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        return "/system/performance/employee/event-relation-role-choose-list";
    }


    @PostMapping("createReviewNotPlan")
    @ResponseBody
    public SysResult createReviewNotPlan(EntryCasReviewDetail entryCasReviewDetail) {
        Boolean result = entryCasReviewDetailService.storeReviewNotPlan(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "新增失败");
    }
}
