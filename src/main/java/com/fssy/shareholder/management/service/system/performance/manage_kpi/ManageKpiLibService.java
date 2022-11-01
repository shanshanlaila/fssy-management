package com.fssy.shareholder.management.service.system.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经营管理指标库服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-12
 */
public interface ManageKpiLibService {
    /**
     * 通过查询条件分页查询经理人年度KPI指标库列表
     *
     * @param params 查询条件
     * @return 经理人年度KPI指标库分页数据
     */
    Page<ManageKpiLib> findManagerKpiLibDataListPerPageByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 经理绩效附件
     * @return 经理绩效附件
     */

    Map<String, Object> readManagerKpiLibDataSource(Attachment attachment);

    /**
     * 通过查询条件查询列表（Map）
     *
     * @param params 查询条件
     * @return 经营管理指标库数据列表
     */
    List<Map<String, Object>> findManagerKpiLibDataSource(Map<String, Object> params);

}
