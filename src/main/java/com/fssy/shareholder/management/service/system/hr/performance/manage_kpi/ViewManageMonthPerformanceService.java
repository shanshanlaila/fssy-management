package com.fssy.shareholder.management.service.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi.ViewManageMonthPerformance;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视图年度月度实绩值 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-16
 */
public interface ViewManageMonthPerformanceService extends IService<ViewManageMonthPerformance> {
    /**
     * 通过条件进行 分页查询(一年十二个月的数据展示）
     * @param params 查询条件
     * @return 分页数据
     */
    Page<Map<String,Object>> findManageKpiMonthDataMapListPerPageByParams(Map<String,Object> params);
    /**
     * 读取附件数据到数据库表（年度目标的导入）
     *
     * @param attachment 附件
     * @param year
     * @return 附件map集合
     */
    Map<String, Object> readManageKpiYearDataSource(Attachment attachment, String year,String companyName);
    /**
     * 读取附件数据到数据库表(月度实绩导入）
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readManageKpiMonthPerformanceDataSource(Attachment attachment, String companyName, String year, String month);
    /**
     * 读取附件数据到数据库表（月度目标导入）
     *
     * @param attachment 附件
     * @return 数据列表
     */
    Map<String, Object> readManageKpiMonthAimDataSource(Attachment attachment,String companyName,String year);
    /**
     * 通过查询条件查询数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findViewManageMonthPerformanceMapDataByParams(Map<String, Object> params);
}
