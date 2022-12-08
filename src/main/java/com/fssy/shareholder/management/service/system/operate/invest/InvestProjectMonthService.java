package com.fssy.shareholder.management.service.system.operate.invest;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectMonth;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_month	**数据表中文名：	项目月度进展表	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目月度状态	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-12-05
 */
public interface InvestProjectMonthService extends IService<InvestProjectMonth> {
    /**
     * 通过查询条件，查询项目月度进展 数据
     * @param params
     * @return 项目月度进展数据
     */

    List<InvestProjectMonth> findInvestProjectMonthSDataByParams(Map<String,Object> params);

    /**
     * 查询一年十二个月的数据，展示查询,包含条件查询
     * @param params
     * @return
     */
    Page<Map<String,Object>> findInvestProjectMonthDataMapListPerPageByParams(Map<String,Object> params);

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<InvestProjectMonth> findInvestProjectMonthDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findInvestProjectMonthDataByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readInvestProjectMonthDataSource(Attachment attachment, String year);

    /**
     * 以主键删除 项目月度进展记录
     */
    public boolean deleteInvestProjectMonthDataById(Integer id);

    /**
     * 修改某条记录
     */
    public boolean updateInvestProjectMonthData(InvestProjectMonth investProjectMonth);
}
