package com.fssy.shareholder.management.wechat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * @author banqunwei
 * @title: WeChatSearchPlanService
 * @description: 计划查询相关工具类
 * @date: 2022/6/30 15:59
 */
public interface WeChatSearchPlanService {

    /**
     * 根据网页提供的类和id查找明细信息
     * @param id
     * @param searchClass
     * @return
     */
    Map<String, Object> findSpecialClassById(String id, String searchClass);

    /**
     * 根据关键词查询采购计划
     * @param params
     * @return
     */
    Page<Map<String, Object>> findPurchasePlanByKeyWord(Map<String, Object> params);

    /**
     * 根据关键词查询客户计划
     * @param params
     * @return
     */
    Page<Map<String, Object>> findCustomerPlanByKeyWord(Map<String, Object> params);

    /**
     * 根据关键词查询生产计划
     * @param params
     * @return
     */
    Page<Map<String, Object>> findManufacturePlanByKeyWord(Map<String, Object> params);

    /**
     * 根据关键词查询采购收货单
     * @param params
     * @return
     */
    Page<Map<String, Object>> findReceiveBillByKeyWord(Map<String, Object> params);

}
