package com.fssy.shareholder.management.wechat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.wechat.service.WeChatSearchPlanService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author banqunwei
 * @title: WeChatSearchPlanServiceImpl
 * @description: 计划查询相关工具类
 * @date: 2022/6/30 16:04
 */
@Service
public class WeChatSearchPlanServiceImpl implements WeChatSearchPlanService {

    @Override
    public Map<String, Object> findSpecialClassById(String id, String searchClass) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> findPurchasePlanByKeyWord(Map<String, Object> params) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> findCustomerPlanByKeyWord(Map<String, Object> params) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> findManufacturePlanByKeyWord(Map<String, Object> params) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> findReceiveBillByKeyWord(Map<String, Object> params) {
        return null;
    }
}
