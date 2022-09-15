package com.fssy.shareholder.management.wechat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * @author banqunwei
 * @title: WeChatSearchMaterialService
 * @description: 基础信息查询相关工具类
 * @date: 2022/6/30 15:59
 */
public interface WeChatSearchMaterialService {

    /**
     * 根据网页提供的类和id查找明细信息
     * @param id
     * @param searchClass
     * @return
     */
    Map<String, Object> findSpecialClassById(String id, String searchClass);

    /**
     * 根据关键词查询实时库存
     * @param params
     * @return
     */
    Page<Map<String, Object>> findWarehouseInventoryByKeyWord(Map<String, Object> params);

}
