package com.fssy.shareholder.management.service.system.manage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiMonthAim;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经营管理月度风险分析 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-24
 */
public interface ManageKpiMonthAnalyzeService extends IService<ManageKpiMonthAim> {
    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManageKpiMonthAim> findManageKpiMonthDataListPerPageByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @return 数据列表
     */
    Map<String, Object> readManageKpiMonthDataSource(Attachment attachment);

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findManageKpiMonthMapDataByParams(Map<String, Object> params);

}
