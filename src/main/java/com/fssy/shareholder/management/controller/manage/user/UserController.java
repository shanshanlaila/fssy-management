/**
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-01-20 	      添加从用户列表添加用户的岗位权限
 */
package com.fssy.shareholder.management.controller.manage.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fssy.shareholder.management.annotation.RequiredLog;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.user.UserService;

/**
 * 用户功能控制器
 *
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/user/")
public class UserController
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 返回用户列表页面
     *
     * @return
     */
    @RequiredLog("用户管理")
    @RequiresPermissions("manage:user:index")
    @GetMapping("index")
    public String userIndex()
    {
        return "manage/user/user-list";
    }

    /**
     * 返回用户列表页面数据
     *
     * @param request 请求实体
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getUserDatas(HttpServletRequest request)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        // 获取limit和page
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        if (request.getParameter("active") != null)
        {
            params.put("active", request.getParameter("active"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("key")))
        {
            params.put("key", request.getParameter("key"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("isSupplier")))
        {
            params.put("isSupplier", request.getParameter("isSupplier"));
		}
		// 2022-01-20 添加从用户列表添加用户的岗位权限
		Page<Map<String, Object>> userPage = userService.findUserDataMapListPerPageByParams(params);
		LOGGER.debug(String.valueOf(userPage.getTotal()));
		if (userPage.getTotal() == 0)
		{
			result.put("code", 404);
			result.put("msg", "未查出数据");

		}
		else
		{
			result.put("code", 0);
			result.put("count", userPage.getTotal());
			result.put("data", userPage.getRecords());
		}
		return result;
    }

    /**
     * 返回创建页面
     *
     * @param request 请求实体
     * @return
     */
    @RequiredLog("添加用户")
    @RequiresPermissions("manage:user:create")
    @GetMapping("create")
    public String createView(HttpServletRequest request)
    {
        return "manage/user/user-create";
    }

    /**
     * 创建用户
     *
     * @param user 用户实体
     * @return
     */
    @RequiredLog("添加用户操作")
    @RequiresPermissions("manage:user:store")
    @PostMapping("store")
    @ResponseBody
    public SysResult store(User user)
    {
        User result = userService.saveUser(user);
        LOGGER.info(result.toString());
        return SysResult.ok();
    }

    /**
     * 返回修改页面
     *
     * @param request 请求实体
     * @param model   视图模型
     * @return
     */
    @RequiredLog("修改用户")
    @RequiresPermissions("manage:user:edit")
    @GetMapping("edit")
    public String editView(HttpServletRequest request, Model model)
    {
        // 获取用户id
        String id = request.getParameter("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        // 查询用户信息
        User user = userService.findUserDataListByParams(params).get(0);
        LOGGER.info("user name :" + user.getName());
        model.addAttribute("user", user);
        return "manage/user/user-edit";
    }

    /**
     * 修改用户
     *
     * @param user 用户实体
     * @return
     */
    @RequiredLog("修改用户操作")
    @RequiresPermissions("manage:user:update")
    @PutMapping("update")
    @ResponseBody
    public SysResult update(User user)
    {
        boolean result = userService.updateUser(user);
        if (result)
        {
            return SysResult.ok();
        }
        return SysResult.build(500, "用户没有更新，请检查数据后重新尝试");
    }

    /**
     * 删除用户
     *
     * @param id 用户表主键
     * @return
     */
    @RequiredLog("删除用户操作")
    @RequiresPermissions("manage:user:destroy")
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult destroy(@PathVariable(value = "id") Integer id)
    {
        boolean result = userService.deleteUserById(id);
        if (result)
        {
            return SysResult.ok();
        }
        return SysResult.build(500, "用户没有删除，请确认操作后重新尝试");
    }

    /**
     * 启用或停用用户
     *
     * @param request 请求实体
     * @return
     */
    @RequiredLog("启用停用用户")
    @RequiresPermissions("manage:user:activate")
    @RequestMapping("activate")
    @ResponseBody
    public SysResult activate(HttpServletRequest request)
    {
        int id = Integer.valueOf(request.getParameter("id"));
        int active = Integer.valueOf(request.getParameter("active"));
        String msg = String.valueOf(request.getParameter("msg"));
        boolean result = userService.activateOrInactivateUser(id, active);
        if (result)
        {
            return SysResult.build(200, String.format("用户%s成功", msg));
        }
        return SysResult.build(500, String.format("用户%s失败，请确认操作后重新尝试", msg));
    }

    /**
     * 个人账户信息查询
     *
     * @param model
     * @return
     */
    @GetMapping("userDetail")
    @RequiredLog("个人账户信息查询")
    public String userDetail(Model model)
    {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String , Object> map = new HashMap<>();
        map.put("id",user.getId());
        List<User> userList =  userService.findUserDataListByParams(map);
        User user1 = new User();
        //因为登陆账户一定有，故不做判断
        user1=userList.get(0);
        model.addAttribute("user", user1);
        return "manage/user/user-detail";
    }

    /**
     * 密码修改(个人信息)
     *
     * @param model
     * @return
     */
    @RequiredLog("密码修改")
    @RequiresPermissions("manage:user:password:edit")
    @GetMapping("userDetailEdit")
    public String userEdit(Model model)
    {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String , Object> map = new HashMap<>();
        map.put("id",user.getId());
        List<User> userList =  userService.findUserDataListByParams(map);
        User user1 = new User();
        //因为登陆账户一定有，故不做判断
        user1=userList.get(0);
        model.addAttribute("user", user1);
        return "manage/user/user-detail-edit";
    }

    /**
     * 仅能修改个人信息
     *
     * @return
     */
    @RequiredLog("修改密码操作")
    @RequiresPermissions("manage:user:password:post")
    @PostMapping("userDetailPost")
    @ResponseBody
    public SysResult userDetailPost(HttpServletRequest request)
    {
        boolean result = userService.saveUserEdit(request);
        if (result)
        {
            request.getSession().removeAttribute("username");
            request.getSession().invalidate();
            return SysResult.build(200, "修改成功,请重新登陆");
        }
        return SysResult.build(500, "修改失败,请检查数据后重新提交");
    }

}
