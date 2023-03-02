package com.fssy.shareholder.management.controller.system.performance.employee.review;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.tools.common.GetTool;
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
    private EntryCasReviewDetailService reviewService;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * 履职总结index/履职总结提交审核页面
     */
    @GetMapping("index")
    @RequiredLog("员工履职总结明细")
    @RequiresPermissions("system:performance:entryCasReviewDetail")
    public String showEntryCasReviewDetail(Model model) {
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
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
        Map<String, Object> params = GetTool.getParams(request);
        GetTool.getPageDataRes(result,params,request,reviewService);
        return result;
    }

    /**
     * 展示修改页面
     *
     * @param id    履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    @RequiredLog("展示总结修改页面")
    public String showEditPage(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = reviewService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能修改取消状态下的总结");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "system/performance/employee/performance-entry-cas-review-detail-edit";
    }

    /**
     * 更新单条总结
     *
     * @param entryCasReviewDetail 总结
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    @RequiredLog("更新总结")
    public SysResult update(EntryCasReviewDetail entryCasReviewDetail, HttpServletRequest request) {
        boolean result = reviewService.updateEntryCasReviewDetail(entryCasReviewDetail, request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "总结更新失败");
    }

    /**
     * 取消总结
     *
     * @param id id
     * @return 取消结果
     */
    @PostMapping("cancel/{id}")
    @ResponseBody
    @RequiredLog("取消总结")
    public SysResult cancel(@PathVariable Long id) {
        EntryCasReviewDetail reviewDetail = reviewService.getById(id);
        reviewDetail.setStatus(PerformanceConstant.CANCEL);
        boolean result = reviewService.updateById(reviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "取消失败");
    }

    /**
     * 总结审核-部长
     *
     * @param model
     * @return
     */
    @GetMapping("MinisterIndex")
    @RequiredLog("总结审核-部长页面")
    @RequiresPermissions("system:performance:entryCasReviewDetail:MinisterIndex")
    public String showEntryCasReviewDetailByMinster(Model model) {
        GetTool.getSelectorData(model);
        // 登陆人部门id
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentId", departmentRoleByUser.getDepartmentId());
        return "system/performance/employee/performance-entry-cas-review-detail-minister-list";
    }

    /**
     * 总结审核-科长
     *
     */
    @GetMapping("SectionChiefIndex")
    @RequiredLog("总结审核-科长页面")
    @RequiresPermissions("system:performance:entryCasReviewDetail:SectionChiefIndex")
    public String showEntryCasPlanDetailListBySectionChief(Model model) {
        GetTool.getSelectorData(model);
        // 登陆人科室id
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("officeId", departmentRoleByUser.getOfficeId());
        model.addAttribute("departmentId", departmentRoleByUser.getDepartmentId());
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
        return "system/performance/employee/performance-entry-cas-review-detail-section-chief-list";
    }

    /**
     * 单条总结审核-部长
     *
     * @param id    总结id
     * @param model 模型
     * @return 页面
     */
    @GetMapping("ministerEdit/{id}")
    @RequiredLog("单条总结审核-部长")
    public String showEditPageByMinister(@PathVariable Long id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = reviewService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能审核取消状态下的履职总结");
        }
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        return "system/performance/employee/performance-entry-cas-review-detail-minister-edit";
    }

    /**
     * 单条总结审核-科长
     *
     * @param id    总结id
     * @param model 模型
     * @return 页面
     */
    @GetMapping("chiefEdit/{id}")
    @RequiredLog("单条总结审核-科长")
    public String showEditPageByChief(@PathVariable Long id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = reviewService.getById(id);
        if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能审核取消状态下的履职总结");
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
        boolean result = reviewService.submitAuditForReview(reviewDetailIds);
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
    @RequiredLog("履职总结撤销操作")
    @PostMapping("retreat")
    @ResponseBody
    public SysResult retreat(@RequestParam(value = "reviewDetailIds[]") List<String> reviewDetailIds, HttpServletRequest request) {
        String identification = request.getParameter("identification");
        boolean result = reviewService.retreatForReview(reviewDetailIds, identification);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "操作失败");
    }

    /**
     * 履职总结-科长单条审核
     *
     * @param entryCasReviewDetail 履职总结
     * @return 提交结果
     */
    @PostMapping("sectionAudit")
    @ResponseBody
    @RequiredLog("履职总结-科长单条审核")
    public SysResult sectionAudit(EntryCasReviewDetail entryCasReviewDetail) {
        boolean result = reviewService.sectionAudit(entryCasReviewDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职总结审核失败");
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
    @RequiredLog("部长总结批量审核")
    public SysResult batchAudit(HttpServletRequest request,
                                @RequestParam(value = "entryReviewDetailIds[]") List<String> entryReviewDetailIds,
                                @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String ministerReview = request.getParameter("ministerReview");
        boolean result = reviewService.batchAuditBySection(entryReviewDetailIds, ministerReview, auditNotes);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "批量审核失败");
    }

    /**
     * 科长总结批量审核
     */
    @RequestMapping("sectionBatchAudit")
    @ResponseBody
    @RequiredLog("科长总结批量审核")
    public SysResult sectionBatchAudit(HttpServletRequest request,
                                       @RequestParam(value = "entryReviewDetailIds[]") List<String> entryReviewDetailIds,
                                       @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String chargeTransactionEvaluateLevel = request.getParameter("chargeTransactionEvaluateLevel");
        String chargeTransactionBelowType = request.getParameter("chargeTransactionBelowType");
        boolean result = reviewService.batchAuditByChief(entryReviewDetailIds, chargeTransactionEvaluateLevel, chargeTransactionBelowType, auditNotes);
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
    public SysResult create(EntryCasReviewDetail entryCasReviewDetail) {
        boolean result = reviewService.saveOneReviewDetail(entryCasReviewDetail);
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
    @RequiredLog("跳转创建单条履职总结-不根据计划")
    public String createReview(Model model) {
        return "system/performance/employee/entry-cas-review-detail-create-new";
    }

    /**
     * 双击选择事件岗位配比
     */
    @GetMapping("matchReview")
    public String matchReview(Model model) {
        GetTool.getSelectorData(model);
        return "system/performance/employee/event-relation-role-choose-list";
    }

    /**
     * 跳转创建单条履职回顾-不根据计划
     *
     * @param entryCasReviewDetail 总结
     * @return 操作结果
     */
    @PostMapping("createReviewNotPlan")
    @ResponseBody
    @RequiredLog("跳转创建单条履职回顾-不根据计划")
    public SysResult createReviewNotPlan(EntryCasReviewDetail entryCasReviewDetail) {
        Boolean result = reviewService.storeReviewNotPlan(entryCasReviewDetail);
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
        Map<String, Object> params = GetTool.getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<Map<String, Object>> handlersItemPage = reviewService.findDataListByMapParams(params);
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

    /**
     * 履职总结的导入
     * @param model
     * @return
     */
    @GetMapping("reviewImport")
    public String reviewImport (Model model){
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
        return "system/performance/employee/review/review-import-list";
    }
}
