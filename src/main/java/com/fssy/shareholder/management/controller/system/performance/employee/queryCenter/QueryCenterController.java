/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee.queryCenter;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
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
 * @Description: TODO
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

    /**
     * 查询中心-plan
     */
    @GetMapping("planIndex")
    @RequiredLog("履职计划查询页面")
    public String viewByPlan(Model model) {
        getSelectorData(model);
        return "/system/performance/employee/queryCenter/performance-plan-query";
    }

    /**
     * 用于前端select组件展示数据
     */
    private void getSelectorData(Model model) {
        Map<String, Object> departmentParams = new HashMap<>(50);
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>(50);
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        Map<String, Object> userParams = new HashMap<>(50);
        List<String> selectedUserIds = new ArrayList<>(50);
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
    }

    /**
     * 查询中心-eventRole
     */
    @RequiredLog("岗位关系查询页面")
    @GetMapping("eventRoleIndex")
    public String eventRoleIndex(Model model) {
        getSelectorData(model);
        return "/system/performance/employee/queryCenter/performance-event-relation-role-query";
    }

    /**
     * 查询中心-review
     */
    @RequiredLog("履职总结查询页面")
    @GetMapping("reviewIndex")
    public String reviewIndex(Model model) {
        getSelectorData(model);
        return "/system/performance/employee/queryCenter/performance-review-query";
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
        return "/system/performance/employee/queryCenter/performance-excellent-query";
    }
}
