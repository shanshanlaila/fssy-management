/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-12-24  	      添加首页的权限，让权限缓存能最新保存
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-06 	      添加shiro的会话id
 */
package com.fssy.shareholder.management.controller.system;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.UserCacheTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;

/**
 * @author QinHui
 * @title: SupplyHomepageController
 * @description: 供应商系统首页控制器
 * @date 2021/12/11
 */
@Controller
public class SupplierHomepageController
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SupplierHomepageController.class);
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserCacheTool userCacheTool;

    /**
     * 返回供应商系统首页
     *
     * @param model
     * @return
     */
    @RequiresPermissions("supplier:home:index")
    @RequestMapping("/system/supplierHomepage")
    public String index(Model model)
    {
    	// 2021-12-24 添加首页的权限，让权限缓存能最新保存
        // 查询所有权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", CommonConstant.PERMISSION_TYPE_FOR_MENU)
                .eq("`system`", CommonConstant.SUPPLIER_SYSTEM);
        List<Map<String, Object>> allList = permissionMapper.selectMaps(queryWrapper);

        HashSet<String> stringPermissions = (HashSet<String>) userCacheTool
                .getCurrentUserAuthorizationStringPermmissions();

        List<Map<String, Object>> filterList = allList.stream()
                .filter(item -> stringPermissions.contains(item.get("authorizationName")))
                .collect(Collectors.toList());
        LOGGER.debug(filterList.toString());
        List<Map<String, Object>> menuTree = IteratorTool.list2TreeList(filterList, "id", "parent", "children");
        menuTree.sort(
                (data1, data2) -> (Integer.valueOf(data1.get("sort").toString())
                        - Integer.valueOf(data2.get("sort").toString()))
        );
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        
        // 2022-01-06 添加shiro的会话id
        Session session = SecurityUtils.getSubject().getSession();
        model.addAttribute("shiroSessionId", session.getId());
        model.addAttribute("currentUser", user);
        model.addAttribute("menus", menuTree);
        return "system/supplier-homepage";
    }

    /**
     * 返回供应商系统首页iframe
     *
     * @return
     */
    @RequestMapping("/system/supplierIndex")
    public String supplyIndex()
    {
        return "system/supplier-index";
    }
}
