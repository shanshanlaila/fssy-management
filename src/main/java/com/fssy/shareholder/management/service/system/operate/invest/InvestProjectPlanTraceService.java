package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTrace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_plan_trace	**数据表中文名：	项目进度计划跟踪表	**业务部门：	经营管理部	**数据表作用：	用于记录单一项目进展详情及跟踪情况	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
public interface InvestProjectPlanTraceService extends IService<InvestProjectPlanTrace> {

    /**
     * 通过查询条件，查询数据(导出）
     * @param params
     * @return
     */
    List<Map<String,Object>> findInvestProjectPlanTracejectDataByParams(Map<String, Object> params);


    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<InvestProjectPlanTrace> findInvestProjectPlanTraceDataListPerPageByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库
     * @param attachment
     * @return
     */
    Map<String,Object> reaadInvestProjectPlanTraceDataSource(Attachment attachment, String year, String companyName, String month, String projectName,String projectListId);


}
