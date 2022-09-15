package com.fssy.shareholder.management.wechat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.wechat.service.WeChatSearchMaterialService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author banqunwei
 * @title:
 * @description:
 * @date: 2022/7/1 11:08
 */
@Service
public class WeChatSearchMaterialServiceImpl implements WeChatSearchMaterialService {
    @Override
    public Map<String, Object> findSpecialClassById(String id, String searchClass) {
        return null;
    }

    @Override
    public Page<Map<String, Object>> findWarehouseInventoryByKeyWord(Map<String, Object> params) {
        return null;
    }
}
