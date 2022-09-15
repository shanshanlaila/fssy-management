package com.fssy.shareholder.management.wechat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.wechat.service.WeChatSearchPlanService;
import com.fssy.shareholder.management.pojo.common.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author banqunwei
 * @title: WeChatLogisticsController
 * @description: 生产制造相关工具类
 * @date: 2022/6/20 22:20
 */
@Controller
@RequestMapping("/api/manufacture/")
public class WeChatManufactureController {

    @Autowired
    private WeChatSearchPlanService weChatSearchPlanService;
    
    /**
     * 根据关键词查找生产计划
     *
     * @param request 请求实体
     * @return
     */
    @RequiredLog("微信小程序-根据关键词查找生产计划")
    @PostMapping("findManufacturePlanByKeyWord")
    @ResponseBody
    public SysResult findManufacturePlanByKeyWord(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyWord", request.getParameter("keyWord"));
        params.put("limit", request.getParameter("limit"));
        params.put("page", request.getParameter("page"));
        Page<Map<String, Object>> manufacturePlanNoticePage = weChatSearchPlanService.findManufacturePlanByKeyWord(params);
        if (manufacturePlanNoticePage.getTotal() == 0) {
            return SysResult.build(500, "未查出数据");
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("count", manufacturePlanNoticePage.getTotal());
            result.put("data", manufacturePlanNoticePage.getRecords());
            return SysResult.build(200, "操作成功", result);
        }
    }
}
