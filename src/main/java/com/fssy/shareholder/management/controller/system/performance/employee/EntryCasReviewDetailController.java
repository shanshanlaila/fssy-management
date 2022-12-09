package com.fssy.shareholder.management.controller.system.performance.employee;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
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

    @Autowired
    private UserService userService;

    /**
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiredLog("员工履职回顾明细")
    @RequiresPermissions("system:performance:entryCasReviewDetail")
    public String showEntryCasReviewDetail(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        Map<String, Object> userParams = new HashMap<>();
        List<String> selectedUserIds = new ArrayList<>();
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        return "system/performance/employee/performance-entry-cas-review-detail-list";
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
        // 审核页面，如果左侧没有数据或者没有点击，右侧显示未查出数据
        String isEmpty = request.getParameter("isEmpty");
        if (!ObjectUtils.isEmpty(isEmpty)) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
            return result;
        }
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
        if (!ObjectUtils.isEmpty(request.getParameter("groupByUserName"))) {
            params.put("groupByUserName", request.getParameter("groupByUserName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("userNameRight"))) {
            params.put("userNameRight", request.getParameter("userNameRight"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("userIds"))) {
            String userIdsStr = request.getParameter("userIds");
            List<String> userIds = Arrays.asList(userIdsStr.split(","));
            params.put("userIds", userIds);
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
    @RequiredLog("展示回顾修改页面")
    public String showEditPage(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能修改取消状态下的回顾");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "system/performance/employee/performance-entry-cas-review-detail-edit";
    }

    /**
     * 更新单条回顾
     *
     * @param entryCasReviewDetail 回顾
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    @RequiredLog("更新回顾")
    public SysResult update(EntryCasReviewDetail entryCasReviewDetail) {
        boolean result = entryCasReviewDetailService.updateEntryCasReviewDetail(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "回顾审核失败");
    }

    /**
     * 取消回顾
     *
     * @param id id
     * @return 取消结果
     */
    @PostMapping("cancel/{id}")
    @ResponseBody
    @RequiredLog("取消回顾")
    public SysResult cancel(@PathVariable Long id) {
        EntryCasReviewDetail reviewDetail = entryCasReviewDetailService.getById(id);
        reviewDetail.setStatus(PerformanceConstant.CANCEL);
        boolean result = entryCasReviewDetailService.updateById(reviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "取消失败");
    }

    /**
     * 回顾审核-部长
     *
     * @param model
     * @return
     */
    @GetMapping("MinisterIndex")
    @RequiredLog("回顾审核-部长页面")
    @RequiresPermissions("system:performance:entryCasReviewDetail:MinisterIndex")
    public String showEntryCasReviewDetailByMinster(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        Map<String, Object> userParams = new HashMap<>();
        List<String> selectedUserIds = new ArrayList<>();
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        return "system/performance/employee/performance-entry-cas-review-detail-minister-list";
    }

    /**
     * 回顾审核-科长
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiredLog("回顾审核-科长页面")
    @RequiresPermissions("system:performance:entryCasPlanDetail:index1")
    public String showEntryCasPlanDetailListBySection_chief(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        Map<String, Object> userParams = new HashMap<>();
        List<String> selectedUserIds = new ArrayList<>();
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        return "system/performance/employee/performance-entry-cas-review-detail-section-chief-list";
    }

    /**
     * 单条回顾审核-部长
     *
     * @param id    回顾id
     * @param model 模型
     * @return 页面
     */
    @GetMapping("MinisterEdit/{id}")
    @RequiredLog("单条回顾审核-部长")
    public String showEditPageByMinister(@PathVariable Long id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能审核取消状态下的事件请单");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "system/performance/employee/performance-entry-cas-review-detail-minister-edit";
    }

    /**
     * 工作计划完成情况审核（科长）-修改按钮
     *
     * @param id id
     * @return 路径
     */
    @GetMapping("sectionEdit/{id}")
    @RequiredLog("单条回顾审核-科长")
    public String showEditPageBySection(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "system/performance/employee/performance-entry-cas-review-detail-section-chief-edit";
    }

    /**
     * 筛选状态-提交审核
     *
     * @return 结果
     */
    @RequiredLog("回顾提交审核")
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
    @RequiredLog("回顾撤销审核")
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
    @RequiredLog("提交修改工作计划完成情况审核评价 （科长，事务类）")
    public SysResult SectionUpdate(EntryCasReviewDetail entryCasReviewDetail) {
        boolean result = entryCasReviewDetailService.sectionWorkAudit(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职明细更新失败");
    }

    /**
     * 回顾审核-部长
     *
     * @param entryReviewDetailIds 回顾的ids
     * @param request              请求
     * @return 结果
     */
    @PostMapping("batchAudit")
    @ResponseBody
    @RequiredLog("部长回顾批量审核")
    public SysResult batchAudit(HttpServletRequest request,
                                @RequestParam(value = "entryReviewDetailIds[]") List<String> entryReviewDetailIds,
                                @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String ministerReview = request.getParameter("ministerReview");
        boolean result = entryCasReviewDetailService.batchAudit(entryReviewDetailIds, ministerReview, auditNotes);
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
    @RequiredLog("科长回顾批量审核")
    public SysResult sectionBatchAudit(HttpServletRequest request,
                                       @RequestParam(value = "entryReviewDetailIds[]") List<String> entryReviewDetailIds,
                                       @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String chargeTransactionEvaluateLevel = request.getParameter("chargeTransactionEvaluateLevel");
        String chargeTransactionBelowType = request.getParameter("chargeTransactionBelowType");
        boolean result = entryCasReviewDetailService.batchAudit(entryReviewDetailIds, chargeTransactionEvaluateLevel, chargeTransactionBelowType, auditNotes);
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
    @RequiredLog("创建单条履职回顾")
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
    @RequiredLog("跳转创建单条履职回顾-不根据计划")
    public String createReview(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);

        return "system/performance/employee/entry-cas-review-detail-create-new";
    }

    /**
     * 双击选择事件岗位配比
     *
     * @param model
     * @return
     */
    @GetMapping("matchReview")
    public String matchReview(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        return "system/performance/employee/event-relation-role-choose-list";
    }


    @PostMapping("createReviewNotPlan")
    @ResponseBody
    @RequiredLog("跳转创建单条履职回顾-不根据计划")
    public SysResult createReviewNotPlan(EntryCasReviewDetail entryCasReviewDetail) {
        Boolean result = entryCasReviewDetailService.storeReviewNotPlan(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "新增失败");
    }

    /**
     * 返回 对象列表
     *
     * @param request 前端请求参数
     * @return Map集合
     */
    @RequestMapping("getObjectsByMap")
    @ResponseBody
    public Map<String, Object> getObjectsByMap(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<Map<String, Object>> handlersItemPage = entryCasReviewDetailService.findDataListByMapParams(params);
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
}
