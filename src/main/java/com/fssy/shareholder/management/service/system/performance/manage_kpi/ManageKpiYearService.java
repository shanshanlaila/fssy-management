package com.fssy.shareholder.management.service.system.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiYear;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经营管理年度指标项目 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-20
 */
public interface ManageKpiYearService {

    /**
     * 通过查询条件 分页 查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManageKpiYear> findManageKpiYearDataListPerPageByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String, Object> readManageKpiYearDataSource(Attachment attachment);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findManageKpiYearMapDataByParams(Map<String, Object> params);

}
