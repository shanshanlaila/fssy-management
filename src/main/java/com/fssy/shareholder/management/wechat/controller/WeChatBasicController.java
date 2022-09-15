package com.fssy.shareholder.management.wechat.controller;

import com.fssy.shareholder.management.pojo.common.SysResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author banqunwei
 * @title: WeChatSelectedController
 * @description: 基础配置类
 * @date: 2022/6/20 22:20
 */
@Controller
@RequestMapping("/api/basic/")
public class WeChatBasicController {
    /**
     * 验证sessionid是否有效
     * @param request 请求实体
     * @return
     */
    @PostMapping("check_sessionId")
    @ResponseBody
    public SysResult check_sessionId(HttpServletRequest request) {
        try {
            String sessionid = "";
            if ("true".equals(WebUtils.toHttp(request).getHeader("isWx")) && !ObjectUtils.isEmpty(WebUtils.toHttp(request).getHeader("cookie"))) {
                sessionid = WebUtils.toHttp(request).getHeader("cookie").split("JSESSIONID=")[1];
            }
            if (SecurityUtils.getSubject().getSession().getId().toString().equals(sessionid)) {
                return SysResult.build(200, "sessionid有效");
            } else {
                return SysResult.build(500, "sessionid失效");
            }
        } catch (Exception e) {
            return SysResult.build(500, "sessionid失效");
        }
    }
}
