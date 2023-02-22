package com.fssy.shareholder.management.controller.system.company;


import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.PositionDepartmentCompany;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.company.PositionDepartmentCompanyService;
import com.fssy.shareholder.management.tools.common.GetTool;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
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
    /**
     * 返回新增页面
     *
     * @param request 请求前端
     * @return 展示页面
     */
    @GetMapping("create")
    public String createFinanceData(HttpServletRequest request, Model model) {
        //1、查询公司列表，用于customerName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
        //查询部门列表，用户列表，职位列表，用xm-select插件
        GetTool.getSelectorData(model);
        return "system/company/position-department-company-create";
    }
    /**
     * 保存positionDepartmentCompany表
     *
     * @param positionDepartmentCompany
     * @return
     */
    @PostMapping("store")
    @RequiredLog("新增")
    @ResponseBody
    public SysResult Store(PositionDepartmentCompany positionDepartmentCompany, HttpServletRequest request) {

        boolean result = positionDepartmentCompanyService.insertPositionDepartmentCompany(positionDepartmentCompany,request);
        if (result) {
            return SysResult.ok();
        } return SysResult.build(500, "新增失败");
    }
}
