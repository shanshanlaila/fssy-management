/*
 * @Title: fssy-management
 * @Description: TODO
 * @author MI
 * @date 2022/9/27 10:08
 * @version
 */
package com.fssy.shareholder.management.controller.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 事件评价标准管理页面
     *
     * @return 事件评价标准管理页面
     */
    @GetMapping("index")
    @RequiredLog("事件评价标准管理")
    @RequiresPermissions("system:performance:event")
    public String showHandlersList() {
        return "/system/performance/employee/performance-event-list";
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
     * excel导出
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        params.put("select", "id,eventsType,jobName,workEvents,delowStandard,middleStandard,fineStandard,excellentStandard");
        List<Map<String, Object>> eventLists = eventListService.findEventListMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "清单表序号");
        fieldMap.put("eventsType", "事件类别");
        fieldMap.put("jobName", "工作职责");
        fieldMap.put("workEvents", "流程（工作事件）");
        fieldMap.put("delowStandard", "不合格");
        fieldMap.put("middleStandard", "中");
        fieldMap.put("fineStandard", "良");
        fieldMap.put("excellentStandard", "优");
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(eventLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("事件清单评价表", eventLists, fieldMap, response, strList, null);
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

        return params;
    }

}