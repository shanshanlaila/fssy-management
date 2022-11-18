package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiMonth;

import java.util.List;
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
     * 通过查询条件 分页查询列表（绩效分数计算）
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManagerKpiScoreOld> findManagerKpiScoreOldDataListPerPageByParams(Map<String, Object> params);
    /**
     * 通过查询条件 分页查询列表（年度推移）
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<Map<String,Object>> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readManagerKpiScoreOldDataSource(Attachment attachment);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findManagerKpiScoreOldDataByParams(Map<String, Object> params);

    /**
     * 以主键删除 分数记录
     */
    public boolean deleteManagerKpiScoreOldDataById(Integer id);

    /**
     * 修改分数记录
     */
    public boolean updateManagerKpiScoreOldData(ManagerKpiScoreOld managerKpiScoreOld);


}
