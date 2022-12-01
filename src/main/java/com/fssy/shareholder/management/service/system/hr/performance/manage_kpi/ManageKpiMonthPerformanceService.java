package com.fssy.shareholder.management.service.system.hr.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi.ManageKpiMonthPerformance;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经营管理月度实绩指标数据表 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-10-24
 */
public interface ManageKpiMonthPerformanceService extends IService<ManageKpiMonthPerformance> {

    /**
     * 通过月份查询条件 分页 查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManageKpiMonthPerformance> findManageKpiMonthDataListPerPageByParams(Map<String, Object> params);

    /**
     * 查询一年十二个月的数据，展示查询,包含条件查询
     * @param params
     * @return
     */
    Page<Map<String,Object>> findManageKpiMonthDataMapListPerPageByParams(Map<String,Object> params);

    /**
     * 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readManageKpiMonthDataSource(Attachment attachment, String companyName, String year,String month);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findManageKpiMonthDataByParams(Map<String, Object> params);

    /**
     * 修改（更新）经营管理指标月度实绩值信息
     * @param manageKpiMonthPerformance
     * @return
     */
    boolean updateManageKpiMonthPerformanceData(ManageKpiMonthPerformance manageKpiMonthPerformance);
}
