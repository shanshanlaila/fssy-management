package com.fssy.shareholder.management.controller.system.operate.company;


import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.company.CompanyProfile;
import com.fssy.shareholder.management.service.system.company.CompanyProfileService;
import com.fssy.shareholder.management.tools.common.GetTool;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 部门：		时间：2022/7/15		表名：公司简介		表名：company_profile		用途：存储公司的基本信息和简介 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2022-12-13
 */
@Controller
@RequestMapping("/system/operate/company/company-profile")
public class CompanyProfileController {
    @Autowired
    private CompanyProfileService companyProfileService;

    /**
     * 企业简介
     *
     * @param model
     * @return 企业简介
     */
    @GetMapping("index")
    @RequiredLog("企业简介")
    @RequiresPermissions("system:operate:company:company-profile:index")
    public String showCompany(Model model) {

        return "/system/company/company-profile-list";
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
        GetTool.getPageDataRes(result, params, request, companyProfileService);
        return result;
    }
    /**
     * 展示企业简介详细页面
     *
     * @param id   企业简介id
     * @param model 数据模型
     * @return 展示页面
     */
    @GetMapping("details/{id}")
    public String showDetailsPage(@PathVariable String id, Model model) {
        CompanyProfile companyProfile = companyProfileService.getById(id);
        model.addAttribute("companyProfile", companyProfile);//companyProfile传到前端
        return "system/company/company-profile-details";
    }
    /**
     * 展示修改页面
     *
     * @param id   企业简介id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        CompanyProfile companyProfile = companyProfileService.getById(id);
        model.addAttribute("companyProfile", companyProfile);//companyProfile传到前端
        return "system/company/company-profile-edit";
    }
    /**
     * 更新公司简介
     *
     * @param companyProfile 公司简介
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(CompanyProfile companyProfile) {
        boolean result = companyProfileService.updateById(companyProfile);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "公司简介更新失败");
    }
    /**
     * 返回新增公司简介页面
     *
     * @param request 请求前端
     * @return 展示页面
     */
    @GetMapping("create")
    public String createFinanceData(HttpServletRequest request, Model model) {
        return "system/company/company-profile-create";
    }
    /**
     * 保存companyProfile表
     *
     * @param companyProfile
     * @return
     */
    @PostMapping("store")
    @RequiredLog("新增单条企业财务基础数据")
    @ResponseBody
    public SysResult Store(CompanyProfile companyProfile, HttpServletRequest request) {
       boolean result = companyProfileService.insertCompanyProfile(companyProfile);
        if (result) {
            return SysResult.ok();
        } return SysResult.build(500, "公司简介新增失败");
    }
}
