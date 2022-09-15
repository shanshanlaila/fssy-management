/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-12-24  	      添加首页的权限，让权限缓存能最新保存
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-06 	      添加shiro的会话id
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-21 	      在首页添加当前登录用户的岗位信息
 */
package com.fssy.shareholder.management.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.pojo.manage.system.System;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.system.SystemService;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.UserCacheTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author QinHui
 * @title: HomepageController
 * @description: 业务首页控制器
 * @date 2021/12/1
 */
@Controller
public class HomepageController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HomepageController.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserCacheTool userCacheTool;

    @Autowired
    private SystemService systemService;

    /**
     * 返回业务系统页面
     *
     * @param model
     * @return
     */
    @RequiresPermissions("system:home:index")
    @RequestMapping("/system/index")
    public String index(Model model) throws Exception
    {
    	// 2021-12-24 添加首页的权限，让权限缓存能最新保存
        // 查询所有权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", CommonConstant.PERMISSION_TYPE_FOR_MENU)
                .eq("`system`", CommonConstant.BUSINESS_SYSTEM);
        List<Map<String, Object>> allList = permissionMapper
                .selectMaps(queryWrapper);

        HashSet<String> stringPermissions = (HashSet<String>) userCacheTool
                .getCurrentUserAuthorizationStringPermmissions();
//
//        // 过滤，只出现拥有权限的菜单
        List<Map<String, Object>> filterList = allList.stream()
                .filter((item) -> stringPermissions.contains(item.get("authorizationName")))
                .collect(Collectors.toList());

        // 获取树形
        LOGGER.debug(filterList.toString());
        List<Map<String, Object>> menuTree = IteratorTool.list2TreeList(filterList, "id", "parent", "children");
        menuTree.sort(
                (data1, data2) -> (Integer.valueOf(data1.get("sort").toString())
                        - Integer.valueOf(data2.get("sort").toString()))
        );
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        List<System> systemList = systemService.findSystemDataListByParams(new HashMap<>());

        // 2022-01-06 添加shiro的会话id
        Session session = SecurityUtils.getSubject().getSession();
        model.addAttribute("shiroSessionId", session.getId());
        model.addAttribute("currentUser", user);
        model.addAttribute("menus", menuTree);
        model.addAttribute("systems", systemList);
        return "system/homepage";
    }

	/**
	 * 返回业务系统首页
	 * 
	 * @param model model对象
	 * @return
	 */
	@RequestMapping("/system/businessIndex")
	public String businessIndex(Model model)
	{
		// 2022-01-21 在首页添加当前登录用户的岗位信息
		model.addAttribute("stationDes", userCacheTool.getStationDescribe());
		return "system/index";
	}
}
