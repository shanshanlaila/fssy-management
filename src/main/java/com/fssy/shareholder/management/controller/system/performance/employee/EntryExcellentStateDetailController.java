package com.fssy.shareholder.management.controller.system.performance.employee;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryExcellentStateDetail;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryExcellentStateDetailService;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表明细		*****数据表名：	bs_performance_entry_excellent_state_detail		*****数据表作用：	员工月度履职评价为优时，填报的内容		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2022-10-24
 */
@Controller
@RequestMapping("/system/entry-excellent-state-detail/")
public class EntryExcellentStateDetailController {
    @Autowired
    private EntryExcellentStateDetailService entryExcellentStateDetailService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("index")
    @RequiredLog("")
    /*@RequiresPermissions("system:performance:entryExcellentStateDetail")*/
    public String showEntryExcellentStateDetail(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        return "/system/performance/employee/entry-excellent-state-detail-list";
    }

    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObject(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<EntryExcellentStateDetail> entryExcellentStateDetailPage = entryExcellentStateDetailService.findDataListByParams(params);
        if (entryExcellentStateDetailPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", entryExcellentStateDetailPage.getTotal());
            result.put("data", entryExcellentStateDetailPage.getRecords());
        }
        return result;
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("createdAt"))) {
            params.put("createdAt", request.getParameter("createdAt"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("createdId"))) {
            params.put("createdId", request.getParameter("createdId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("createdName"))) {
            params.put("createdName", request.getParameter("createdName"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("updatedAt"))) {
            params.put("updatedAt", request.getParameter("updatedAt"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("updatedId"))) {
            params.put("updatedId", request.getParameter("updatedId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("updatedName"))) {
            params.put("updatedName", request.getParameter("updatedName"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("eventsId"))) {
            params.put("eventsId", request.getParameter("eventsId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("workEvents"))) {
            params.put("workEvents", request.getParameter("workEvents"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("mainUserName"))) {
            params.put("mainUserName", request.getParameter("mainUserName"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("completeDesc"))) {
            params.put("completeDesc", request.getParameter("completeDesc"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("actualCompleteDate"))) {
            params.put("actualCompleteDate", request.getParameter("actualCompleteDate"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("oriTarget"))) {
            params.put("oriTarget", request.getParameter("oriTarget"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("actualTarget"))) {
            params.put("actualTarget", request.getParameter("actualTarget"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("innovative"))) {
            params.put("innovative", request.getParameter("innovative"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("departmentName"))) {
            params.put("departmentName", request.getParameter("departmentName"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("departmentId"))) {
            params.put("departmentId", request.getParameter("departmentId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("createDate"))) {
            params.put("createDate", request.getParameter("createDate"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("createName"))) {
            params.put("createName", request.getParameter("createName"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("createId"))) {
            params.put("createId", request.getParameter("createId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("auditName"))) {
            params.put("auditName", request.getParameter("auditName"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("auditId"))) {
            params.put("auditId", request.getParameter("auditId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("auditDate"))) {
            params.put("auditDate", request.getParameter("auditDate"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("auditNote"))) {
            params.put("auditNote", request.getParameter("auditNote"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("mergeNo"))) {
            params.put("mergeNo", request.getParameter("mergeNo"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("mergeId"))) {
            params.put("mergeId", request.getParameter("mergeId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("casPlanId"))) {
            params.put("casPlanId", request.getParameter("casPlanId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("casReviewId"))) {
            params.put("casReviewId", request.getParameter("casReviewId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("applyDate"))) {
            params.put("applyDate", request.getParameter("applyDate"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("ministerReview"))) {
            params.put("ministerReview", request.getParameter("ministerReview"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("classReview"))) {
            params.put("classReview", request.getParameter("classReview"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("ministerReviewUser"))) {
            params.put("ministerReviewUser", request.getParameter("ministerReviewUser"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("ministerReviewUserId"))) {
            params.put("ministerReviewUserId", request.getParameter("ministerReviewUserId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("ministerReviewDate"))) {
            params.put("ministerReviewDate", request.getParameter("ministerReviewDate"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("classReviewUser"))) {
            params.put("classReviewUser", request.getParameter("classReviewUser"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("classReviewUserId"))) {
            params.put("classReviewUserId", request.getParameter("classReviewUserId"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("classReviewDate"))) {
            params.put("classReviewDate", request.getParameter("classReviewDate"));
        }
        if (ObjectUtils.isEmpty(request.getParameter("nextUserName"))) {
            params.put("nextUserName", request.getParameter("nextUserName"));
        }
        return params;
    }

    /**
     * 撤销审核
     *
     * @param excellentStateDetailIds
     * @return
     */
    @RequiredLog("撤销审核")
    @PostMapping("retreat")
    @ResponseBody
    public SysResult retreat(@RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds) {
        boolean result = entryExcellentStateDetailService.retreat(excellentStateDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "撤销审核失败");
    }
    /**
     * 展示修改页面
     * @param id 履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        EntryExcellentStateDetail entryExcellentStateDetail = entryExcellentStateDetailService.getById(id);
        if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryExcellentStateDetail", entryExcellentStateDetail);
        return "/system/performance/employee/entry-excellent-state-detail-edit";
    }
    /**
     * 修改更新提交评优材料
     *
     * @param entryExcellentStateDetail
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(EntryExcellentStateDetail entryExcellentStateDetail) {
        boolean result = entryExcellentStateDetailService.updateEntryExcellentStateDetail(entryExcellentStateDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职明细更新失败");
    }

}
