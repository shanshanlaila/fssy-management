package com.fssy.shareholder.management.service.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi.ManagerKpiScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 经理人绩效分数表 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-31
 */
public interface ManagerKpiScoreService extends IService<ManagerKpiScore> {

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManagerKpiScore> findManagerKpiScoreDataListPerPageByParams(Map<String, Object> params);

}
