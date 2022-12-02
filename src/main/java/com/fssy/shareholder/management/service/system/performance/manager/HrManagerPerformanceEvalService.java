package com.fssy.shareholder.management.service.system.performance.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.pojo.system.performance.manager.HrManagerPerformanceEval;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *经理人绩效汇总评分表  服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-30
 */
public interface HrManagerPerformanceEvalService extends IService<HrManagerPerformanceEval> {

    /**
     * 通过查询条件，查询经理人定性评价 数据(导出）
     * @param params
     * @return 经理人定性评价数据
     */

    List<Map<String,Object>> findHrManagerPerformanceEvalDataByParams(Map<String,Object> params);

    /**
     * 通过查询条件，查询经理人定性评价 数据(查询全部修改或者其他用途）
     * @param params
     * @return 经理人定性评价数据
     */

    List<HrManagerPerformanceEval> findHrHrManagerPerformanceEvalDataByParams(Map<String,Object> params);

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<HrManagerPerformanceEval> findHrManagerPerformanceEvalDataListPerPageByParams(Map<String, Object> params);

    /**
     * 添加经理人绩效汇总表信息
     *
     * @return
     */
    boolean updateScore(String year,String month);

    /**
     * 修改 经理人绩效汇总表 数据
     * @param hrManagerPerformanceEval 经理人绩效汇总表 实体
     * @return 经理人绩效汇总表据
     */
     boolean updateHrManagerPerformanceEval(HrManagerPerformanceEval hrManagerPerformanceEval);
}
