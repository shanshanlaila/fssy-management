/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-11-29 		添加权限过滤,添加登录用户
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-21 	      在首页添加当前登录用户的岗位信息	
 */
package com.fssy.shareholder.management.controller.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员首页控制器
 *
 * @author Solomon
 */
@Controller
public class ManageIndexController
{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ManageIndexController.class);

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 用户缓存工具类
     */
    @Autowired
    private UserCacheTool userCacheTool;

    @Autowired
    private SystemService systemService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 返回管理员系统页面
     *
     * @param model 视图模型
     * @return
     * @throws Exception
     */
    @RequiresPermissions("manage:index")
    @RequestMapping("/manage/index")
    public String index(Model model) throws Exception
    {
        // 查询所有权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", CommonConstant.PERMISSION_TYPE_FOR_MENU)
                .eq("`system`", CommonConstant.MANAGE_SYSTEM);

        List<Map<String, Object>> allList = permissionMapper
                .selectMaps(queryWrapper);

        // 2021-11-29 添加权限过滤，从ehcache中查询出当前用户的权限
        HashSet<String> stringPermissions = (HashSet<String>) userCacheTool
                .getCurrentUserAuthorizationStringPermmissions();

        // 过滤，只出现拥有权限的菜单
        List<Map<String, Object>> filterList = allList.stream()
                .filter((item) -> stringPermissions.contains(item.get("authorizationName")))
                .collect(Collectors.toList());

        // 获取树形
        LOGGER.debug(filterList.toString());
        List<Map<String, Object>> menutree = IteratorTool.list2TreeList(filterList,
                "id", "parent", "children");
        menutree.sort(
                (data1, data2) -> (Integer.valueOf(data1.get("sort").toString())
                        - Integer.valueOf(data2.get("sort").toString())));

        // 2021-11-29 添加登录用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        List<System> systems = systemService.findSystemDataListByParams(new HashMap<>());

        // 2021-12-01 添加shiro的会话id
        Session session = SecurityUtils.getSubject().getSession();
        model.addAttribute("shiroSessionId", session.getId());
        model.addAttribute("currentUser", user);
        model.addAttribute("menus", menutree);
        model.addAttribute("systems", systems);
        return "manage/manage-homepage";
    }

    /**
     * 跳转至决策系统页面
     */
//    @RequiresPermissions("manage:decisionSystem")
    @RequestMapping("/manage/decisionSystem")
    public ModelAndView decisionSystem()
    {
        // 2021-11-29 添加登录用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        User user1 = userMapper.selectById(user.getId());
        String ssoToken = user1.getSsoToken();
        return new ModelAndView(String.format("redirect:http://192.168.1.244:8080/webroot/decision?ssoToken=%s",ssoToken));
    }

    /**
     * 返回管理员首页
     * 
     * @param model model对象
     * @return
     */
    @RequestMapping("/manage/manageIndex")
    public String manageIndex(Model model)
    {
    	// 2022-01-21 在首页添加当前登录用户的岗位信息
    	model.addAttribute("stationDes", userCacheTool.getStationDescribe());
        return "manage/index";
    }
}
