package com.fssy.shareholder.management.controller.manage.user;

import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.user.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author QinHui
 * @title: SupplierController
 * @description: 供应商工厂账号管理
 * @date 2021/12/11
 */
@Controller
@RequestMapping("/manage/supplier/")
public class SupplierController
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SupplierController.class);


    @Autowired
    private UserService userService;

    /**
     * 返回供应商工厂列表
     */
    @RequiresPermissions("supplier:index")
    @GetMapping("index")
    public String index()
    {
        return "manage/user/supplier-list";
    }

    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request)
    {
        return null;
    }

    /**
     * 内部初始化供应商账号页面
     */
    @RequiresPermissions("supplier:edit")
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model)
    {
        return "manage/user/supplier-edit";
    }

    /**
     * 保存内部初始化供应商账号信息
     *
     * @param request
     * @return
     */
    @PutMapping("update")
    @ResponseBody
    public SysResult update(HttpServletRequest request)
    {
        return SysResult.ok();
    }

    /**
     * 返回修改密码的页面
     * @param request
     * @param model
     * @return
     */
    @GetMapping("editPassword")
    public String editPassword(HttpServletRequest request, Model model)
    {
        String id = request.getParameter("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        User user = userService.findUserDataListByParams(params).get(0);
        model.addAttribute("user", user);
        return "manage/user/supplier-password-edit";
    }

    /**
     * 更新密码
     * @param request
     * @return
     */
    @PutMapping("updatePassword")
    @ResponseBody
    public SysResult updatePassword(HttpServletRequest request)
    {
        return SysResult.build(500, "操作失败，请重新尝试！");

    }
}
