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
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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
        return "/system/performance/employee/performance-entry-cas-plan-detail-list";
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
        Map<String, Object> params = getParams(request);
        params.put("select",
                "id," +
                        "eventsType," +
                        "jobName," +
                        "workEvents," +
                        "delowStandard," +
                        "middleStandard," +
                        "fineStandard," +
                        "excellentStandard," +
                        "mainOrNext," +
                        "planningWork," +
                        "times,workOutput," +
                        "planStartDate," +
                        "planEndDate," +
                        "departmentName," +
                        "roleName," +
                        "userName," +
                        "applyDate"
        );
        List<Map<String, Object>> eventLists = entryCasPlanDetailService.findEntryCasPlanDetailMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "事件清单表序号");
        fieldMap.put("eventsType", "事件类别");
        fieldMap.put("jobName", "工作职责");
        fieldMap.put("workEvents", "流程（工作事件）");
        fieldMap.put("delowStandard", "不合格");
        fieldMap.put("middleStandard", "中");
        fieldMap.put("fineStandard", "良");
        fieldMap.put("excellentStandard", "优");
        fieldMap.put("jixiaoleixing", "绩效类型");
        fieldMap.put("shijianjiazhibiaozhunfen", "事件价值标准分");
        fieldMap.put("departmentName", "部门名称");
        fieldMap.put("roleName", "岗位名称");
        fieldMap.put("userName", "员工姓名");
        fieldMap.put("applyDate", "申报日期");
        fieldMap.put("zhudan", "主/次担任");
        fieldMap.put("duiyinggongzuoneirong", "对应工作事件的计划内容");
        fieldMap.put("pinci", "频次");
        fieldMap.put("biaodan", "表单（输出内容）");
        fieldMap.put("jihuakaishishijian", "计划开始时间");
        fieldMap.put("jihuawanchengshijian", "计划完成时间");
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(eventLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("履职管控", eventLists, fieldMap, response, strList, null);
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

}
