/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee.queryCenter;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleService;
import com.fssy.shareholder.management.tools.common.GetTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MI
 * @ClassName: QueryCenterController
 * @Description: 查询中心controller
 * @date 2023/2/10 9:51
 */
@Controller
@RequestMapping("/system/queryCenter/")
public class QueryCenterController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventsRelationRoleService eventsRelationRoleService;

    @Autowired
    private EntryCasPlanDetailService entryCasPlanDetailService;

    /**
     * 查询中心-plan
     */
    @GetMapping("planIndex")
    @RequiredLog("履职计划查询页面")
    public String viewByPlan(Model model) {
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        boolean flag = entryCasPlanDetailService.isExistExportData(user);
        model.addAttribute("uerId", user.getId());
        model.addAttribute("flag", flag);
        return "system/performance/employee/queryCenter/performance-plan-query";
    }

    /**
     * 查询中心-eventRole
     */
    @RequiredLog("岗位关系查询页面")
    @GetMapping("eventRoleIndex")
    public String eventRoleIndex(Model model) {
        GetTool.getSelectorData(model);
        // 查出当前登录用户是否存在符合打出的数据
        boolean flag = eventsRelationRoleService.isExistExportData();
        model.addAttribute("isExistExportData", flag);
        return "system/performance/employee/queryCenter/performance-event-relation-role-query";
    }

    /**
     * 查询中心-review
     */
    @RequiredLog("履职总结查询页面")
    @GetMapping("reviewIndex")
    public String reviewIndex(Model model) {
        GetTool.getSelectorData(model);
        return "system/performance/employee/queryCenter/performance-review-query";
    }

    /**
     * 查询中心-excellent
     */
    @RequiredLog("履职评优查询页面")
    @GetMapping("excellentIndex")
    public String excellentIndex(Model model) {
        Map<String, Object> departmentParams = new HashMap<>(50);
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> params = new HashMap<>(10);
        List<Map<String, Object>> userNameList = userService.findUserSelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("userNameList", userNameList);
        return "system/performance/employee/queryCenter/performance-excellent-query";
    }
}
