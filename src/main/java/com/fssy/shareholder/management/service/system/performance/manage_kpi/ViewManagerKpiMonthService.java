package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiMonth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人KPI分数管理服务接口
 * VIEW 服务类 进行计算所需相关分数的查询
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
public interface ViewManagerKpiMonthService extends IService<ViewManagerKpiMonth>{

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ViewManagerKpiMonth> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findViewManagerKpiMonthMapDataByParams(Map<String, Object> params);

    /**
     * 根据条件查询所有数据
     * @param params
     * @return
     */
    List<ViewManagerKpiMonth> findViewManagerKpiMonthDataByParams(Map<String,Object> params);

    /**
     * 生成分数
     * @param
     * @return
     */
    boolean createScore (Map<String, Object> params);
}
