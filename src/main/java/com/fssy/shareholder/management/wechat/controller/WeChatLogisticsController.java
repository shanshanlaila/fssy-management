package com.fssy.shareholder.management.wechat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.wechat.service.WeChatSearchMaterialService;
import com.fssy.shareholder.management.wechat.service.WeChatSearchPlanService;
import com.fssy.shareholder.management.pojo.common.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author banqunwei
 * @title: WeChatLogisticsController
 * @description: 物流管理相关工具类
 * @date: 2022/6/20 22:20
 */
@Controller
@RequestMapping("/api/logistics/")
public class WeChatLogisticsController {

    @Autowired
    private WeChatSearchPlanService weChatSearchPlanService;

    @Autowired
    private WeChatSearchMaterialService weChatSearchMaterialService;

    /**
     * 根据关键词查找客户计划
     *
     * @param request 请求实体
     * @return
     */
    @RequiredLog("微信小程序-根据关键词查找客户计划")
    @PostMapping("findCustomerPlanByKeyWord")
    @ResponseBody
    public SysResult findCustomerPlanByKeyWord(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyWord", request.getParameter("keyWord"));
        params.put("limit", request.getParameter("limit"));
        params.put("page", request.getParameter("page"));
        Page<Map<String, Object>> deliveryPlanCustomerPage = weChatSearchPlanService.findCustomerPlanByKeyWord(params);
        if (deliveryPlanCustomerPage.getTotal() == 0) {
            return SysResult.build(500, "未查出数据");
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("count", deliveryPlanCustomerPage.getTotal());
            result.put("data", deliveryPlanCustomerPage.getRecords());
            return SysResult.build(200, "操作成功", result);
        }
    }

    /**
     * 根据关键词查找实时库存
     *
     * @param request 请求实体
     * @return
     */
    @RequiredLog("微信小程序-根据关键词查找实时库存")
    @PostMapping("findWarehouseInventoryByKeyWord")
    @ResponseBody
    public SysResult findWarehouseInventoryByKeyWord(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyWord", request.getParameter("keyWord"));
        params.put("limit", request.getParameter("limit"));
        params.put("page", request.getParameter("page"));
        if (!ObjectUtils.isEmpty(request.getParameter("typeId"))) {
            if (!request.getParameter("typeId").equals("undefined")) {
                params.put("typeId", request.getParameter("typeId"));
            }
        }
        if (!ObjectUtils.isEmpty(request.getParameter("warehouseId"))) {
            if (!request.getParameter("warehouseId").equals("undefined")) {
                params.put("warehouseId", request.getParameter("warehouseId"));
            }
        }
        Page<Map<String, Object>> warehouseInventoryPage = weChatSearchMaterialService.findWarehouseInventoryByKeyWord(params);
        if (warehouseInventoryPage.getTotal() == 0) {
            return SysResult.build(500, "未查出数据");
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("count", warehouseInventoryPage.getTotal());
            result.put("data", warehouseInventoryPage.getRecords());
            return SysResult.build(200, "操作成功", result);
        }
    }
}
