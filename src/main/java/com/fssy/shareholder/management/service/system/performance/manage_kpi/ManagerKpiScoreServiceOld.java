package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
public interface ManagerKpiScoreServiceOld extends IService<ManagerKpiScoreOld> {
    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManagerKpiScoreOld> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params);
}
