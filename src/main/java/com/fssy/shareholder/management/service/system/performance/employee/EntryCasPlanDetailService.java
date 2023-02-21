/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门： 绩效科 *****数据表中文名： 员工履职计划明细 *****数据表名：
 * bs_performance_employee_entry_cas_plan_detail *****数据表作用： 员工每月月初填写的履职情况计划明细
 * *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
public interface EntryCasPlanDetailService extends BaseService<EntryCasPlanDetail> {

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 履职管控计划表附件
     * @return 履职管控计划表附件map集合
     */
    Map<String, Object> readEntryCasPlanDetailDataSource(Attachment attachment);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     *
     * @param params 查询条件
     * @return 履职计划关系数据列表
     */
    List<Map<String, Object>> findEntryCasPlanDetailMapDataByParams(Map<String, Object> params);


    /**
     * 履职计划提交审核
     *
     * @param planDetailIds 计划ids
     * @return 审核成功结果
     */
    boolean submitAuditForPlan(List<String> planDetailIds);

    /**
     * 计划撤销审核
     *
     * @param planDetailIds 计划ids
     * @return 撤销成功结果
     */
    boolean retreatForPlan(List<String> planDetailIds);

    /**
     * 审核
     *
     * @param planDetailIds 计划ids
     * @param event 审核结果
     * @return 通过/拒绝
     */
    boolean affirmStore(List<String> planDetailIds, String event, List<String> auditNotes);

    /**
     * 单条填写履职计划
     *
     * @param entryCasPlanDetail 履职计划
     * @return 填写成功
     */
    boolean saveOneCasPlanDetail(EntryCasPlanDetail entryCasPlanDetail, HttpServletRequest request);

    /**
     * 根据参数分页查询数据-map
     *
     * @param params 查询参数
     * @return 数据分页
     */
    Page<Map<String, Object>> findDataListByMapParams(Map<String, Object> params);

    /**
     * 需要通知计划到期的时间
     * @return map
     */
    Map<Long, Map<String, Object>> findWeChatNoticeMap();
}
