package com.fssy.shareholder.management.controller.system.company;


import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.service.system.company.PositionDepartmentCompanyService;
import com.fssy.shareholder.management.tools.common.GetTool;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 基础-员工-职位-部门-公司表	 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2023-02-15
 */
@Controller
@RequestMapping("/system/company/position-department-company")
public class PositionDepartmentCompanyController {
    @Autowired
    private PositionDepartmentCompanyService positionDepartmentCompanyService;
    /**
     * 基础-员工-职位-部门-公司表
     *
     * @param model
     * @return 基础-员工-职位-部门-公司表
     */
    @GetMapping("index")
    @RequiredLog("基础-员工-职位-部门-公司表")
    @RequiresPermissions("system:company:position-department-company:index")
    public String showCompany(Model model) {

        return "system/company/position-department-company-list";
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
        Map<String, Object> params = GetTool.getParams(request);
        GetTool.getPageDataRes(result, params, request, positionDepartmentCompanyService);
        return result;
    }
}
