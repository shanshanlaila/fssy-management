/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 * 伍坚山           2022-10-14      新增履职计划表的导入导出
 */
package com.fssy.shareholder.management.controller.system.performance.employee.plan;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
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
     * 计划页面
     */
    @GetMapping("index")
    @RequiredLog("履职明细情况计划")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String showEntryCasPlanDetailList(Model model) {
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
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
        Map<String, Object> result = new HashMap<>(20);
        // 审核页面，如果左侧没有数据或者没有点击，右侧显示未查出数据
        String isEmpty = request.getParameter("isEmpty");
        if (!ObjectUtils.isEmpty(isEmpty)) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
            return result;
        }
        Map<String, Object> params = GetTool.getParams(request);
        GetTool.getPageDataRes(result, params, request, entryCasPlanDetailService);
        return result;
    }

    /**
     * 履职计划管控excel导出
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("downloadForCharge")
    @RequiredLog("导出履职计划填报月底总结")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        // 导出履职计划填报月底总结
        Map<String, Object> params = GetTool.getParams(request);
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
                        "applyDate,standardValue,office"
        );
        List<Map<String, Object>> eventLists = entryCasPlanDetailService.findEntryCasPlanDetailMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "履职计划序号");
        fieldMap.put("eventsRoleId", "事件岗位序号");
        fieldMap.put("departmentName", "部门名称");
        fieldMap.put("office", "科室名称");
        fieldMap.put("roleName", "岗位名称");
        fieldMap.put("userName", "职员名称");
        fieldMap.put("eventsFirstType", "事件类型");
        fieldMap.put("jobName", "工作职责");
        fieldMap.put("workEvents", "流程（工作事件）");
        fieldMap.put("standardValue", "事件标准价值");
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
        sheetOutputService.exportNum("导出履职计划填报月底总结", eventLists, fieldMap, response, strList, null);
    }

    /**
     * 展示修改页面
     *
     * @param id    履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    @RequiredLog("点击计划修改按钮")
    public String showEditPage(@PathVariable String id, Model model) {
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(id);
        if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
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
    @RequiredLog("计划修改")
    public SysResult update(EntryCasPlanDetail entryCasPlanDetail) {
        boolean result = entryCasPlanDetailService.updateById(entryCasPlanDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "更新失败");
    }

    /**
     * 取消履职明细
     *
     * @param id id
     * @return 取消结果
     */
    @PostMapping("cancel/{id}")
    @ResponseBody
    @RequiredLog("计划取消")
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
    @RequiresPermissions("system:performance:entryCasPlanDetail:changeStatus")
    @PostMapping("changeStatus")
    @ResponseBody
    public SysResult changeStatus(@RequestParam(value = "planDetailIds[]") List<String> planDetailIds) {
        boolean result = entryCasPlanDetailService.submitAuditForPlan(planDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "只能选择待提交审核状态的事件清单，提交审核失败，请重新刷新后选择提交");
    }

    /**
     * 撤销审核
     *
     * @param planDetailIds 计划ids
     * @return 撤销结果
     */
    @RequiredLog("撤销审核")
    @PostMapping("retreat")
    @ResponseBody
    public SysResult retreat(@RequestParam(value = "planDetailIds[]") List<String> planDetailIds) {
        boolean result = entryCasPlanDetailService.retreatForPlan(planDetailIds);
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
    @RequiredLog("计划提交审核")
    public SysResult affirm(HttpServletRequest request,
                            @RequestParam(value = "planDetailIds[]") List<String> planDetailIds,
                            @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String event = request.getParameter("event");// 审核结果标识
        boolean res = entryCasPlanDetailService.affirmStore(planDetailIds, event, auditNotes);
        if (res) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败请检查数据后重试");
    }


    @GetMapping("SectionChiefIndex")
    @RequiredLog("履职计划科长审核")
    @RequiresPermissions("system:performance:entryCasPlanDetail:SectionChiefIndex")
    public String showEntryCasPlanDetailListBySectionChief(Model model) {
        GetTool.getSelectorData(model);
        // 登陆人科室id
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("officeId", departmentRoleByUser.getTheDepartmentId());
        return "system/performance/employee/performance-entry-cas-plan-detail-section-chief-list";
    }

    /**
     * 展示新增单条履职总结页面
     *
     * @param id 履职明细id
     * @return 修改页面
     */
    @GetMapping("createReview/{id}")
    public String showCreateReview(@PathVariable String id, Model model) {
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(id);
        if (ObjectUtils.isEmpty(entryCasPlanDetail)) {
            throw new ServiceException("不存在该计划");
        }
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        return "system/performance/employee/plan/entry-cas-plan-detail-createReview";
    }

    @GetMapping("SelectIndex")
    @RequiredLog("待选择关联事件清单")
    @RequiresPermissions("system:performance:entryCasPlanDetail")
    public String selectIndex(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
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
        if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        return "system/performance/employee/performance-entry-cas-plan-detail-select-edit";
    }

    /**
     * 点击选择基础事件展示事件详情页面
     *
     * @return 页面路径
     */
    @GetMapping("AssociateEvents/{id}")
    public String associateEvents(@PathVariable String id, Model model) {
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
        planDetail.setStatus(PerformanceConstant.WAIT_WRITE_REVIEW);
        planDetail.setNewStatus(PerformanceConstant.FINAL);
        boolean result = entryCasPlanDetailService.updateById(planDetail);
        if (result) {
            return SysResult.build(200, "关联基础事件成功");
        } else {
            return SysResult.build(500, "关联基础事件失败");
        }
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
        model.addAttribute("roleNameList", roleNameList);
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
    @RequiredLog("创建单条计划")
    public SysResult create(EntryCasPlanDetail entryCasPlanDetail, HttpServletRequest request) {
        boolean result = entryCasPlanDetailService.saveOneCasPlanDetail(entryCasPlanDetail, request);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "创建失败");
    }

    @RequestMapping("getObjectsByMap")
    @ResponseBody
    public Map<String, Object> getObjectsByMap(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(10);
        Map<String, Object> params = GetTool.getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<Map<String, Object>> handlersItemPage = entryCasPlanDetailService.findDataListByMapParams(params);
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
     * 转发到计划导入页面
     */
    @GetMapping("planImport")
    @RequiredLog("计划导入")
    public String planImport(Model model) {
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
        return "system/performance/employee/plan/plan-import-list";
    }

    /**
     * 转发到计划导出页面
     */
    @GetMapping("planExport")
    @RequiredLog("计划导出")
    public String planExport(Model model) {
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
        boolean flag = entryCasPlanDetailService.isExistExportData(user);
        model.addAttribute("flag", flag);
        return "system/performance/employee/plan/plan-export-list";
    }

    /**
     * 转发到新增工作流表单
     *
     * @return 路径
     */
    @GetMapping("createNewEve")
    @RequiredLog("点击创建新增工作流按钮")
    public String createNewEve(Model model) {
        return "system/performance/employee/plan/plan-new-event";
    }

    /**
     * 保存新增工作流
     * @param planDetail
     * @return
     */
    @PostMapping("saveNewEve")
    @ResponseBody
    public SysResult saveNewEve(EntryCasPlanDetail planDetail) {
        boolean result = entryCasPlanDetailService.saveNewEve(planDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "新增计划失败");
    }

}
