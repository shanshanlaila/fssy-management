package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiYear;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人年度kpi指标 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-10-26
 */
public interface ManagerKpiYearService extends IService<ManagerKpiYear> {
    /**
     * 通过查询条件 分页 查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManagerKpiYear> findManagerKpiYearDataListPerPageByParams(Map<String, Object> params);

    /**
     * 导入 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readManagerKpiYearDataSource(Attachment attachment);

    /**
     * 导出 通过查询条件查询履职计划map数据
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findManagerKpiYearDataByParams(Map<String, Object> params);
}
