package com.fssy.shareholder.management.controller.system.company;


import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.service.system.company.BS_UserService;
import com.fssy.shareholder.management.tools.common.GetTool;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 基础-员工表	 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2023-02-16
 */
@Controller
@RequestMapping("/system/company/user")
public class BS_UserController {
    @Autowired
    private BS_UserService bs_userService;

    /**
     * 基础-员工表
     * @param model
     * @return 基础-员工表
     */
    @GetMapping("index")
    @RequiredLog("基础-员工表")
    @RequiresPermissions("system:company:user:index")
    public String BS_User(Model model) {
        return "system/company/bs-user-list";
    }

    /**
     * 返回对象列表
     * @param request 前端请求参数
     * @return Map集合
     */
    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String,Object> getObjects(HttpServletRequest request) {
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> params = GetTool.getParams(request);
        GetTool.getPageDataRes(result,params,request,bs_userService);
        return result;
    }
}
