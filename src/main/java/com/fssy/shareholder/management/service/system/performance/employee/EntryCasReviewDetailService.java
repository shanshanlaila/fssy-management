package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工履职回顾明细		*****数据表名：	bs_performance_employee_entry_cas_review_detail		*****数据表作用：	员工每月月末对月初计划的回顾		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 服务类
 * </p>
 *
 * @author 农浩
 * @since 2022-10-20
 */
public interface EntryCasReviewDetailService extends IService<EntryCasReviewDetail> {
    /**
     * 根据参数分页查询数据
     *
     * @param params 查询参数
     * @return 数据分页
     */
    Page<EntryCasReviewDetail> findDataListByParams(Map<String, Object> params);

    /**
     * 履职回顾-部长审核（单条）
     *
     * @param entryCasReviewDetail 回顾
     * @return 结果
     */
    boolean updateEntryCasReviewDetail(EntryCasReviewDetail entryCasReviewDetail);

    /**
     * 工作计划完成情况提交审核
     *
     * @param reviewDetailIds
     * @return
     */
    boolean submitAudit(List<String> reviewDetailIds);

    /**
     * 工作计划完成情况撤销审核
     *
     * @param reviewDetailIds
     * @return
     */
    boolean retreat(List<String> reviewDetailIds);

    /**
     * 工作计划完成情况审核评价 （科长，事务类）
     *
     * @param entryCasReviewDetail 回顾履职
     * @return 结果
     */
    boolean sectionWorkAudit(EntryCasReviewDetail entryCasReviewDetail);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 履职计划回顾表附件
     * @return 履职管控计划回顾表附件map集合
     */
    Map<String, Object> readEntryCasReviewDetailDataSource(Attachment attachment);


    /**
     * 批量审核-计划工作完成情况评价（部长）
     *
     * @param entryReviewDetailIds 履职回顾的dis
     * @param ministerReview       部长复核
     * @return 审核结果
     */
    boolean batchAudit(List<String> entryReviewDetailIds, String ministerReview,List<String> auditNotes);

    /**
     * 批量审核-计划工作完成评价（科长）
     *
     * @param entryReviewDetailIds           履职回顾的Ids
     * @param chargeTransactionEvaluateLevel 事务类评价等级
     * @param chargeTransactionBelowType     事务类评价不同类型
     * @return
     */
    boolean batchAudit(List<String> entryReviewDetailIds, String chargeTransactionEvaluateLevel, String chargeTransactionBelowType,List<String> auditNotes);

    /**
     * 新增单履职回顾
     *
     * @param entryCasReviewDetail
     * @return
     */
    boolean saveOneReviewDetail(EntryCasReviewDetail entryCasReviewDetail);

    /**
     * 不根据计划创建履职回顾
     *
     * @param entryCasReviewDetail 回顾
     * @return 结果
     */
    Boolean storeReviewNotPlan(EntryCasReviewDetail entryCasReviewDetail);

    Page<Map<String, Object>> findDataListByMapParams(Map<String, Object> params);
}
