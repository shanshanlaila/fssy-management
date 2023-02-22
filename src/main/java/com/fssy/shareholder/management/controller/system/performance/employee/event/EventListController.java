/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee.event;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MI
 * @ClassName: HandlersItemController
 * @Description: 事件清单控制器
 * @date 2022/9/27 10:08
 */
@Controller
@RequestMapping("/system/performance/employee/eventsList/")
public class EventListController {
    @Autowired
    private EventListService eventListService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * “事件清单评判标准管理”菜单
     */
    @RequiredLog("事件清单评判标准管理")
    @GetMapping("manage")
    @RequiresPermissions("system:performance:employee:manage")
    public String showEventStatus(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        return "system/performance/employee/performance-event-manage-list";
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
        Map<String, Object> params = GetTool.getParams(request);
        GetTool.getPageDataRes(result, params, request, eventListService);
        return result;
    }

    /**
     * 基础事件清单首页
     */
    @RequiredLog("基础事件清单管理")
    @GetMapping("index")
    @RequiresPermissions("performance:employee:event:without:standard:index")
    public String index(Model model) {
        // 部门
        Map<String, Object> departmentParams = new HashMap<>(20);
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        // 基础事件创建人
        Map<String, Object> userParams = new HashMap<>(50);
        List<String> selectedUserIds = new ArrayList<>(50);
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName", departmentRoleByUser.getDepartmentName());
        return "system/performance/employee/eventList/event-list";
    }

    /**
     * 基础事件清单导出
     */
    @RequiredLog("基础事件清单导出")
    @GetMapping("indexExport")
    @RequiresPermissions("performance:employee:event:without:standard:indexExport")
    public String indexExport(Model model) {
        // 部门
        Map<String, Object> departmentParams = new HashMap<>(20);
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        // 基础事件创建人
        Map<String, Object> userParams = new HashMap<>(50);
        List<String> selectedUserIds = new ArrayList<>(50);
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName", departmentRoleByUser.getDepartmentName());
        return "system/performance/employee/eventList/event-list-export";
    }

    /**
     * 返回修改页面
     */
    @GetMapping("edit")
    //@RequiresPermissions("performance:employee:event:edit")
    public String edit(HttpServletRequest request, Model model) {
        //获取无标准事件内容和清单Id
        String id = request.getParameter("id");
        EventList eventList = eventListService.getById(Long.valueOf(id));
        if (eventList.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能修改取消状态的事件清单");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser(user);
        String userDepartmentName = departmentRoleByUser.getDepartmentName();
        if (!userDepartmentName.equals(eventList.getDepartmentName())) {
            throw new ServiceException("不能操作其他部门的事件");
        }
        model.addAttribute("eventList", eventList);
        return "system/performance/events-list-edit";
    }

    /**
     * 更新保存无标准事件清单
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(EventList eventList) {
        boolean result = eventListService.updateEventList(eventList);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "更新失败，请检查数据后重新提交");
    }

    /**
     * 更新保存修改事件清单评判标准事件清单
     */
    @PostMapping("update1")
    @ResponseBody
    public SysResult update1(EventList eventList) {
        boolean result = eventListService.updateEventList(eventList);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "更新失败，请检查数据后重新提交");
    }

    /**
     * 导出事件清单维护事件岗位配比
     *
     * @param request  请求
     * @param response 响应
     */
    @RequiredLog("导出事件清单维护事件岗位配比")
    @RequiresPermissions("system:performance:employee:event:attachment:importToCompleteRole")
    @GetMapping("downloadToCompleteRole")
    public void downloadToCompleteRole(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = GetTool.getParams(request);
        params.put("select",
                "id," +
                        "eventsType," +
                        "jobName," +
                        "workEvents," +
                        "eventsFirstType," +
                        "standardValue,departmentName");
        List<Map<String, Object>> eventLists = eventListService
                .findEventListMapDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "事件清单序号");// A
        fieldMap.put("jobName", "工作职责");// B
        fieldMap.put("workEvents", "流程（工作事件）");// C
        fieldMap.put("eventsFirstType", "事务类型");// D
        fieldMap.put("standardValue", "事件标准价值");// E
        fieldMap.put("departmentName", "部门名称");// 跨部门 F
        fieldMap.put("proportion", "*占比");// 多主担 G
        fieldMap.put("roleName", "*岗位名称");// 跨岗位 H
        fieldMap.put("isMainOrNext", "*主担/次担"); // I
        fieldMap.put("userName", "*职员名称");// J
        fieldMap.put("activeDate", "*生效日期");// K
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(1, 2, 3, 4, 6, 7);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(eventLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("事件分配岗位表", eventLists, fieldMap, response, strList, null);
    }

    /**
     * 匹配基础事件
     */
    @GetMapping("matchEventList")
    @RequiredLog("匹配基础事件")
    public String showMatchEventList(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName", departmentRoleByUser.getDepartmentName());
        return "system/performance/employee/entry-cas-new-plan-detail-match-event-list";
    }

    /**
     * 返回新增单条基础事件页面
     */
    @GetMapping("create")
    @RequiredLog("返回新增单条基础事件页面")
    public String createEventList(HttpServletRequest request, Model model) {
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        return "system/performance/events-list-create";
    }

    /**
     * 保存eventList表
     */
    @PostMapping("store")
    @RequiredLog("保存新增单条基础事件")
    @ResponseBody
    public SysResult store(EventList eventList, HttpServletRequest request) {
        eventListService.insertEventList(eventList);
        return SysResult.ok();
    }

    /**
     * 显示基础事件详情
     *
     * @param id    基础事件id
     * @param model 数据模型
     * @return 详情页面
     */
    @GetMapping("details/{id}")
    @RequiredLog("显示基础事件详情")
    public String details(@PathVariable Long id, Model model) {
        EventList eventList = eventListService.getById(id);
        model.addAttribute("eventList", eventList);
        return "system/performance/employee/events-list-details";
    }

    /**
     * 属于新增工作流的履职计划创建关联基础事件
     *
     * @param eventList 基础事件
     * @param planId    计划id
     * @return 结果
     */
    @PostMapping("storeOnNewPlan/{planId}")
    @RequiredLog("创建关联事件清单")
    @ResponseBody
    public SysResult storeOnNewPlan(EventList eventList, @PathVariable Long planId) {
        boolean result = eventListService.insertEventByPlan(eventList, planId);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "创建关联基础事件失败");
    }
}
