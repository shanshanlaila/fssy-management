/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 * 伍坚山           2022-10-14      新增履职计划表的导入导出
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * *****业务部门： 绩效科 *****数据表中文名： 员工履职计划明细 *****数据表名：
 * bs_performance_employee_entry_cas_plan_detail *****数据表作用： 员工每月月初填写的履职情况计划明细
 * *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@Controller
@RequestMapping("/system/entry-cas-plan-detail/")
public class EntryCasPlanDetailController {

    @Autowired
    private EntryCasPlanDetailService entryCasPlanDetailService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntryCasReviewDetailService entryCasReviewDetailService;

    @Autowired
    private RoleService roleService;


    /**
     * 事件评价标准管理页面
     *
     * @return 事件评价标准管理页面
     */
    @GetMapping("index")
    @RequiredLog("履职明细情况计划")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String showEntryCasPlanDetailList(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        Map<String, Object> userParams = new HashMap<>();
        List<String> selectedUserIds = new ArrayList<>();
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams,selectedUserIds);
        model.addAttribute("userList", userList);
        ViewDepartmentRoleUser viewDepartmentRoleUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName",viewDepartmentRoleUser.getDepartmentName());
        return "system/performance/employee/performance-entry-cas-plan-detail-list";
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

        Page<EntryCasPlanDetail> handlersItemPage = entryCasPlanDetailService.findDataListByParams(params);
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
        if (!ObjectUtils.isEmpty(request.getParameter("twoStatus"))) {
            params.put("twoStatus", request.getParameter("twoStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("newStatus"))) {
            params.put("newStatus", request.getParameter("newStatus"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("groupByUserName"))) {
            params.put("groupByUserName", request.getParameter("groupByUserName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("userNameRight"))) {
            params.put("userNameRight", request.getParameter("userNameRight"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("userIds")))
        {
            String userIdsStr = request.getParameter("userIds");
            List<String> userIds = Arrays.asList(userIdsStr.split(","));
            params.put("userIds", userIds);
        }
        return params;
    }

    /**
     * 履职计划管控excel导出
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        // 导出履职计划填报月底回顾
        Map<String, Object> params = getParams(request);
        params.put("select",
                "id,eventsRoleId," +
                        "eventsFirstType," +
                        "jobName," +
                        "workEvents," +
                        "mainOrNext," +
                        "planningWork," +
                        "times,planOutput," +
                        "planStartDate," +
                        "planEndDate," +
                        "departmentName," +
                        "roleName," +
                        "userName," +
                        "applyDate,standardValue"
        );
        List<Map<String, Object>> eventLists = entryCasPlanDetailService.findEntryCasPlanDetailMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "履职计划序号");
        fieldMap.put("eventsRoleId", "事件岗位序号");
        fieldMap.put("eventsFirstType", "事件类型");
        fieldMap.put("jobName", "工作职责");
        fieldMap.put("workEvents", "流程（工作事件）");
        //fieldMap.put("eventsForm", "绩效类型");
        fieldMap.put("standardValue", "事件价值标准分");
        fieldMap.put("departmentName", "部门名称");
        fieldMap.put("roleName", "岗位名称");
        fieldMap.put("userName", "员工姓名");
        fieldMap.put("applyDate", "申报日期");
        fieldMap.put("mainOrNext", "主/次担任");
        fieldMap.put("planningWork", "对应工作事件的计划内容");
        fieldMap.put("times", "频次");
        fieldMap.put("planOutput", "表单（输出内容）");// 来源于自己填写
        fieldMap.put("planStartDate", "计划开始时间");
        fieldMap.put("planEndDate", "计划完成时间");
        fieldMap.put("sjwcsj", "*实际完成时间");
        fieldMap.put("ggwcms", "*工作完成描述");
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(eventLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("导出履职计划填报月底回顾", eventLists, fieldMap, response, strList, null);
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
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(id);
        if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        return "system/performance/employee/performance-entry-cas-plan-detail-edit";
    }

    /**
     * 更新履职明细
     *
     * @param entryCasPlanDetail 履职明细实体
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(EntryCasPlanDetail entryCasPlanDetail) {
        boolean result = entryCasPlanDetailService.updateById(entryCasPlanDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职明细个更新失败");
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
        LambdaUpdateWrapper<EntryCasPlanDetail> entryCasPlanDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        entryCasPlanDetailLambdaUpdateWrapper.eq(EntryCasPlanDetail::getId, id).set(EntryCasPlanDetail::getStatus, "取消");
        boolean result = entryCasPlanDetailService.update(entryCasPlanDetailLambdaUpdateWrapper);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "取消失败");
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
    public SysResult indexStatus(@RequestParam(value = "planDetailIds[]") List<String> planDetailIds) {
        boolean result = entryCasPlanDetailService.submitAudit(planDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "只能选择待提交审核状态的事件清单，提交审核失败，请重新刷新后选择提交");
    }

    /**
     * 撤销审核
     *
     * @param planDetailIds
     * @return
     */
    @RequiredLog("撤销审核")
    @PostMapping("retreat")
    @ResponseBody
    public SysResult retreat(@RequestParam(value = "planDetailIds[]") List<String> planDetailIds) {
        boolean result = entryCasPlanDetailService.retreat(planDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "撤销审核失败");
    }

    /**
     * 审核结果
     *
     * @param planDetailIds
     * @param
     * @return 通过/拒绝
     */
    @PostMapping("affirmStore")
    @ResponseBody
    public SysResult affirm(HttpServletRequest request,
                            @RequestParam(value = "planDetailIds[]") List<String> planDetailIds,
                            @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String event = request.getParameter("event");
        boolean res = entryCasPlanDetailService.affirmStore(planDetailIds, event,auditNotes);
        if (res) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败请检查数据后重试");
    }

    /**
     * 履职计划部长审核管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("MinisterIndex")
    @RequiredLog("履职计划部长审核管理")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String showEntryCasPlanDetailListByMinster(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName",departmentRoleByUser.getDepartmentName());
        return "system/performance/employee/performance-entry-cas-plan-detail-minister-list";
    }

    @GetMapping("index1")
    @RequiredLog("履职计划科长审核管理")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String showEntryCasPlanDetailListBySection_chief(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "system/performance/employee/performance-entry-cas-plan-detail-section-chief-list";
    }

    /**
     * 展示新增单条履职回顾页面
     *
     * @param id 履职明细id
     * @return 修改页面
     */
    @GetMapping("createReview/{id}")
    public String showCreateReview(@PathVariable String id, Model model) {
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(id);
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        return "system/performance/employee/entry-cas-plan-detail-createReview";
    }

    @GetMapping("HRIndex")
    @RequiredLog("人力资源部审核")
    @RequiresPermissions("system:performance:entryCasPlanDetail:HRIndex")
    public String HRIndex(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "system/performance/employee/performance-entry-cas-plan-detail-HR-list";
    }

    /**
     * 判断新增工作流
     *
     * @param planDetailIds 计划ids
     * @param request       请求
     * @return 结果
     */
    @PostMapping("HRAffirmStore")
    @ResponseBody
    public SysResult HRAffirmStore(@RequestParam(value = "planDetailIds[]") List<String> planDetailIds, HttpServletRequest request) {
        String event = request.getParameter("event");
        boolean res = entryCasPlanDetailService.HRAffirmStore(planDetailIds, event);
        if (res) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败请检查数据后重试");
    }

    @GetMapping("SelectIndex")
    @RequiredLog("待选择关联事件清单")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String SelectIndex(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "system/performance/employee/performance-entry-cas-plan-detail-select-list";
    }

    /**
     * 展示选择事件清单页面
     *
     * @param id    履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("SelectEdit/{id}")
    public String showSelectEditPage(@PathVariable String id, Model model) {
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(id);
        if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        return "system/performance/employee/performance-entry-cas-plan-detail-select-edit";
    }

    /**
     * 更新履职明细
     *
     * @param entryCasPlanDetail 履职明细实体
     * @return 结果
     */
    @PostMapping("SelectUpdate")
    @ResponseBody
    public SysResult SelectUpdate(EntryCasPlanDetail entryCasPlanDetail) {
        boolean result = entryCasPlanDetailService.SelectUpdate(entryCasPlanDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "事件清单序号为空提交失败");
    }

    /**
     * 点击选择基础事件展示事件详情页面
     *
     * @return 页面路径
     */
    @GetMapping("AssociateEvents/{id}")
    public String AssociateEvents(@PathVariable String id, Model model) {
        model.addAttribute("planId", id);
        return "system/performance/employee/entry-cas-new-plan-detail";
    }

    /**
     * 新增工作选择关联基础事件
     *
     * @param event  事件
     * @param planId 计划id
     * @return 结果
     */
    @PostMapping("match/{planId}")
    @ResponseBody
    public SysResult match(EventList event, @PathVariable String planId) {
        EntryCasPlanDetail planDetail = entryCasPlanDetailService.getById(planId);
        planDetail.setEventsId(event.getId());
        planDetail.setStatus(PerformanceConstant.ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW);
        planDetail.setNewStatus(PerformanceConstant.EVENT_LIST_STATUS_FINAL);
        boolean result = entryCasPlanDetailService.updateById(planDetail);
        if (result) {
            return SysResult.build(200, "关联基础事件成功");
        } else return SysResult.build(500, "关联基础事件失败");
    }

    /**
     * 转发到待创建关联基础事件
     *
     * @param model 模型
     * @return 路径
     */
    @GetMapping("createEventPlan")
    @RequiredLog("待创建关联基础事件")
    public String createEventPlan(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "system/performance/employee/plan/entry-cas-plan-detail-create-event-list";
    }

    /**
     * 转发到填写事件清单表单
     *
     * @return 路径
     */
    @GetMapping("showEventForm/{id}")
    public String showEventAndRoleForm(@PathVariable Long id, Model model) {
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(id);
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        return "system/performance/employee/plan/plan-create-event-list-form";
    }
    /**
     * 创建单条履职计划
     *
     * @param entryCasPlanDetail 履职计划
     * @return 结果
     */
    @PostMapping("save")
    @ResponseBody
    public SysResult create(EntryCasPlanDetail entryCasPlanDetail, HttpServletRequest request) {
        boolean result = entryCasPlanDetailService.saveOneCasPlanDetail(entryCasPlanDetail,request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "创建失败");
    }
}
