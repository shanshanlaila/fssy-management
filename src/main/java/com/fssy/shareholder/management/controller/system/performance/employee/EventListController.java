/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
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

    /**
     * 无标准事件管理页面
     *
     * @return 事件评价标准管理页面
     */
    @GetMapping("index")
    @RequiredLog("无标准事件管理")
    @RequiresPermissions("system:performance:event")
    public String showEventList(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "/system/performance/employee/performance-event-list";
    }

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
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        return "/system/performance/employee/performance-event-manage-list";
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

        Page<EventList> handlersItemPage = eventListService.findDataListByParams(params);
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
     * 事件清单评判标准管理-按钮：导出
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("downloadObjects")
    public void downloadObjects(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        params.put("select",
                "id," +
                        "eventsFirstType," +
                        "jobName," +
                        "workEvents," +
                        "delowStandard," +
                        "middleStandard," +
                        "fineStandard," +
                        "excellentStandard," +
                        "status"
        );
        List<Map<String, Object>> eventLists = eventListService.findEventListMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("id", "事件清单序号");
        fieldMap.put("eventsFirstType", "事件类型");
        fieldMap.put("jobName", "工作职责");
        fieldMap.put("workEvents", "流程（工作事件）");
        fieldMap.put("status", "状态");
        fieldMap.put("delowStandard", "不合格标准");
        fieldMap.put("middleStandard", "中标准");
        fieldMap.put("fineStandard", "良标准");
        fieldMap.put("excellentStandard", "优标准");

        // 创建导出服务
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(eventLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("事件清单标准评价表", eventLists, fieldMap, response, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), null);
    }


    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentName"))) {
            params.put("departmentName", request.getParameter("departmentName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("strategyFunctions"))) {
            params.put("strategyFunctions", request.getParameter("strategyFunctions"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("supportFunctions"))) {
            params.put("supportFunctions", request.getParameter("supportFunctions"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("jobName"))) {
            params.put("jobName", request.getParameter("jobName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workEvents"))) {
            params.put("workEvents", request.getParameter("workEvents"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsType"))) {
            params.put("eventsType", request.getParameter("eventsType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workOutput"))) {
            params.put("workOutput", request.getParameter("workOutput"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsFirstType"))) {
            params.put("eventsFirstType", request.getParameter("eventsFirstType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentId"))) {
            params.put("departmentId", request.getParameter("departmentId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("duration"))) {
            params.put("duration", request.getParameter("duration"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("level"))) {
            params.put("level", request.getParameter("level"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createDate"))) {
            params.put("createDate", request.getParameter("createDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("activeDate"))) {
            params.put("activeDate", request.getParameter("activeDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("performanceForm"))) {
            params.put("performanceForm", request.getParameter("performanceForm"));
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
        if (!ObjectUtils.isEmpty(request.getParameter("listCreateUser"))) {
            params.put("listCreateUser", request.getParameter("listCreateUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("listCreateUserId"))) {
            params.put("listCreateUserId", request.getParameter("listCreateUserId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("valueCreateUser"))) {
            params.put("valueCreateUser", request.getParameter("valueCreateUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("valueCreateUserId"))) {
            params.put("valueCreateUserId", request.getParameter("valueCreateUserId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("valueCreateDate"))) {
            params.put("valueCreateDate", request.getParameter("valueCreateDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("listAttachmentId"))) {
            params.put("listAttachmentId", request.getParameter("listAttachmentId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("valueAttachmentId"))) {
            params.put("valueAttachmentId", request.getParameter("valueAttachmentId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("standardCreateUser"))) {
            params.put("standardCreateUser", request.getParameter("standardCreateUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("standardCreateUserId"))) {
            params.put("standardCreateUserId", request.getParameter("standardCreateUserId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("standardCreateDate"))) {
            params.put("standardCreateDate", request.getParameter("standardCreateDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("standardAttachmentId"))) {
            params.put("standardAttachmentId", request.getParameter("standardAttachmentId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentIds"))) {
            String departmentIds = request.getParameter("departmentIds");
            List<String> departmentIdList = Arrays.asList(departmentIds.split(","));
            params.put("departmentIdList", departmentIdList);
        }
        if (!ObjectUtils.isEmpty(request.getParameter("office"))) {
            params.put("office", request.getParameter("office"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("officeId"))) {
            params.put("officeId", request.getParameter("officeId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("statusWait"))) {
            params.put("statusWait", request.getParameter("statusWait"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("statusCancel"))) {
            params.put("statusCancel", request.getParameter("statusCancel"));
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
        return params;
    }

    /**
     * 返回附件上传页面
     *
     * @param model
     * @return 页面
     */
    @RequiredLog("无标准事件管理")
    @GetMapping("withoutStandardIndex")
    @RequiresPermissions("performance:employee:event:without:standard:index")
    public String withoutStandardIndex(Model model) {
        SimpleDateFormat ssad = new SimpleDateFormat();
        ssad.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = ssad.format(date);
        model.addAttribute("importDateStart", importDateStart);
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName", departmentRoleByUser.getDepartmentName());
        return "system/performance/events-list-list";
    }

    /**
     * 返回修改页面
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    //@RequiresPermissions("performance:employee:event:edit")
    public String edit(HttpServletRequest request, Model model) {
        //获取无标准事件内容和清单Id
        String id = request.getParameter("id");
        EventList eventList = eventListService.getById(Long.valueOf(id));
        if (eventList.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
            throw new ServiceException("不能修改取消状态的事件清单");
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser(user);
        String userDepartmentName = departmentRoleByUser.getDepartmentName();
        if (!userDepartmentName.equals(eventList.getDepartmentName())) {
            throw new ServiceException("不能操作其他部门的事件");
        }
        model.addAttribute("eventList", eventList);//发送数据到前端，eventList对应
        return "/system/performance/events-list-edit";
    }

    /**
     * 更新保存无标准事件清单
     *
     * @param eventList
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(EventList eventList) {
        boolean result = eventListService.updateEventList(eventList);
        if (result)
            return SysResult.ok();
        return SysResult.build(500, "更新失败，请检查数据后重新提交");
    }

    /**
     * 取消无标准事件清单状态变为取消
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult destroy(@PathVariable(value = "id") Integer id) {
        boolean res = eventListService.changeStatus(id);
        if (res)
            return SysResult.ok();
        return SysResult.build(500, "无标准事件内容未删除成功，请确认操作后重新尝试");
    }

    /**
     * 修改事件清单评判标准事件清单
     *
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit1")
    //@RequiresPermissions("performance:employee:event:edit")
    public String edit1(HttpServletRequest request, Model model) {
        //获取无标准事件内容和清单Id
        String id = request.getParameter("id");
        EventList eventList = eventListService.getById(Long.valueOf(id));
        model.addAttribute("eventList", eventList);//发送数据到前端，eventList对应
        return "/system/performance/employee/performance-event-manage-edit";
    }

    /**
     * 更新保存修改事件清单评判标准事件清单
     *
     * @param eventList
     * @return
     */
    @PostMapping("update1")
    @ResponseBody
    public SysResult update1(EventList eventList) {
        boolean result = eventListService.updateEventList(eventList);
        if (result)
            return SysResult.ok();
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
        Map<String, Object> params = getParams(request);
        params.put("select",
                "id," +
                        "eventsType," +
                        "jobName," +
                        "workEvents," +
                        "eventsFirstType," +
                        "standardValue," +
                        "delow," +
                        "middle," +
                        "fine," +
                        "excellent");
        List<Map<String, Object>> eventLists = eventListService
                .findEventListMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "事件清单序号");
        fieldMap.put("jobName", "工作职责");
        fieldMap.put("workEvents", "流程（工作事件）");
        fieldMap.put("eventsFirstType", "事务类型");// D
        fieldMap.put("departmentName", "*部门名称");// 跨部门
        fieldMap.put("roleName", "*岗位名称");// 跨岗位
        fieldMap.put("proportion", "*占比");// 多主担
        fieldMap.put("isMainOrNext", "*主担/次担");
        fieldMap.put("userName", "*职员名称");
        fieldMap.put("standardValue", "事件标准价值");
        fieldMap.put("delow", "不合格价值");
        fieldMap.put("middle", "中价值");
        fieldMap.put("fine", "良价值");
        fieldMap.put("excellent", "优价值");
        fieldMap.put("activeDate", "*生效日期");
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(1, 2, 3, 4, 6, 7);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(eventLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("事件分配岗位表", eventLists, fieldMap, response, strList, null);
    }

    @GetMapping("matchEventList")
    public String showMatchEventList(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);//传到前端去
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("departmentName", departmentRoleByUser.getDepartmentName());
        return "/system/performance/employee/entry-cas-new-plan-detail-match-event-list";
    }

    /**
     * 返回新增单条基础事件页面
     *
     * @param request
     * @return
     */
    @GetMapping("create")
    public String createEventList(HttpServletRequest request, Model model) {
        //1、查询部门列表，用于customerName xm-select插件
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        return "system/performance/events-list-create";
    }

    /**
     * 保存eventList表
     *
     * @param eventList
     * @return
     */
    @PostMapping("store")
    @RequiredLog("保存新增单条基础事件")
    @ResponseBody
    public SysResult Store(EventList eventList, HttpServletRequest request) {
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
    public String details(@PathVariable Long id, Model model) {
        EventList eventList = eventListService.getById(id);
        model.addAttribute("eventList", eventList);
        return "/system/performance/employee/events-list-details";
    }
}
