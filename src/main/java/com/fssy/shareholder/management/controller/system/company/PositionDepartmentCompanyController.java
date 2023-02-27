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
import org.springframework.web.bind.annotation.*;

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
        //1、查询公司列表，用于companyName xm-select插件
        Map<String, Object> companyParams = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(companyParams, new ArrayList<>());
        model.addAttribute("companyNameList", companyNameList);
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
    @RequiresPermissions("system:company:position-department-company:create")
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

    /**
     * 展示修改页面
     *
     * @param id    positionDepartmentCompany id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    @RequiresPermissions("system:company:position-department-company:edit")
    public String showEditPage(@PathVariable String id, Model model) {
        PositionDepartmentCompany positionDepartmentCompany = positionDepartmentCompanyService.getById(id);
        model.addAttribute("positionDepartmentCompany", positionDepartmentCompany);//positionDepartmentCompany传到前端
        return "system/company/position-department-company-edit";
    }

    /**
     * 更新
     *
     * @param positionDepartmentCompany 实体类
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(PositionDepartmentCompany positionDepartmentCompany) {
        boolean result = positionDepartmentCompanyService.updateById(positionDepartmentCompany);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "更新失败");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    @RequiredLog("删除")
    @ResponseBody
    @RequiresPermissions("system:company:position-department-company:delete")
    public SysResult delete( @PathVariable String id) {
        PositionDepartmentCompany ppp =
                positionDepartmentCompanyService.getById(id);
        boolean p = positionDepartmentCompanyService.removeById(ppp);
        if (p){
            return SysResult.ok();
        }
        return SysResult.build(500,"删除失败");
    }
}
