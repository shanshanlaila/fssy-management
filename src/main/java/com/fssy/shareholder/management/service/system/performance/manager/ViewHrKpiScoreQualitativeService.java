package com.fssy.shareholder.management.service.system.performance.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manager.ViewHrKpiScoreQualitative;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 经理人定性评价汇总表 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
public interface ViewHrKpiScoreQualitativeService extends IService<ViewHrKpiScoreQualitative> {

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ViewHrKpiScoreQualitative> findViewHrKpiScoreQualitativeDataListPerPageByParams(Map<String, Object> params);
}
