package com.fssy.shareholder.management.service.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManageYearMonthScore;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 计算经营管理月度分数视图 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-10
 */
public interface ViewManageYearMonthScoreService extends IService<ViewManageYearMonthScore> {
    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ViewManageYearMonthScore> findViewManageYearMonthScoreDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findViewManageYearMonthScoreMapDataByParams(Map<String, Object> params);
    /**
     * 生成分数
     * @param
     * @return
     */
    boolean createScore (Map<String, Object> params);
    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @return 数据列表
     */
    Map<String, Object> readViewManageYearMonthScoreDataSource(Attachment attachment, String companyName, String year,String month);
    /**
     * 修改分数记录
     */
    public boolean updateViewManageYearMonthScoreData(ManageKpiMonthAim manageKpiMonthAim);

}
