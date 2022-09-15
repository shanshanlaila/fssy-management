package com.fssy.shareholder.management.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.shiro.HttpClientUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author Solomon
 */
@Controller
public class LoginController
{
    @Autowired
    private DepartmentRoleUserMapper departmentRoleUserMapper;

    /**
     * 组织结构，用户，角色关联表与组织结构表，角色表，用户表视图数据访问实现类
     */
    @Autowired
    private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 返回登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    /**
     * 用户登录操作
     *
     * @param request 请求实体
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public SysResult doLogin(HttpServletRequest request) {
        // 判断是不是微信请求的登录，微信请求的登录是否成功
        boolean isWX = false;
        String openid = "";
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = null;
        boolean weChatReLogin = false;
        if (!ObjectUtils.isEmpty("isWeChatReLogin") && "true".equals(request.getHeader("isWX"))) {
            if ("true".equals(request.getParameter("isWeChatReLogin"))) {
                weChatReLogin = true;
                if (!ObjectUtils.isEmpty(request.getParameter("code"))) {
                    openid = getSessionKey(String.valueOf(request.getParameter("code")));
                    UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
                    userUpdateWrapper.eq("openId", openid).set("openId", "");
                    userMapper.update(null, userUpdateWrapper);
                }
            }
        }

        // 获取openid登录微信小程序，条件：1、请求头包含isWX并且值为true，2、包含从前端返回的code, 3、不是重新登录（切换账号）
        if ("true".equals(request.getHeader("isWX")) && !ObjectUtils.isEmpty(request.getParameter("code")) && !weChatReLogin) {
            // 获取openid
            openid = getSessionKey(String.valueOf(request.getParameter("code")));
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("openId", openid);
            List<User> users = userMapper.selectList(userQueryWrapper);
            if (!ObjectUtils.isEmpty(users)) {
                // 设置微信请求的登录成功
                isWX = true;
                // 获取shiro的主体
                // 创建用户令牌，注意区分标识，这里是wx，表示是微信小程序登录的
                token = new UsernamePasswordToken(users.get(0).getName(), users.get(0).getPassword(), false, "wx");
            }
        }

        // 如果微信请求的登录是不成功的，就验证前端传回的用户名和密码
        if (!isWX) {
            String userName = String.valueOf(request.getParameter("name"));
            String password = String.valueOf(request.getParameter("password"));
            // 获取shiro的主体
            // 创建用户令牌，注意区分标识，这里是normal，表示是正常请求登录的
            token = new UsernamePasswordToken(userName, password, false, "noraml");
        }

        try
        {
            // 密码认证
            subject.login(token);
        } catch (UnknownAccountException e)
        {
            throw new UnknownAccountException("登录的账号不存在");
        } catch (IncorrectCredentialsException e)
        {
            throw new IncorrectCredentialsException("登录密码错误");
        } catch (LockedAccountException e)
        {
            throw new LockedAccountException("登录的账号已经被锁定，需要解锁");
        } catch (ExpiredCredentialsException e)
        {
            throw new ExpiredCredentialsException("密码已经过期");
        } catch (DisabledAccountException e)
        {
            throw new DisabledAccountException("登录账号已被禁止使用");
        } catch (ExcessiveAttemptsException e)
        {
            throw new ExcessiveAttemptsException("登录次数超过限制");
        } catch (Exception e)
        {
            throw e;
        }
        // 设置session
        // 将用户保存到session中，移到realm中
        // 2021-9-26登录认证用了缓存，放在realm中就不能再保存到session了，需要放在这
        SecurityUtils.getSubject().getSession().setAttribute("currentUser",
                subject.getPrincipal());
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        // 2021-12-11 判断跳转的系统
        Integer judgeSystem = 1;

        // 如果是微信小程序的，就把openid写到数据库里
        // 判断条件：1、请求头包含isWX并且值为true，2、微信请求的登录不成功（也就是用用户名和密码登录，也就是后端没有openid），3、openid不为空
        if ("true".equals(request.getHeader("isWX")) && !isWX && !openid.equals("")) {
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("id", user.getId()).set("openId", openid);
            userMapper.update(null, userUpdateWrapper);
        }


        // 查询登录用户的角色和组织信息
        QueryWrapper<ViewDepartmentRoleUser> userRelationQueryWrapper = new QueryWrapper<>();
        userRelationQueryWrapper.eq("userId", user.getId());
        List<ViewDepartmentRoleUser> userRelationList = viewDepartmentRoleUserMapper
                .selectList(userRelationQueryWrapper);
        // 保存到session
        SecurityUtils.getSubject().getSession().setAttribute("currentUserRelation",
                userRelationList);


        for (ViewDepartmentRoleUser viewDepartmentRoleUser : userRelationList)
        {
            // 查找用户当前的角色是否为供应商
            if (CommonConstant.ROLE_SUPPLIER == viewDepartmentRoleUser.getRoleId().intValue())
            {
                judgeSystem = 3;
                break;
            }
            // 查找用户当前的角色，是否为管理员
            else if (1 == viewDepartmentRoleUser.getRoleId().intValue())
            {
                judgeSystem = 1;
                break;
            }
            else
            {
                judgeSystem = 2;
            }
        }

        // 返回微信小程序
        if ("true".equals(request.getHeader("isWX"))) {
            Map<String, Object> result = new HashMap<>();
            result.put("sessionid", SecurityUtils.getSubject().getSession().getId().toString());
            result.put("userName", user.getName());
            return SysResult.build(200,"操作成功", result);
        }
        return SysResult.build(200, "登录成功，正在跳转，请等待", judgeSystem);
    }

    //获取session
    private String getSessionKey(String code) {
        String appid = "wx9ed4640214c58f8d";
        String AppSecret = "4fac76643cdc00f9f3d0b441ade3c723";
        Map<String,String> map = new HashMap<>();
        map.put("appid",appid);
        map.put("secret",AppSecret);
        map.put("js_code",code);
        map.put("grant_type","authorization_code"); //固定的微信文档里有
        String openid = "";
        try {
            String S = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session",map);//请求微信接口获取openid
            JSONObject jsonObject = JSONObject.parseObject(S);
            openid = jsonObject.get("openid").toString();
        } catch (Exception e) {

        }
        return openid;
    }
}
