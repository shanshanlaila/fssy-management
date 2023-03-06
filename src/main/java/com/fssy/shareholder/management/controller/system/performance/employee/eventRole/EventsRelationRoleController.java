/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.employee.eventRole;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 事件清单主担岗位关系表 *****数据表名：
 * bs_performance_events_relation_main_role *****数据表作用： 事件清单对应的主担岗位 *****变更记录：
 * 时间 变更人 变更内容 20220915 兰宇铧 初始设计 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-10-10
 */
@Controller
@RequestMapping("/system/performance/employee/events-relation-role/")
public class EventsRelationRoleController {
    /**
     * 事件清单岗位关系功能业务实现类
     */
    @Autowired
    private EventsRelationRoleService eventsRelationRoleService;

    /**
     * 返回事件清单岗位关系管理页面
     *
     * @param model model对象
     */
    @RequiredLog("事件清单岗位关系管理")
    @RequiresPermissions("performance:employee:event:relation:role:index")
    @GetMapping("index")
    public String index(Model model) {
        GetTool.getSelectorData(model);
        // 登陆人科室id
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        model.addAttribute("officeId", departmentRoleByUser.getTheDepartmentId());
        return "system/performance/employee/eventRole/relation-role-list";
    }

    /**
     * 返回事件清单岗位关系管理页面
     *
     * @param model model对象
     */
    @RequiredLog("事件清单岗位关系导出")
    @RequiresPermissions("performance:employee:event:relation:role:indexByExport")
    @GetMapping("indexByExport")
    public String indexByExport(Model model) {
        GetTool.getSelectorData(model);
        User user = GetTool.getUser();
        model.addAttribute("userId", user.getId());
        // 查出当前登录用户是否存在符合导出的数据
        boolean flag = eventsRelationRoleService.isExistExportData();
        model.addAttribute("isExistExportData",flag);
        return "system/performance/employee/eventRole/relation-role-list-export";
    }

    /**
     * 获取数据格式
     *
     * @param request 请求对象
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(20);
        Map<String, Object> params = GetTool.getParams(request);
        GetTool.getPageDataRes(result, params, request, eventsRelationRoleService);
        return result;
    }

    /**
     * excel导出(按钮：导出事件清单填报履职计划)
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("downloadForCharge")
    @RequiredLog("导出事件清单填报履职计划")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        // 导出事件清单填报履职计划
        Map<String, Object> params = GetTool.getParams(request);
        params.put("select",
                "id," +
                        "eventsId," +
                        "jobName," +
                        "workEvents," +
                        "departmentName," +
                        "standardValue," +
                        "eventsFirstType," +
                        "isMainOrNext," +
                        "userName," +
                        "roleName,office"
        );
        List<Map<String, Object>> relationRoleLists = eventsRelationRoleService.findRelationRoleMapDataByParams(params);

        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        // 需要改背景色的格子
        fieldMap.put("id", "事件岗位ID");// A
        fieldMap.put("eventsId", "事件清单ID");// B
        fieldMap.put("departmentName", "部门名称");// C
        fieldMap.put("office", "科室名称");// C
        fieldMap.put("roleName", "岗位名称");// D
        fieldMap.put("userName", "职员名称");// E
        fieldMap.put("eventsFirstType", "事件类型");// F
        fieldMap.put("jobName", "工作职责");// G
        fieldMap.put("workEvents", "流程（工作事件）");// H
        fieldMap.put("standardValue", "事件标准价值");// I
        fieldMap.put("isMainOrNext", "主/次担");// J
        // 需要填写的部分
        fieldMap.put("duiyingjihuaneirong", "*对应工作事件的计划内容");// planningWork K
        fieldMap.put("pinci", "频次");// L
        fieldMap.put("biaodan", "*表单（输出内容）");// planOutput M
        fieldMap.put("jihuakaishishijian", "*计划开始时间");// N
        fieldMap.put("jihuawanchengshijian", "*计划完成时间");// O
        // 标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(relationRoleLists)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("履职管控表", relationRoleLists, fieldMap, response, strList, null);
    }

    /**
     * 展示新增单条履职计划页面
     *
     * @param id 基础事件id
     * @return 展示页面
     */
    @GetMapping("createCasPlan/{id}")
    @RequiredLog("展示新增单条履职计划页面")
    public String showCreateCasPlan(@PathVariable String id, Model model) {
        EventsRelationRole eventsRelationRole = eventsRelationRoleService.getById(id);
        model.addAttribute("eventsRelationRole", eventsRelationRole);
        return "system/performance/employee/events-relation-role-creatCasPlan";
    }
}
