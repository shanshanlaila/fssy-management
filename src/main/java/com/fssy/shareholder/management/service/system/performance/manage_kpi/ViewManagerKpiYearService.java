package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiYear;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiYear;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW 经理人年度kpi指标 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-11-03
 */
public interface ViewManagerKpiYearService extends IService<ViewManagerKpiYear> {
    /**
     * 通过查询条件 分页 查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ViewManagerKpiYear> findViewManagerKpiYearDataListPerPageByParams(Map<String, Object> params);

    /**
     * 导入 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readViewManagerKpiYearDataSource(Attachment attachment,String companyName, String year);



    /**
     * 导出 通过查询条件查询履职计划map数据
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findViewManagerKpiYearDataByParams(Map<String, Object> params);
    /**
     * 修改经理人权重等信息
     * @param managerKpiYear
     * @return
     */
    boolean updateManagerKpiYearData(ManagerKpiYear managerKpiYear);
}
