package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiCoefficient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人绩效系数表 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-11-07
 */
public interface ManagerKpiCoefficientService extends IService<ManagerKpiCoefficient> {
    /**
     * 通过查询条件 分页 查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManagerKpiCoefficient> findManagerKpiCoefficientDataListPerPageByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @param
     * @return 附件map集合
     */
    Map<String, Object> readManagerKpiCoefficientDataSource(Attachment attachment, HttpServletRequest request);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findManagerKpiCoefficientMapDataByParams(Map<String, Object> params);

    /**
     * 修改经理人系数信息
     * @param managerKpiCoefficient
     * @return
     */
    boolean updateManagerKpiCoefficientData(ManagerKpiCoefficient managerKpiCoefficient);


}
